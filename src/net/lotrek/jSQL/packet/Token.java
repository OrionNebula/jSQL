package net.lotrek.jSQL.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import net.lotrek.jSQL.SQLTools;

public class Token
{
	private TokenType type;
	private HashMap<String, Object> valueMap = new HashMap<>();
	
	public Token(TokenType type)
	{
		this.type = type;
	}
	
	public Token setProperty(String key, Object value)
	{
		valueMap.put(key, value);
		
		return this;
	}
	
	public void write(DataOutputStream dos) throws IOException, Exception
	{
		if(!valueMap.keySet().containsAll(Arrays.asList(type.getRequiredValues())))
			throw new Exception("Cannot write token, missing properties required for " + type);
		
		dos.writeByte(type.getType());
		type.getWriteToken().write(dos, valueMap);
	}
	
	public static enum TokenType
	{
		ALTMETADATA(0x88, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		ALTROW(0xD3, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		COLINFO(0xA5, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		COLMETADATA(0x81, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		DONE(0xFD, new String[]{
				"Status",
				"CurCmd",
				"DoneRowCount",
		}, (dos, prop) -> {
			dos.writeShort((int)prop.get("Status"));
			dos.writeShort((int)prop.get("CurCmd"));
			dos.writeLong((long)prop.get("DoneRowCount"));
		}),
		DONEINPROC(0xFF, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		DONEPROC(0xFE, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		ENVCHANGE(0xE3, new String[]{
				"Type",
				"NewValue",
				"OldValue"
		}, (dos, prop) -> {
			int type = (int)prop.get("Type");
			byte[] newValue = (byte[])prop.get("NewValue"),
					oldValue = (byte[])prop.get("oldValue");
			
			dos.writeShort(1 + newValue.length + oldValue.length);
			
			dos.writeByte(type);
			dos.write(newValue, 0, newValue.length);
			dos.write(oldValue, 0, oldValue.length);
		}),
		ERROR(0xAA, new String[]{
				"Number",
				"State",
				"Class",
				"MsgText",
				"ServerName",
				"ProcName",
				"LineNumber"
		}, (dos, prop) -> {
			int number = (int)prop.get("Number"), state = (int)prop.get("State"), clazz = (int)prop.get("Class"), lineNumber = (int)prop.get("LineNumber");
			String msgText = (String)prop.get("MsgText"), serverName = (String)prop.get("ServerName"), procName = (String)prop.get("ProcName");
			
			dos.writeShort(16 + 2*(msgText.length() + serverName.length() + procName.length()));
			dos.writeInt(Integer.reverseBytes(number));
			dos.writeByte(state);
			dos.writeByte(clazz);
			
			dos.writeShort(Short.reverseBytes((short)msgText.length()));
			SQLTools.writeVarchar(msgText, dos);
			dos.writeByte(serverName.length());
			SQLTools.writeVarchar(serverName, dos);
			dos.writeByte(procName.length());
			SQLTools.writeVarchar(procName, dos);
			
			dos.writeInt(lineNumber);
			
		}),
		FEATUREEXTACK(0xAE, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		INFO(0xAB, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		LOGINACK(0xAD, new String[]{
				"Interface",
				"TDSVersion",
				"ProgName",
				"MajorVer",
				"MinorVer",
				"BuildNumHi",
				"BuildNumLow"
		}, (dos, prop) -> {
			String progName = (String)prop.get("ProgName");
			int intface = (int)prop.get("Interface"),
					TDSVersion = (int)prop.get("TDSVersion"),
					majorVer = (int)prop.get("MajorVer"),
					minorVer = (int)prop.get("MinorVer"),
					buildNumHi = (int)prop.get("BuildNumHi"),
					buildNumLow = (int)prop.get("BuildNumLow");
			
			//Length, Interface, TDSVersion
			dos.writeShort(10 + progName.length()*2);
			dos.writeByte(intface);
			dos.writeInt(TDSVersion);
			
			//ProgName
			dos.writeByte(progName.length());
			SQLTools.writeVarchar(progName, dos);
			
			//ProgVersion
			dos.writeByte(majorVer);
			dos.writeByte(minorVer);
			dos.writeByte(buildNumHi);
			dos.writeByte(buildNumLow);
		}),
		NBCROW(0xD2, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		OFFSET(0x78, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		ORDER(0xA9, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		RETURNSTATUS(0x79, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		RETURNVALUE(0xAC, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		ROW(0xD1, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		SESSIONSTATE(0xE4, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		SSPI(0xED, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		TABNAME(0xA4, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		TVP_ROW(0x01, new String[]{
				
		}, (dos, prop) -> {
			
		}),
		;
		
		private int type;
		private WriteToken token;
		private String[] values;
		
		private TokenType(int type, String[] requiredValues, WriteToken token)
		{
			this.type = type;
			this.token = token;
			this.values = requiredValues;
		}
		
		public int getType()
		{
			return type;
		}
		
		public WriteToken getWriteToken()
		{
			return token;
		}
		
		public String[] getRequiredValues()
		{
			return values;
		}
		
		public static interface WriteToken
		{
			public void write(DataOutputStream dos, HashMap<String, Object> properties) throws IOException;
		}
	}
}
