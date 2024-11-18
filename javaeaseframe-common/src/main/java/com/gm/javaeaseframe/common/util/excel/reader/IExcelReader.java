package com.gm.javaeaseframe.common.util.excel.reader;


import com.gm.javaeaseframe.common.util.excel.common.Workbook;
import com.gm.javaeaseframe.common.util.excel.common.WorkbookTemplate;

public interface IExcelReader
{
	public Workbook process() throws Exception;
	
	public WorkbookTemplate processTemplate() throws Exception;
}
