package net.lotrek.jSQL.transact;

public class ServerArch
{
	public static class UserPermission
	{
		public UserPermission()
		{
			
		}
	}
	
	public static class ServerObject
	{
		
	}
	
	public static enum ObjectType
	{
		SERVER,
		DATABASE,
		TABLE,
		SCHEMA
		;
	}
	
	public static enum PermissionType
	{
		CONTORL,
		ALTER,
		ALTER_ANY,
		TAKE_OWNERSHIP,
		IMPERSONATE,
		CREATE,
		VIEW_DEFINITION,
		REFERENCES,
		;
	}
}
