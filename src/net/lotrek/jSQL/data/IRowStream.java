package net.lotrek.jSQL.data;

public interface IRowStream
{
	public String getName();
	public DataColumn[] getColumns();
	public boolean isRowCountSupported();
	public int getRowCount();
	public DataRow readRow();
	public boolean hasNextRow();
}
