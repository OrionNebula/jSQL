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
import net.lotrek.jSQL.packet.SQLBatch;
import net.lotrek.jSQL.packet.Token.TokenType;
import net.lotrek.jSQL.server.ServerConfig.LoginContext;
import net.lotrek.jSQL.packet.SQLPacket;
import net.lotrek.jSQL.packet.Tabular;
import net.lotrek.jSQL.packet.Token;

public enum ConnectionState
{
	INITIAL(PacketType.PRELOGIN.getPacketClass(), (server, client, toRespond, dos) -> {
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
	TLSSSLNEGO(null, (server, client, toRespond, dos) -> {
		
		return "LOGIN";
	}),
	LOGIN(PacketType.LOGIN.getPacketClass(), (server, client, toRespond, dos) -> {
		Login packet = (Login)toRespond;
		
		server.getConfiguration().setPacketSize(packet.getPacketSize());
		
		server.getServerOut().printf("Client app \"%s\" connected to server \"%s\" from hostname \"%s\"\n",
				new String(packet.getDataKey(packet.getOffset().getIbAppName(), packet.getOffset().getCchAppName() * 2)).replace("\0", ""),
				new String(packet.getDataKey(packet.getOffset().getIbServerName(), packet.getOffset().getCchServerName() * 2)).replace("\0", ""),
				new String(packet.getDataKey(packet.getOffset().getIbHostname(), packet.getOffset().getCchHostName() * 2)).replace("\0", ""));
		
		LoginContext passAuth = server.getConfiguration().getAuthProvider().generateLogin(SQLTools.readVarchar(packet.getDataKey(packet.getOffset().getIbUserName(), packet.getOffset().getCchUserName() * 2)),
				SQLTools.readVarchar(SQLTools.swapPasswordEncrypt(packet.getDataKey(packet.getOffset().getIbPassword(), packet.getOffset().getCchPassword() * 2))));
		
		PacketHeader head = new PacketHeader().setEOM(true).setSpid(SQLTools.getPID()).setPacketId(1).setType(PacketType.TABULAR);
		Tabular tabPacket = new Tabular();
		DataOutputStream tabStream = new DataOutputStream(tabPacket.getStream());
		
		try{
			if(passAuth != null)
				new Token(TokenType.LOGINACK)
					.setProperty("Interface", 1)
					.setProperty("TDSVersion", 0x74000004)
					.setProperty("ProgName", server.getConfiguration().getServerName())
					.setProperty("MajorVer", 0x0B)
					.setProperty("MinorVer", 0)
					.setProperty("BuildNumHi", 0x08)
					.setProperty("BuildNumLow", 0xCB).write(tabStream);
			else
				new Token(TokenType.ERROR)
				.setProperty("Number", 18456)
				.setProperty("State", 0)
				.setProperty("Class", 14)
				.setProperty("MsgText", "Login Failed for User")
				.setProperty("ServerName", server.getConfiguration().getServerName())
				.setProperty("ProcName", "")
				.setProperty("LineNumber", 0).write(tabStream);
			
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
		
		return passAuth != null ? "LOGGEDIN" : "FINAL";
	}),
	SPNEGO(null, (server, client, toRespond, dos) -> {
		
		return "LOGGEDIN";
	}),
	LOGGEDIN(PacketType.BATCH.getPacketClass(), (server, client, toRespond, dos) -> {
		
		SQLBatch pack = (SQLBatch)toRespond;
		String toProcess = SQLTools.readVarchar(pack.getPacketData());
		
		System.out.println(toProcess);
		
		return "LOGGEDIN";
	}),
	FINAL(SQLPacket.class, (server, client, toRespond, dos) -> {
		client.terminate();
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
		public String write(SQLServer server, ServerClient client, SQLPacket toRespond, DataOutputStream dos) throws IOException;
	}
}
