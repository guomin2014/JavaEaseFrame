package com.gm.javaeaseframe.common.util.excel.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.apache.commons.lang3.StringUtils;

import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.CodeEnum;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.HandlerResult;
import com.gm.javaeaseframe.common.util.excel.common.Workbook;
import com.gm.javaeaseframe.common.util.excel.handler.AbstractContentHandler;
import com.gm.javaeaseframe.common.util.excel.handler.Excel2003ContentHandler;

/**
 * Excel2003读取器，按XML格式读取，采用事件驱动模式解析excel2003中的内容，遇到特定事件才会触发，大大减少了内存的使用。
 * 
 * @author GuoMin
 * @date 2017年7月14日
 */
public class Excel2003XMLReader extends AbstractExcelReader
{

	public Excel2003XMLReader(String prefix, boolean printLog, IDataReaderCallback rowReader, ExcelReaderMeta meta)
	{
		super(prefix, printLog, rowReader, meta);
	}

	/**
	 * 遍历excel下所有的sheet
	 * 
	 * @throws Exception
	 */
	public Workbook processWorkbook() throws Exception
	{
		String filePath = meta.getFilePath();
		BufferedInputStream bis = null;
		try
		{
			if (StringUtils.isNotBlank(meta.getFilePath())) {
				bis = new BufferedInputStream(new FileInputStream(filePath));
			} else if (meta.getInput() != null) {
				bis = new BufferedInputStream(meta.getInput());
			}
			AbstractContentHandler<Workbook> handler = new Excel2003ContentHandler<>(super.OPER_PREFIX, debug, logger, dataCallback, meta);
			HandlerResult<Workbook> result = handler.processSheet(meta.getSheetIndex(), "", bis, super.useTemplate);
			if(result != null)
			{
				CodeEnum code = CodeEnum.findByValue(result.getCode());
				if(code == CodeEnum.SUCCESS || code == CodeEnum.SUCCESS_END_SHEET || code == CodeEnum.SUCCESS_END_WORKBOOK)
				{
					return result.getData();
				}
			}
		}
		finally
		{
			if(bis != null)
			{
				try
				{
					bis.close();
				}catch(Exception e){}
			}
		}
		return null;
	}

}
