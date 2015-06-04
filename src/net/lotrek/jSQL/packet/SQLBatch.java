package net.lotrek.jSQL.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLBatch implements SQLPacket
{
	private byte[] data;
	private ArrayList<AllHeader> allHeader = new ArrayList<>();
	
	public void read(PacketHeader head, DataInputStream dis) throws IOException
	{
		int allLen = Integer.reverseBytes(dis.readInt()) - 4;
		int totLen = head.getTotLength() - 8 - allLen - 4;
		while(allLen > 0)
		{
			AllHeader aHead = new AllHeader();
			aHead.read(dis);
			allHeader.add(aHead);
			allLen -= aHead.getHeaderLength();
		}
		
		data = new byte[totLen];
		dis.readFully(data);
	}

	public void write(DataOutputStream dos) throws IOException
	{
		
	}

	public ArrayList<AllHeader> getAllHeader()
	{
		return allHeader;
	}
	
	public byte[] getPacketData()
	{
		return data;
	}
	
	public int getPayloadLength()
	{
		return data.length;
	}
	
	public static class AllHeader
	{
		private int headerLength, headerType;
		private byte[] headerData;
		
		public void read(DataInputStream dis) throws IOException
		{
			headerLength = Integer.reverseBytes(dis.readInt());
			headerType = Short.reverseBytes(dis.readShort());
			headerData = new byte[headerLength - 6];
			dis.readFully(headerData);
		}

		public int getHeaderLength() {
			return headerLength;
		}

		public void setHeaderLength(int headerLength) {
			this.headerLength = headerLength;
		}

		public int getHeaderType() {
			return headerType;
		}

		public void setHeaderType(int headerType) {
			this.headerType = headerType;
		}

		public byte[] getHeaderData() {
			return headerData;
		}

		public void setHeaderData(byte[] headerData) {
			this.headerData = headerData;
		}

		@Override
		public String toString() {
			return "AllHeader [headerLength=" + headerLength + ", headerType=" + headerType + ", headerData="
					+ Arrays.toString(headerData) + "]";
		}
		
		
	}
}
