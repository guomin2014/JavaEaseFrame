package com.gm.javaeaseframe.common.util.excel.common;

import java.util.List;

public class WorksheetData
{
	/** sheet的序号 */
	private int sheetIndex;
	/** sheet的名称 */
	private String sheetName;
	/** 当前sheet中的行标，即sheet中第几行的数据 */
	private int currRowIndex;
	/** 当前sheet中的行数，因为空行，所以：<=currRowIndex */
	private int currRowCount;
	/** 已读取所有sheet的总行数 */
	private int totalRowCount;
	/** 标题 */
	private String title;
	/** 每栏的标题 */
	private String[] columns;
	/** 数据列表 */
	private List<String[]> dataList;
	
	public WorksheetData(){}
	
	public WorksheetData(int sheetIndex, String sheetName, int currRowIndex, int currRowCount, int totalRowCount)
	{
		this.sheetIndex = sheetIndex;
		this.sheetName = sheetName;
		this.currRowIndex = currRowIndex;
		this.currRowCount = currRowCount;
		this.totalRowCount = totalRowCount;
	}
	
	public int getSheetIndex()
	{
		return sheetIndex;
	}
	public void setSheetIndex(int sheetIndex)
	{
		this.sheetIndex = sheetIndex;
	}
	public String getSheetName()
	{
		return sheetName;
	}
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}
	public int getCurrRowIndex()
	{
		return currRowIndex;
	}
	public void setCurrRowIndex(int currRowIndex)
	{
		this.currRowIndex = currRowIndex;
	}
	public int getCurrRowCount()
	{
		return currRowCount;
	}
	public void setCurrRowCount(int currRowCount)
	{
		this.currRowCount = currRowCount;
	}
	public int getTotalRowCount()
	{
		return totalRowCount;
	}
	public void setTotalRowCount(int totalRowCount)
	{
		this.totalRowCount = totalRowCount;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String[] getColumns()
	{
		return columns;
	}
	public void setColumns(String[] columns)
	{
		this.columns = columns;
	}
	public List<String[]> getDataList()
	{
		return dataList;
	}
	public void setDataList(List<String[]> dataList)
	{
		this.dataList = dataList;
	}
	
}
