package com.gm.javaeaseframe.common.util.excel.common;

import java.util.List;

public class WorkbookTemplate extends Template
{
	/** 所有sheet的集合 */
	protected List<Worksheet> sheetList;
	/** 已读取所有sheet的总行数 */
	protected int totalRowCount;
	
	/** 模板文件路径 */
	private String templateFilePath;
	
	public List<Worksheet> getSheetList()
	{
		return sheetList;
	}

	public void setSheetList(List<Worksheet> sheetList)
	{
		this.sheetList = sheetList;
	}
	
	public int getSheetCount()
	{
		return this.sheetList == null ? 0 : this.sheetList.size();
	}

	public int getTotalRowCount()
	{
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount)
	{
		this.totalRowCount = totalRowCount;
	}

	public String getTemplateFilePath()
	{
		return templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath)
	{
		this.templateFilePath = templateFilePath;
	}
	
}
