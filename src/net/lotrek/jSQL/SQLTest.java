package net.lotrek.jSQL;

import java.io.IOException;
import java.util.Scanner;

import net.lotrek.jSQL.server.AllowAllLoginContext;
import net.lotrek.jSQL.server.SQLServer;
import net.lotrek.jSQL.server.ServerConfig;
import net.lotrek.jSQL.server.ServerConfig.EncryptionSetting;

public class SQLTest
{
	public static void main(String[] args)
	{
		try {
			SQLServer server = new SQLServer(System.out, 1433,
					new ServerConfig("jSQL", EncryptionSetting.ENCRYPT_NOT_SUP, (username, password) -> {
						System.out.println(username + " : " + password);
						return (username.equals("username") && password.equals("password") ? new AllowAllLoginContext() : null);
					}));
			Scanner in = new Scanner(System.in);
			System.out.println(in.nextLine());
			server.terminate();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
