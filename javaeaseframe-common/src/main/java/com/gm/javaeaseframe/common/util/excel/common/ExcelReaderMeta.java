package com.gm.javaeaseframe.common.util.excel.common;

import java.io.InputStream;

public class ExcelReaderMeta
{
	/** 文件路径 */
	private String filePath;
	/** 文件流，与文件路径二选一 */
	private InputStream input;
	/** 包含主标题行 */
	private boolean incloudTitleRow = false;
	/** 包含栏标题行 */
	private boolean incloudColumnRow = false;
	/** sheet的序号，-1：表示全部，指定从1开始 */
	private int sheetIndex = -1;
	/** 每个sheet最大读取多少行，-1：表示全部 */
	private int maxRowPreSheet = -1;
	/** 每次最大读取多少行 */
	private int maxRowForEveryTime = 1000;
	
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	public InputStream getInput() {
		return input;
	}
	public void setInput(InputStream input) {
		this.input = input;
	}
	public int getSheetIndex()
	{
		return sheetIndex;
	}
	public void setSheetIndex(int sheetIndex)
	{
		this.sheetIndex = sheetIndex;
	}
	public int getMaxRowPreSheet()
	{
		return maxRowPreSheet;
	}
	public void setMaxRowPreSheet(int maxRowPreSheet)
	{
		this.maxRowPreSheet = maxRowPreSheet;
	}
	public int getMaxRowForEveryTime()
	{
		return maxRowForEveryTime;
	}
	public void setMaxRowForEveryTime(int maxRowForEveryTime)
	{
		this.maxRowForEveryTime = maxRowForEveryTime;
	}
	public boolean getIncloudTitleRow()
	{
		return incloudTitleRow;
	}
	public void setIncloudTitleRow(boolean incloudTitleRow)
	{
		this.incloudTitleRow = incloudTitleRow;
	}
	public boolean getIncloudColumnRow()
	{
		return incloudColumnRow;
	}
	public void setIncloudColumnRow(boolean incloudColumnRow)
	{
		this.incloudColumnRow = incloudColumnRow;
	}
	
}
