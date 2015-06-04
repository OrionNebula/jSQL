package net.lotrek.jSQL.packet;

public enum PacketType
{
	BATCH(1, SQLBatch.class),
	LEGACYLOGIN(2, null),
	RPC(3, null),
	TABULAR(4, null),
	UNUSED_5(5, null),
	ATTENTION(6, null),
	BULK(7, null),
	UNUSED_8(8, null),
	UNUSED_9(9, null),
	UNUSED_10(10, null),
	UNUSED_11(11, null),
	UNUSED_12(12, null),
	UNUSED_13(13, null),
	TRANSACTION(14, null),
	UNUSED_15(15, null),
	LOGIN(16, Login.class),
	SSPI(17, null),
	PRELOGIN(18, PreLogin.class),
	;
	
	private Class<? extends SQLPacket> packClass;
	private int typeId;
	
	private PacketType(int typeId, Class<? extends SQLPacket> packClass)
	{
		this.typeId = typeId;
		this.packClass = packClass;
	}
	
	public int getTypeId()
	{
		return typeId;
	}
	
	public Class<? extends SQLPacket> getPacketClass()
	{
		return packClass;
	}
}
