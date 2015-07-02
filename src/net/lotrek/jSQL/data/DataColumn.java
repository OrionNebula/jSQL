package net.lotrek.jSQL.data;

public class DataColumn implements Cloneable
{
	private DataTable parent;
	private String colName;
	private SQLType type;
	
	public DataColumn(String columnName, SQLType dataType)
	{
		colName = columnName;
		type = dataType;
	}
	
	public void setDataTable(DataTable parent)
	{
		if(this.parent == null)
			this.parent = parent;
		else
			throw new IllegalStateException(this + " already has a parent DataTable");
	}
	
	public DataTable getDataTable()
	{
		return parent;
	}
	
	public String getName()
	{
		return colName;
	}
	
	public SQLType getType()
	{
		return type;
	}
	
	public String toString()
	{
		return "DataColumn [parent=" + parent + ", colName=" + colName + ", type=" + type + "]";
	}
	
	public DataColumn clone()
	{
		return new DataColumn(this.colName, this.type);
	}
}