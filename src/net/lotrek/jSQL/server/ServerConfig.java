package net.lotrek.jSQL.server;

public class ServerConfig
{
	private EncryptionSetting encSet;
	private AuthProvider prov;
	private int packetSize;
	private String serverName;
	
	public ServerConfig(String serverName, EncryptionSetting enc, AuthProvider authProvider)
	{
		this.serverName = serverName;
		prov = authProvider;
		this.encSet = enc;
	}
	
	public EncryptionSetting getEncryption()
	{
		return encSet;
	}
	
	public AuthProvider getAuthProvider()
	{
		return prov;
	}
	
	public String getServerName()
	{
		return serverName;
	}
	
	public int getPacketSize()
	{
		return packetSize;
	}

	public void setPacketSize(int packetSize)
	{
		this.packetSize = packetSize;
	}

	public static interface AuthProvider
	{
		public boolean isValidCredentials(String username, String password);
	}
	
	public static enum EncryptionSetting
	{
		ENCRYPT_OFF(0),
		ENCRYPT_ON(1),
		ENCRYPT_NOT_SUP(2),
		ENCRYPT_REQ(3),
		;
		
		private int val;
		
		private EncryptionSetting(int value)
		{
			val = value;
		}
		
		public int getValue()
		{
			return val;
		}
	}
}
