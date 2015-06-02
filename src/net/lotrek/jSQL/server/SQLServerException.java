package net.lotrek.jSQL.server;

public class SQLServerException extends Exception
{
	private static final long serialVersionUID = 7490714077424638684L;
	private SQLServerExceptionType type;
	
	public SQLServerException(SQLServerExceptionType type, String message)
	{
		super(message);
		this.type = type;
	}
	
	public SQLServerException(String message)
	{
		this(SQLServerExceptionType.UNEXPECTED_ERROR, message);
	}
	
	public SQLServerException()
	{
		this(null);
	}
	
	public String getMessage()
	{
		return String.format("%s : %s", type.name(), super.getMessage());
	}

	public SQLServerExceptionType getType()
	{
		return type;
	}
	
	public static enum SQLServerExceptionType
	{
		INCOMPATABLE_CLIENT,
		UNEXPECTED_PACKET,
		INVALID_REQUEST,
		UNEXPECTED_ERROR,
		;
	}
}
