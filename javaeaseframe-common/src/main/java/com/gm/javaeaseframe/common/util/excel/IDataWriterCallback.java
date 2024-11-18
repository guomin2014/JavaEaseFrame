package com.gm.javaeaseframe.common.util.excel;

import java.util.List;

public interface IDataWriterCallback
{
	/**
	 * 数据回调（循环读取待写入的数据）
	 * @param index	第几次读取数据，从1开始
	 * @return null OR size=0：则表示没有可读取的数据，将结束循环
	 */
	public List<String[]> doCallback(int index);  
}
