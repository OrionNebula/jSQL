package net.lotrek.jSQL.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface SQLPacket
{
	public void read(PacketHeader head, DataInputStream dis) throws IOException;
	public void write(DataOutputStream dos) throws IOException;
	public int getPayloadLength();
}
