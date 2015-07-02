package net.lotrek.jSQL.data;

public interface IRowAcceptor
{
	public void consumeColumns(DataColumn[] cols);
	public void consumeRow(DataRow row);
	public void consumeStream(IRowStream stream);
	public int consumeStream(IRowStream stream, int rowCount);
	public void consumeDataProvider(IRowDataProvider prov);
}
