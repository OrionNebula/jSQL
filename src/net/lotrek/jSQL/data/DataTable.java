package net.lotrek.jSQL.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class DataTable
{
	private DataColumn[] columns;
	private List<DataRow> rows;
	private String tableName;
	
	public DataTable(String tableName, DataColumn[] columns, List<DataRow> rows)
	{
		this.tableName = tableName;
		this.columns = columns;
		this.rows = rows;
		for (DataColumn col : columns)
			col.setDataTable(this);
	}
	
	public DataTable(String tableName, DataColumn[] columns, DataRow[] rows)
	{
		this(tableName, columns, Arrays.asList(rows));
	}
	
	public DataTable(String tableName, DataColumn[] columns)
	{
		this(tableName, columns, new DataRow[0]);
	}
	
	public List<DataRow> getRows()
	{
		return rows;
	}
	
	public DataColumn[] getColumns()
	{
		return this.columns;
	}
	
	public String getName()
	{
		return this.tableName;
	}
	
	public DataTableRowDataProvider getRowDataProvider()
	{
		return new DataTableRowDataProvider(this);
	}
	
	public DataTable select(Predicate<DataRow> where)
	{
		DataColumn[] newColumns = new DataColumn[this.getColumns().length];
		for (int i = 0; i < newColumns.length; i++)
			newColumns[i] = this.getColumns()[i].clone();
		
		List<DataRow> rows = new ArrayList<>();
		for (DataRow dataRow : this.getRows())
			if(where.test(dataRow))
				rows.add(new DataRow(newColumns, dataRow.rowData()));
		
		return  new DataTable(this.getName(), newColumns, rows);
	}
	
	public DataTable join(String tableName, JoinType type, DataTable other, DataColumn[] resultColumns, BiPredicate<DataRow, DataRow> on)
	{
		DataColumn[] colData = new DataColumn[resultColumns.length];
		for(int i = 0; i < colData.length; i++)
			colData[i] = resultColumns[i].clone();
		List<DataRow> rows = new ArrayList<>();
		boolean[] thisMatch = new boolean[this.getRows().size()], thatMatch = new boolean[other.getRows().size()];
		int outer = 0;
		for (DataRow thisRow : this.getRows())
		{
			int inner = 0;
			for(DataRow thatRow : other.getRows())
			{
				if(on.test(thisRow, thatRow))
				{
					thisMatch[outer] = true;
					thatMatch[inner] = true;
					String[] rowData = new String[resultColumns.length];
					for (int i = 0; i < resultColumns.length; i++)
					{
						if(thisRow.hasColumn(resultColumns[i]))
							rowData[i] = thisRow.getValue(resultColumns[i]);
						else if(thatRow.hasColumn(resultColumns[i]))
							rowData[i] = thatRow.getValue(resultColumns[i]);
						else
							throw new IllegalArgumentException("resultColumns contained a column not present in this or other: " + resultColumns[i]);
							
					}
					rows.add(new DataRow(colData, rowData));
				}
				inner++;
			}
			outer++;
		}
		
		switch(type)
		{
		case FULL:
			for (int i = 0; i < thatMatch.length; i++)
				if(!thatMatch[i])
				{
					String[] rowData = new String[resultColumns.length];
					for (int j = 0; j < resultColumns.length; j++)
					{
						if(other.getRows().get(i).hasColumn(resultColumns[j]))
							rowData[j] = other.getRows().get(i).getValue(resultColumns[j]);
						else
							rowData[j] = null;
							
					}
					rows.add(new DataRow(colData, rowData));
				}
			for (int i = 0; i < thisMatch.length; i++)
				if(!thisMatch[i])
				{
					String[] rowData = new String[resultColumns.length];
					for (int j = 0; j < resultColumns.length; j++)
					{
						if(this.getRows().get(i).hasColumn(resultColumns[j]))
							rowData[j] = this.getRows().get(i).getValue(resultColumns[j]);
						else
							rowData[j] = null;
							
					}
					rows.add(new DataRow(colData, rowData));
				}
			break;
		case LEFT:
			for (int i = 0; i < thisMatch.length; i++)
				if(!thisMatch[i])
				{
					String[] rowData = new String[resultColumns.length];
					for (int j = 0; j < resultColumns.length; j++)
					{
						if(this.getRows().get(i).hasColumn(resultColumns[j]))
							rowData[j] = this.getRows().get(i).getValue(resultColumns[j]);
						else
							rowData[j] = null;
							
					}
					rows.add(new DataRow(colData, rowData));
				}
			break;
		case RIGHT:
			for (int i = 0; i < thatMatch.length; i++)
				if(!thatMatch[i])
				{
					String[] rowData = new String[resultColumns.length];
					for (int j = 0; j < resultColumns.length; j++)
					{
						if(other.getRows().get(i).hasColumn(resultColumns[j]))
							rowData[j] = other.getRows().get(i).getValue(resultColumns[j]);
						else
							rowData[j] = null;
							
					}
					rows.add(new DataRow(colData, rowData));
				}
			break;
		default:
			break;
		
		}
					
		return new DataTable(tableName, colData, rows);
	}
	
	public DataTable union(boolean all, String tableName, DataColumn[] nativeColumns, DataColumn[] foreignColumns, DataTable toUnion)
	{
		if(nativeColumns.length != foreignColumns.length)
			throw new IllegalArgumentException("Unioning tables must have the same column count");
		
		DataColumn[] colData = new DataColumn[nativeColumns.length];
		for(int i = 0; i < colData.length; i++)
			colData[i] = nativeColumns[i].clone();
		List<DataRow> rows = new ArrayList<>();
		for (DataRow dataRow : this.getRows())
			rows.add(new DataRow(colData, dataRow.rowData()));
		for (DataRow dataRow : toUnion.getRows())
			if(all)
				rows.add(new DataRow(colData, dataRow.rowData()));
			else
			{
				boolean toAdd = true;
				for (DataRow tmpRow : rows)
					if(!(toAdd &= tmpRow.isDistinct(colData, dataRow)))
						break;
				if(toAdd)
					rows.add(new DataRow(colData, dataRow.rowData()));
			}
		
		return new DataTable(tableName, colData, rows);
	}
	
	public DataTable group(DataColumn[] groupColumns, DataColumn[] aggregateColumns, BiFunction<String, String, String>[] aggregateFunctions)
	{
		for (DataColumn col : groupColumns)
			for (DataColumn agCol : aggregateColumns)
				if(col == agCol)
					throw new IllegalArgumentException("Cannot both group by and aggregate on " + col);
		
		DataColumn[] finalColumns = new DataColumn[groupColumns.length + aggregateColumns.length];
		for(int i = 0; i < finalColumns.length; i++)
			if(i < groupColumns.length)
				finalColumns[i] = groupColumns[i].clone();
			else
				finalColumns[i] = aggregateColumns[i - groupColumns.length].clone();
		List<DataRow> rows = new ArrayList<>();
		
		for (DataRow row : this.getRows())
		{
			DataRow lastRow = null;
			boolean isDistinct = true;
			for (DataRow testRow : rows)
				if(!(isDistinct &= row.isDistinct(groupColumns, lastRow = testRow)))
					break;
			
			if(isDistinct)
			{
				String[] data = new String[finalColumns.length];
				for (int i = 0; i < finalColumns.length; i++)
					if(i < groupColumns.length)
						data[i] = row.getValue(groupColumns[i]);
					else 
						data[i] = row.getValue(aggregateColumns[i - groupColumns.length]);
				rows.add(new DataRow(finalColumns, data));
			}else
				for (int i = 0; i < aggregateColumns.length; i++)
					lastRow.setValue(finalColumns[groupColumns.length + i], aggregateFunctions[i].apply(lastRow.getValue(finalColumns[groupColumns.length + i]), row.getValue(aggregateColumns[i])));
		}
		
		return new DataTable(this.getName(), finalColumns, rows);
	}

	public String generateCSV(char delimiter)
	{
		String toReturn = "";
		for (DataColumn dataColumn : this.getColumns())
			toReturn += dataColumn.getName() + delimiter;
		toReturn = toReturn.substring(0, toReturn.length() - 1) + "\r\n";
		for (DataRow row : this.getRows())
		{
			String[] data = row.rowData();
			for (String dat : data) {
				toReturn += dat + delimiter;
			}
			toReturn = toReturn.substring(0, toReturn.length() - 1) + "\r\n";
		}
		
		return toReturn;
	}
	
	public void generateCSV(char delimiter, OutputStream toWrite) throws IOException
	{
		for (int i = 0; i < this.getColumns().length; i++)
			toWrite.write((this.getColumns()[i].getName() + (i+1 != this.getColumns().length ? delimiter : "")).getBytes());
		toWrite.write("\r\n".getBytes());
		for (DataRow row : this.getRows())
		{
			String[] data = row.rowData();
			for (int i = 0; i < data.length; i++)
				toWrite.write((data[i] + (i+1 != data.length ? delimiter : "")).getBytes());
			toWrite.write("\r\n".getBytes());
		}
	}
	
	public String toString() {
		return "DataTable [tableName=" + tableName + "]";
	}
	
	public static DataTable load(IRowDataProvider prov)
	{
		return new DataTable(prov.getName(), prov.getColumns(), prov.getRows());
	}
	
	public static DataTable load(IRowStream stream)
	{
		if(stream.isRowCountSupported())
			return load(stream, stream.getRowCount());
		
		List<DataRow> rows = new ArrayList<>();
		DataRow loaded;
		while(stream.hasNextRow() && (loaded = stream.readRow()) != null)
			rows.add(loaded);
		
		return new DataTable(stream.getName(), stream.getColumns(), rows);
	}
	
	public static DataTable load(IRowStream stream, int rowCount)
	{
		if(stream.isRowCountSupported() && stream.getRowCount() < rowCount)
			rowCount = stream.getRowCount();
		
		List<DataRow> rows = new ArrayList<>();
		for (int i = 0; i < rowCount && stream.hasNextRow(); i++)
			rows.add(stream.readRow());
		
		return new DataTable(stream.getName(), stream.getColumns(), rows);
	}
	
	private static class DataTableRowDataProvider implements IRowDataProvider
	{
		private DataTable tbl;
		private int readPtr = 0;
		
		public DataTableRowDataProvider(DataTable toProvide)
		{
			this.tbl = toProvide;
		}
		
		public String getName() {
			return tbl.getName();
		}

		public DataColumn[] getColumns() {
			DataColumn[] toReturn = new DataColumn[tbl.getColumns().length];
			for (int i = 0; i < toReturn.length; i++)
				toReturn[i] = tbl.getColumns()[i].clone();
			return toReturn;
		}

		public List<DataRow> getRows()
		{
			return tbl.getRows();
		}

		public int getRowCount()
		{
			return tbl.getRows().size();
		}

		public DataRow readRow()
		{
			return tbl.getRows().get(readPtr++);
		}

		public boolean isRowCountSupported()
		{
			return true;
		}

		public boolean hasNextRow()
		{
			return readPtr < getRowCount();
		}
	}
	
	public static enum JoinType
	{
		INNER,
		LEFT,
		RIGHT,
		FULL
	}
}
