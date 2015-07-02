package net.lotrek.jSQL.data;

import java.util.List;

public interface IRowDataProvider extends IRowStream
{
	public List<DataRow> getRows();
}