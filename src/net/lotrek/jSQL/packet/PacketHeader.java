package net.lotrek.jSQL.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketHeader
{
	private PacketType type;
	private boolean EOM, IGNORE, RESETCONNECTION, RESETCONNECTIONSKIPTRAN;
	private int totLength, spid, packetId;
	
	public void read(DataInputStream dis) throws IOException
	{
		type = PacketType.values()[dis.readByte() - 1];
		int status = dis.readByte();
		EOM = (status & 1) != 0;
		IGNORE = (status & 2) != 0;
		RESETCONNECTION = (status & 8) != 0;
		RESETCONNECTIONSKIPTRAN = (status & 0x10) != 0;
		totLength = dis.readUnsignedShort();
		spid = dis.readUnsignedShort();
		packetId = dis.readUnsignedByte();
		dis.readByte();
	}
	
	public void write(DataOutputStream dos) throws IOException
	{
		dos.writeByte(type.getTypeId());
		dos.writeByte((EOM ? 1 : 0) | (IGNORE ? 2 : 0) | (RESETCONNECTION ? 8 : 0) | (RESETCONNECTIONSKIPTRAN ? 0x10 : 0));
		dos.writeShort(totLength);
		dos.writeShort(spid);
		dos.writeByte(packetId);
		dos.writeByte(0);
	}
	
	public String toString()
	{
		return "PacketHeader [type=" + type + " (0x" + Integer.toHexString(type.getTypeId()) + "), EOM=" + EOM + ", IGNORE="
				+ IGNORE + ", RESETCONNECTION=" + RESETCONNECTION
				+ ", RESETCONNECTIONSKIPTRAN=" + RESETCONNECTIONSKIPTRAN
				+ ", totLength=" + totLength + ", spid=" + spid + ", packetId="
				+ packetId + "]";
	}
	
	public PacketType getType() {
		return type;
	}

	public PacketHeader setType(PacketType type) {
		this.type = type;
		
		return this;
	}

	public boolean isEOM() {
		return EOM;
	}

	public PacketHeader setEOM(boolean eOM) {
		EOM = eOM;
		
		return this;
	}

	public boolean isIGNORE() {
		return IGNORE;
	}

	public PacketHeader setIGNORE(boolean iGNORE) {
		IGNORE = iGNORE;
		
		return this;
	}

	public boolean isRESETCONNECTION() {
		return RESETCONNECTION;
	}

	public PacketHeader setRESETCONNECTION(boolean rESETCONNECTION) {
		RESETCONNECTION = rESETCONNECTION;
		
		return this;
	}

	public boolean isRESETCONNECTIONSKIPTRAN() {
		return RESETCONNECTIONSKIPTRAN;
	}

	public PacketHeader setRESETCONNECTIONSKIPTRAN(boolean rESETCONNECTIONSKIPTRAN) {
		RESETCONNECTIONSKIPTRAN = rESETCONNECTIONSKIPTRAN;
		
		return this;
	}

	public int getTotLength() {
		return totLength;
	}

	public PacketHeader setTotLength(int totLength) {
		this.totLength = totLength;
		
		return this;
	}

	public int getSpid() {
		return spid;
	}

	public PacketHeader setSpid(int spid) {
		this.spid = spid;
		
		return this;
	}

	public int getPacketId() {
		return packetId;
	}

	public PacketHeader setPacketId(int packetId) {
		this.packetId = packetId;
		
		return this;
	}
}
