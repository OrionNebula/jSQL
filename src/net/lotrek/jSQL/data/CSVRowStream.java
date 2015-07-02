package net.lotrek.jSQL.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class CSVRowStream implements IRowStream
{
	private Scanner reader;
	private String tableName;
	private char delim;
	private DataColumn[] cachedColumns;
	
	public CSVRowStream(String tableName, InputStream is, char delimiter) throws IOException
	{
		this.tableName = tableName;
		this.delim = delimiter;
		reader = new Scanner(is);
		String[] cols = reader.nextLine().split("\\"+delimiter);
		cachedColumns = new DataColumn[cols.length];
		for (int i = 0; i < cols.length; i++)
			cachedColumns[i] = new DataColumn(cols[i], null);
	}
	
	public String getName()
	{
		return tableName;
	}

	public DataColumn[] getColumns()
	{
		return cachedColumns;
	}

	public boolean isRowCountSupported()
	{
		return false;
	}

	public int getRowCount()
	{
		throw new UnsupportedOperationException("getRowCount is not supported for CSVRowStream");
	}

	public DataRow readRow()
	{
		if(!hasNextRow())
			return null;
		
		String line = reader.nextLine();
		if(line.isEmpty() && !hasNextRow())
			return null;
		else if(hasNextRow() && line.isEmpty()) return readRow();
		return new DataRow(cachedColumns, line.split("\\"+delim));
	}

	public boolean hasNextRow()
	{
		return reader.hasNextLine();
	}
}
