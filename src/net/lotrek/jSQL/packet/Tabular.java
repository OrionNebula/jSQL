package net.lotrek.jSQL.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Tabular implements SQLPacket
{
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	public void read(PacketHeader head, DataInputStream dis) throws IOException
	{
		int totLength = head.getTotLength() - 8;
		byte[] b = new byte[totLength];
		dis.readFully(b);
		baos.write(b);
	}

	public void write(DataOutputStream dos) throws IOException
	{
		dos.write(baos.toByteArray());
	}
	
	public ByteArrayOutputStream getStream()
	{
		return baos;
	}

	public int getPayloadLength()
	{
		return baos.size();
	}

}
