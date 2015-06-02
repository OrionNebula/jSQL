package net.lotrek.jSQL.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.lotrek.jSQL.SQLTools;
import net.lotrek.jSQL.packet.Login;
import net.lotrek.jSQL.packet.PacketHeader;
import net.lotrek.jSQL.packet.PacketType;
import net.lotrek.jSQL.packet.PreLogin;
import net.lotrek.jSQL.packet.PreLogin.PL_OPTION_TOKEN;
import net.lotrek.jSQL.packet.PreLogin.PRELOGIN_OPTION;
import net.lotrek.jSQL.packet.Token.TokenType;
import net.lotrek.jSQL.packet.SQLPacket;
import net.lotrek.jSQL.packet.Tabular;
import net.lotrek.jSQL.packet.Token;

public enum ConnectionState
{
	INITIAL(PacketType.PRELOGIN.getPacketClass(), (server, toRespond, dos) -> {
		PreLogin packet = (PreLogin)toRespond;
		
		PreLogin data = new PreLogin();

		data.addPreLoginOption(new PRELOGIN_OPTION()
				.setOptionToken(PL_OPTION_TOKEN.VERSION)
				.setLength(6)
				.setOffset(0)
				.setData(packet.getOptionByToken(PL_OPTION_TOKEN.VERSION).getData()));
		data.addPreLoginOption(new PRELOGIN_OPTION()
				.setOptionToken(PL_OPTION_TOKEN.ENCRYPTION)
				.setLength(1)
				.setOffset(6)
				.setData(new byte[]{(byte) server.getConfiguration().getEncryption().getValue()}));
		data.addPreLoginOption(new PRELOGIN_OPTION()
				.setOptionToken(PL_OPTION_TOKEN.INSTOPT)
				.setLength(1)
				.setOffset(7)
				.setData(new byte[]{0}));
		data.addPreLoginOption(new PRELOGIN_OPTION()
				.setOptionToken(PL_OPTION_TOKEN.MARS)
				.setLength(1)
				.setOffset(12)
				.setData(new byte[]{0}));
		data.addPreLoginOption(PRELOGIN_OPTION.TERMINATOR);
		
		SQLTools.sendAggrigatePacket(dos,
				new PacketHeader().setType(PacketType.TABULAR).setEOM(true).setTotLength(8 + data.getPayloadLength()).setSpid(SQLTools.getPID()).setPacketId(1),
				data);
		
		return "LOGIN";
	}),
	TLSSSLNEGO(null, (server, toRespond, dos) -> {
		
		return "LOGIN";
	}),
	LOGIN(PacketType.LOGIN.getPacketClass(), (server, toRespond, dos) -> {
		Login packet = (Login)toRespond;
		
		server.getConfiguration().setPacketSize(packet.getPacketSize());
		
		server.getServerOut().printf("Client app \"%s\" connected to server \"%s\" from hostname \"%s\"\n",
				new String(packet.getDataKey(packet.getOffset().getIbAppName(), packet.getOffset().getCchAppName() * 2)).replace("\0", ""),
				new String(packet.getDataKey(packet.getOffset().getIbServerName(), packet.getOffset().getCchServerName() * 2)).replace("\0", ""),
				new String(packet.getDataKey(packet.getOffset().getIbHostname(), packet.getOffset().getCchHostName() * 2)).replace("\0", ""));
		
		boolean passAuth = server.getConfiguration().getAuthProvider().isValidCredentials(
				new String(packet.getDataKey(packet.getOffset().getIbUserName(), packet.getOffset().getCchUserName() * 2)).replace("\0", ""),
				new String(SQLTools.swapPasswordEncrypt(packet.getDataKey(packet.getOffset().getIbPassword(), packet.getOffset().getCchPassword() * 2))).replace("\0", ""));
		
		PacketHeader head = new PacketHeader().setEOM(true).setSpid(SQLTools.getPID()).setPacketId(1).setType(PacketType.TABULAR);
		Tabular tabPacket = new Tabular();
		DataOutputStream tabStream = new DataOutputStream(tabPacket.getStream());
		
		try{
			new Token(TokenType.LOGINACK)
				.setProperty("Interface", 1)
				.setProperty("TDSVersion", 0x74000004)
				.setProperty("ProgName", server.getConfiguration().getServerName())
				.setProperty("MajorVer", 0x0B)
				.setProperty("MinorVer", 0)
				.setProperty("BuildNumHi", 0x08)
				.setProperty("BuildNumLow", 0xCB).write(tabStream);
			
			new Token(TokenType.DONE)
			.setProperty("Status", 0)
			.setProperty("CurCmd", 0)
			.setProperty("DoneRowCount", 0l).write(tabStream);
		}catch(Exception e)
		{
			e.printStackTrace(server.getServerOut());
		}
		
		head.setTotLength(8 + tabStream.size());
		
		SQLTools.sendAggrigatePacket(dos, head, tabPacket);
		SQLTools.sendAggrigatePacket(new DataOutputStream(new FileOutputStream(new File("sendPack.bin"))), head, tabPacket);
		
		return "LOGGEDIN";
	}),
	SPNEGO(null, (server, toRespond, dos) -> {
		
		return "LOGGEDIN";
	}),
	LOGGEDIN(null, (server, toRespond, dos) -> {
		
		return "REQUESTEXEC";
	}),
	REQUESTEXEC(null, (server, toRespond, dos) -> {
		
		return "LOGGEDIN";
	}),
	FINAL(null, (server, toRespond, dos) -> {
		
		return "FINAL";
	}),
	;
	
	private Class<? extends SQLPacket> expect;
	private PacketResponse response;
	
	private ConnectionState(Class<? extends SQLPacket> expectingClass, PacketResponse response)
	{
		this.expect = expectingClass;
		this.response = response;
	}
	
	public Class<? extends SQLPacket> getExpectingClass()
	{
		return expect;
	}
	
	public PacketResponse getResponse()
	{
		return response;
	}
	
	public static interface PacketResponse
	{
		public String write(SQLServer server, SQLPacket toRespond, DataOutputStream dos) throws IOException;
	}
}
