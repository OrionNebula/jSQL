package net.lotrek.jSQL.data;

import java.io.IOException;
import java.io.OutputStream;

public class CSVRowAcceptor implements IRowAcceptor
{
	private OutputStream fos;
	private DataColumn[] cols;
	private char delim;
	
	public CSVRowAcceptor(OutputStream stream, char delimiter, DataColumn[] cols)
	{
		delim = delimiter;
		fos = stream;
		this.consumeColumns(cols);
	}
	
	public void consumeColumns(DataColumn[] cols)
	{
		try {
			if(this.cols == null)
			{
				this.cols = cols;
				for (int i = 0; i < cols.length; i++)
					fos.write((cols[i].getName() + (i+1 != cols.length ? delim : "")).getBytes());
				fos.write("\r\n".getBytes());
			}
			else
				throw new IllegalStateException("Columns have already been consumed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void consumeRow(DataRow row)
	{
		try {
			for (int i = 0; i < row.rowData().length; i++)
				fos.write((row.rowData()[i] + (i+1 != row.rowData().length ? delim : "")).getBytes());
			fos.write("\r\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void consumeStream(IRowStream stream)
	{
		if(stream.isRowCountSupported())
			consumeStream(stream, stream.getRowCount());
	}

	public int consumeStream(IRowStream stream, int rowCount)
	{
		if(stream.isRowCountSupported() && stream.getRowCount() < rowCount)
			rowCount = stream.getRowCount();
		
		int i = 0;
		for (;i < rowCount && stream.hasNextRow(); i++)
			consumeRow(stream.readRow());
		
		return i;
	}

	public void consumeDataProvider(IRowDataProvider prov)
	{
		consumeStream(prov);
	}
}
