package net.lotrek.jSQL.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.ArrayList;

public class SQLServer implements Runnable
{
	private ArrayList<ServerClient> clientList = new ArrayList<>();
	private ServerSocket sock;
	private volatile boolean shouldTerm;
	private Thread th;
	private PrintStream out;
	private ServerConfig options;
	
	public SQLServer(PrintStream out, int port, ServerConfig config) throws IOException
	{
		this.options = config;
		this.out = out;
		sock = new ServerSocket(port);
		th = new Thread(this);
		th.start();
	}

	public ServerConfig getConfiguration()
	{
		return options;
	}
	
	public PrintStream getServerOut()
	{
		return out;
	}
	
	public void run()
	{
		while(true)
		{
			if(shouldTerm)
				break;
			
			try {
				ServerClient cli = new ServerClient(this, sock.accept());
				clientList.add(cli);
				cli.start();
			} catch (IOException e)
			{
				e.printStackTrace(out);
			}
		}
	}
	
	public void terminate()
	{
		shouldTerm = true;
		try {
			sock.close();
		} catch (IOException e1) {
			e1.printStackTrace(out);
		} finally {
			for (ServerClient serverClient : clientList)
				try {
					serverClient.terminate();
				} catch (IOException e1) {
					e1.printStackTrace(out);
				}
			
			try {
				th.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
