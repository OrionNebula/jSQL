package net.lotrek.jSQL.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.lotrek.jSQL.packet.PacketHeader;
import net.lotrek.jSQL.packet.SQLPacket;
import net.lotrek.jSQL.server.SQLServerException.SQLServerExceptionType;

public class ServerClient implements Runnable
{
	private Thread th;
	private Socket sock;
	private DataInputStream dis;
	private DataOutputStream dos;
	private SQLServer server;
	private ConnectionState conState = ConnectionState.INITIAL;
	
	public ServerClient(SQLServer parent, Socket sock) throws IOException
	{
		this.server = parent;
		this.sock = sock;
		dis = new DataInputStream(sock.getInputStream());
		dos = new DataOutputStream(sock.getOutputStream());
	}
	
	public void start()
	{
		th = new Thread(this);
		th.start();
	}
	
	public SQLServer getServer()
	{
		return server;
	}
	
	public ConnectionState getConnectionState()
	{
		return conState;
	}
	
	public void run()
	{
		while(true)
		{
			if(sock.isClosed())
				break;
			
			try {
				if(dis.available() > 0)
				{
					PacketHeader head = new PacketHeader();
					head.read(dis);
					getServer().getServerOut().println(head);
					SQLPacket pack = head.getType().getPacketClass().newInstance();
					pack.read(head, dis);
					getServer().getServerOut().println(pack);
					if(!pack.getClass().equals(getConnectionState().getExpectingClass()))
					{
						dos.close();
						dis.close();
						sock.close();
						throw new RuntimeException(new SQLServerException(SQLServerExceptionType.UNEXPECTED_PACKET, String.format("Recieved %s when %s was expected", pack.getClass(), getConnectionState().getExpectingClass())));
					}
					conState = ConnectionState.valueOf(getConnectionState().getResponse().write(getServer(), this, pack, dos));
					if(conState == ConnectionState.FINAL)
					{
						terminate();
						break;
					}
				}
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected()
	{
		return !sock.isClosed();
	}
	
	public void terminate() throws IOException
	{
		dos.close();
		dis.close();
		if(isConnected())
			sock.close();
		
		if(Thread.currentThread() != th)
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
