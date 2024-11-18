package com.gm.javaeaseframe.common.util.excel.reader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderTemplateMeta;
import com.gm.javaeaseframe.common.util.excel.common.Workbook;
import com.gm.javaeaseframe.common.util.excel.common.WorkbookTemplate;
import com.gm.javaeaseframe.common.util.excel.handler.ReaderHandler;

public abstract class AbstractExcelReader extends ReaderHandler implements IExcelReader
{
    protected static Log logger = LogFactory.getLog(IExcelReader.class);
	
	/** 是否获取模板信息 */
	protected boolean useTemplate = false;
	/** 是否使用默认模板 */
	protected boolean useDefaultTemplate = false;
	
	public AbstractExcelReader(String prefix, boolean printLog, IDataReaderCallback dataCallback, ExcelReaderMeta meta)
	{
		super(prefix, printLog, logger, dataCallback, meta);
	}
	
	@Override
	public Workbook process() throws Exception
	{
		if(meta == null || (StringUtils.isBlank(meta.getFilePath()) && meta.getInput() == null))
		{
			throw new Exception("导入的文件路径为空");
		}
		long startTime = System.currentTimeMillis();
		String filePath = meta.getFilePath();
		if (debug)
		{
			logger.info(String.format("【%s】开始解析excel文档-->%s", OPER_PREFIX, filePath));
		}
		Workbook result = this.processWorkbook();
		if (debug)
		{
			long time = System.currentTimeMillis() - startTime;
			int sheetCount = result == null ? 0 : result.getSheetCount();
			int totalRowCount = result == null ? 0 : result.getTotalRowCount();
			logger.info(String.format("【%s】完成解析Excel文档，共%s个sheet工作薄，总共%s行记录，用时：%s毫秒-->%s", OPER_PREFIX, sheetCount, totalRowCount, time, filePath));
		}
		return result;
	}
	
	public WorkbookTemplate processTemplate() throws Exception
	{
		this.useTemplate = true;
		if(meta instanceof ExcelReaderTemplateMeta)
		{
			ExcelReaderTemplateMeta tmeta = (ExcelReaderTemplateMeta)meta;
			useDefaultTemplate = tmeta.getUseDefaultTemplate();
		}
		if(useDefaultTemplate)
		{
			meta.setFilePath(this.createDefaultTemplate());
		}
		Workbook workbook = this.process();
		if(workbook != null)
		{
			workbook.setTemplateFilePath(meta.getFilePath());
		}
		return workbook;
	}
	/**
	 * 创建默认模板
	 * @return
	 * @throws Exception
	 */
	public String createDefaultTemplate() throws Exception
	{
		return null;
	}
	/**
	 * 解析workbook
	 * @return
	 * @throws Exception
	 */
	public abstract Workbook processWorkbook() throws Exception;

}
