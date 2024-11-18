package com.gm.javaeaseframe.common.util.excel.common;

import java.util.List;

public class Worksheet extends WorksheetTemplate
{
	/** sheet的序号 */
	private int sheetIndex;
	/** sheet名称 */
	private String sheetName;
	/** 列数 */
	private int columnCount;
	/** 主标题--跨栏数（默认=columnCount-1） */
	private int acrossCount;
	/** 标题栏行数 */
	private int titleRowCount = 0;
	/** 行数 */
	private int rowCount;
	/** 主标题--跨栏 */
	private String title;
	/** 每列的标题 */
	private List<String> columns;
	/** 数据列表 */
	private List<Row> rows;
	
	public Worksheet(){}
	
	public Worksheet(int sheetIndex, String sheetName, int rowCount, int columnCount){
		this.sheetIndex = sheetIndex;
		this.sheetName = sheetName;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}

	public int getSheetIndex()
	{
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex)
	{
		this.sheetIndex = sheetIndex;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
		this.rowCount = this.titleRowCount + (rows == null ? 0 : rows.size());
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
		this.acrossCount = columnCount - 1;
	}

	public int getAcrossCount()
	{
		return acrossCount;
	}

	public void setAcrossCount(int acrossCount)
	{
		this.acrossCount = acrossCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
		if(title != null && title.trim().length() > 0)
		{
			this.titleRowCount++;
		}
	}

	public List<String> getColumns()
	{
		return columns;
	}

	public void setColumns(List<String> columns)
	{
		this.columns = columns;
		if(columns != null && columns.size() > 0)
		{
			this.titleRowCount++;
		}
	}

	public int getTitleRowCount()
	{
		return titleRowCount;
	}
	
}
