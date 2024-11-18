package com.gm.javaeaseframe.common.util.excel.common;

import java.util.List;

public class Workbook extends WorkbookTemplate
{
	
	public Workbook(){}
	
	public Workbook(List<Worksheet> sheetList, int totalRowCount)
	{
		super.sheetList = sheetList;
		super.totalRowCount = totalRowCount;
	}
	
}
