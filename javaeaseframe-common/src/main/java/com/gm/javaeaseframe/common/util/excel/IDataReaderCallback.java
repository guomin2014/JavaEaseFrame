package com.gm.javaeaseframe.common.util.excel;

import com.gm.javaeaseframe.common.util.excel.common.WorksheetData;

public interface IDataReaderCallback
{
	/**
	 * 数据回调（将读取的数据通过该方法进行回调通知）
	 * @param result
	 * @return
	 */
    public boolean doCallback(WorksheetData result);  
}
