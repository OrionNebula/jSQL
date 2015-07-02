package net.lotrek.jSQL.data;

import java.util.HashMap;
import java.util.Map;

public class DataRow
{
	private DataColumn[] colOrder;
	private Map<DataColumn, String> rowData;
	
	public DataRow(DataColumn[] columns, String...data)
	{
		colOrder = columns;
		rowData = new HashMap<>();
		
		for(int i = 0; i < columns.length; i++)
			rowData.put(columns[i], data[i]);
	}
	
	public void setValue(int colId, String value)
	{
		setValue(colOrder[colId], value);
	}
	
	public void setValue(DataColumn col, String value)
	{
		rowData.put(col, value);
	}
	
	public String getValue(int colId)
	{
		return getValue(colOrder[colId]);
	}
	
	public String getValue(DataColumn col)
	{
		return rowData.get(col);
	}
	
	public boolean hasColumn(DataColumn col)
	{
		return rowData.keySet().contains(col);
	}
	
	public int getColId(DataColumn col)
	{
		for (int i = 0; i < colOrder.length; i++)
			if(colOrder[i] == col)
				return i;
		return -1;
	}
	
	public int getColId(String name)
	{
		for (int i = 0; i < colOrder.length; i++)
			if(colOrder[i].getName().equals(name))
				return i;
		return -1;
	}
	
	public String[] rowData()
	{
		String[] toReturn = new String[rowData.size()];
		int i = 0;
		for (DataColumn col : colOrder)
			toReturn[i++] = rowData.get(col);
		
		return toReturn;
	}
	
	public boolean isDistinct(DataColumn[] forComparison, DataRow toCompare)
	{
		for (int i = 0; i < forComparison.length; i++)
			if(!this.getValue(forComparison[i]).equals(toCompare.getValue(toCompare.getColId(forComparison[i].getName()))))
				return true;
		
		return false;
	}
	
	public String toString()
	{
		return rowData.toString();
	}
}