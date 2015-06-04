package net.lotrek.jSQL.server;

import net.lotrek.jSQL.server.ServerConfig.LoginContext;
import net.lotrek.jSQL.transact.ServerArch.UserPermission;

public class AllowAllLoginContext implements LoginContext
{
	public boolean hasPermission(UserPermission perm)
	{
		return true;
	}

	public void grantPermission(UserPermission perm)
	{
		
	}

	public void revokePermission(UserPermission perm)
	{
		
	}
	
}
