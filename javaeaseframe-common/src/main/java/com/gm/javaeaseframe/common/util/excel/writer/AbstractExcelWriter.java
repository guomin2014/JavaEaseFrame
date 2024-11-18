package com.gm.javaeaseframe.common.util.excel.writer;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gm.javaeaseframe.common.util.FileUtil;
import com.gm.javaeaseframe.common.util.excel.IDataWriterCallback;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderTemplateMeta;
import com.gm.javaeaseframe.common.util.excel.common.ExcelWriterMeta;
import com.gm.javaeaseframe.common.util.excel.common.RowTemplate;
import com.gm.javaeaseframe.common.util.excel.common.RowTypeEnum;
import com.gm.javaeaseframe.common.util.excel.common.WorkbookTemplate;
import com.gm.javaeaseframe.common.util.excel.common.Worksheet;
import com.gm.javaeaseframe.common.util.excel.common.WorksheetTemplate;
import com.gm.javaeaseframe.common.util.excel.reader.ExcelReaderFactory;
import com.gm.javaeaseframe.common.util.excel.reader.IExcelReader;

public abstract class AbstractExcelWriter implements IExcelWriter
{
	protected Log logger = LogFactory.getLog(this.getClass());
	
	protected static final String OPER_PREFIX = "数据导出";
	
	protected static final String OPER_TEMPLATE_PREFIX = "模板解析";
	/** 手动结束的编码 */
	protected final String OPER_MANUAL_END_CODE = "300001";
	
	/** 是否打印日志，true：打印，默认false */
	protected boolean debug = false;
	
	protected IDataWriterCallback dataCallback;
	
	protected ExcelWriterMeta meta;
	
	/** 是否使用模板 */
	protected boolean useTemplate = false;
	/** 是否使用默认模板 */
	protected boolean useDefaultTemplate = false;
	/** 是否有标题栏 */
	protected boolean templateIncloudTitleRow = false;
	/** 是否有列标题 */
	protected boolean templateIncloudColumnRow = false;
	/** 工作薄的模板 */
	protected WorkbookTemplate workbookTemplate;
	
	protected Worksheet worksheetTemplate;
	
	/** 列数 */
	protected int columnNum = 0;
	/** 当前sheet的行标 */
	protected int rowIndex = 0;
	/** 当前sheet的行数 */
	protected int currRowCount = 0;
	/** 总行数 */
	protected int totalRowCount = 0;
	/** 当前sheet的名称 */
	private String sheetName = "";
	/** 当前sheet的序号，从1开始 */
	private int sheetIndex = 0;
	
	public AbstractExcelWriter(boolean printLog, IDataWriterCallback dataCallback, ExcelWriterMeta meta)
	{
		this.debug = printLog;
		this.dataCallback = dataCallback;
		this.meta = meta;
		if(meta == null)
		{
			throw new RuntimeException("没有存储文件的描述信息");
		}
		this.templateIncloudTitleRow = meta.getTemplateIncloudTitle();
		this.templateIncloudColumnRow = meta.getTemplateIncloudColumn();
		if(StringUtils.isNotEmpty(meta.getTitle()))
		{
			this.templateIncloudTitleRow = true;
		}
		if(meta.getColumnName() != null && meta.getColumnName().length > 0)
		{
			this.templateIncloudColumnRow = true;
		}
		if(StringUtils.isEmpty(meta.getCreateUser()))
		{
			this.meta.setCreateUser("");
		}
		if(StringUtils.isEmpty(meta.getSheetName()))
		{
			this.meta.setSheetName("sheet");
		}
		this.useTemplate = true;//所有写文件均使用模板文件
		if(StringUtils.isNotEmpty(meta.getTemplateFilePath()))
		{
			String tempExt = FileUtil.getExtend(meta.getTemplateFilePath());
			String ext = FileUtil.getExtend(meta.getFilePath());
			File file = new File(meta.getTemplateFilePath());
			if(!file.exists() || !file.isFile() || file.length() > 2097152 || StringUtils.isEmpty(ext) || !ext.equalsIgnoreCase(tempExt))//文件小于2M
			{
				//判断模板文件文件类型与输出文件类型是否一致
				this.useDefaultTemplate = true;
			}
			else
			{
				this.useDefaultTemplate = false;
			}
		}
		else
		{
			this.useDefaultTemplate = true;
		}
		if(this.useDefaultTemplate && debug)
		{
			logger.info(String.format("【%s】模板文件不存在或文件格式不正确或文件大于2M导致不能使用，将使用默认模板！", OPER_TEMPLATE_PREFIX));
		}
		this.initTemplate(useDefaultTemplate ? meta.getFilePath() : meta.getTemplateFilePath());
	}
	
	private WorkbookTemplate processTemplate(String templateFilePath) throws Exception
	{
		ExcelReaderTemplateMeta readerMeta = new ExcelReaderTemplateMeta();
		readerMeta.setFilePath(templateFilePath);
		readerMeta.setIncloudTitleRow(templateIncloudTitleRow);
		readerMeta.setIncloudColumnRow(templateIncloudColumnRow);
		readerMeta.setSheetIndex(1);//只解析第一个sheet
		readerMeta.setTitle(meta.getTitle());
		readerMeta.setColumnName(meta.getColumnName());
		readerMeta.setUseDefaultTemplate(useDefaultTemplate);
		IExcelReader reader = ExcelReaderFactory.createReader(OPER_TEMPLATE_PREFIX, debug, null, readerMeta);
		if(reader != null)
		{
			return reader.processTemplate();
		}
		return null;
	}
	
	public void initTemplate(String templateFilePath)
	{
		try
		{
			workbookTemplate = this.processTemplate(templateFilePath);
			if(this.workbookTemplate == null)
			{
				this.useTemplate = false;
				this.templateIncloudTitleRow = false;
				this.templateIncloudColumnRow = false;
			}
			else
			{
				if(useDefaultTemplate)
				{
					meta.setTemplateFilePath(workbookTemplate.getTemplateFilePath());
				}
				if(workbookTemplate.getSheetCount() > 0)
				{
					worksheetTemplate = workbookTemplate.getSheetList().get(0);
				}
				else
				{
					throw new Exception("不能获取到模板的sheet样式");
				}
				if(worksheetTemplate != null && StringUtils.isNotEmpty(worksheetTemplate.getSheetName()))
				{
					if(StringUtils.isEmpty(meta.getSheetName()))
					{
						meta.setSheetName(worksheetTemplate.getSheetName());
					}
				}
				if(debug)
				{
					boolean titleRow = false;
					boolean columnRow = false;
					boolean dataRow = false;
					int columnCount = 0;
					if(worksheetTemplate.getDataTemplate() != null)
					{
						dataRow = true;
						columnCount = worksheetTemplate.getDataTemplate().getColumnCount();
					}
					if(worksheetTemplate.getColumnTemplate() != null)
					{
						columnRow = true;
						if(columnCount == 0)
						{
							columnCount = worksheetTemplate.getColumnTemplate().getColumnCount();
						}
					}
					if(worksheetTemplate.getTitleTemplate() != null)
					{
						titleRow = true;
						if(columnCount == 0)
						{
							columnCount = worksheetTemplate.getTitleTemplate().getColumnCount();
						}
					}
					logger.info(String.format("【%s】模板文件解析结果-->主题行：%s，栏标题行：%s，数据行：%s，列数：%s", OPER_TEMPLATE_PREFIX, titleRow, columnRow, dataRow, columnCount));
				}
			}
		}
		catch(Exception e)
		{
			this.workbookTemplate = null;
			this.useTemplate = false;
			this.templateIncloudTitleRow = false;
			this.templateIncloudColumnRow = false;
			if(debug)
			{
				logger.error(String.format("【%s】初始化模板文件失败-->%s，原因：%s", OPER_TEMPLATE_PREFIX, meta.getTemplateFilePath(), e.getMessage()));
			}
		}
	}
	
	@Override
	public void process() throws Exception
	{
		long startTime = System.currentTimeMillis();
        int rowCount = 0;
        String filePath = meta.getFilePath();
		// excel单表最大行数是65535
		int maxRowNum = meta.getMaxRowPreSheet();
		String title = meta.getTitle();
		String sheetNamePrefix = meta.getSheetName();
		String[] columnNames = meta.getColumnName();
        List<String> columns = new ArrayList<String>();
		if(columnNames != null && columnNames.length > 0)
		{
			columns.addAll(Arrays.asList(columnNames));
			columnNum = columnNames.length;
		}
		sheetName = sheetNamePrefix;
		if(debug)
		{
			logger.info(String.format("【%s】开始数据写入到Excel-->%s", OPER_PREFIX, filePath));
		}
		File file = new File(filePath);
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
		}
		RowTemplate titleTemplate = this.worksheetTemplate != null ? this.worksheetTemplate.getTitleTemplate() : null;
		RowTemplate columnTemplate = this.worksheetTemplate != null ? this.worksheetTemplate.getColumnTemplate() : null;
		RowTemplate dataTemplate = this.worksheetTemplate != null ? this.worksheetTemplate.getDataTemplate() : null;
		if(columnNum > 0 && titleTemplate != null)
		{
			titleTemplate.setColumnCount(columnNum);
		}
        Writer writer = null;
        try
        {
        	rowIndex = 0;
        	sheetIndex = 1;
        	boolean needReWriter = false;
        	writer = this.beginWorkbook(writer, filePath, workbookTemplate);
        	//begin sheet
        	writer = this.beginWorksheet(writer, sheetName, worksheetTemplate);
        	//插入标题行
            this.beginTitleRow(writer, title, columns, titleTemplate, columnTemplate);
            //插入数据行
            List<String[]> dataList = null;
            int index = 1;
            long startRowTime = System.currentTimeMillis();
			while((dataList = dataCallback.doCallback(index)) != null)
			{
				if(dataList.isEmpty())
				{
					break;
				}
				if(columnNum == 0)
				{
					for(String[] datas : dataList)
					{
						if(datas != null && datas.length > 0)
						{
							columnNum = datas.length;
							break;
						}
					}
				}
				if(columnNum == 0)
				{
					continue;
				}
				if(debug)
				{
					logger.info(String.format("【%s】开始第%s次写入，每行%s列，本次共%s行", OPER_PREFIX, index, columnNum, dataList.size()));
				}
				rowCount = 0;
				for(String[] datas : dataList)
				{
					if(datas == null || datas.length == 0)
					{
						continue;
					}
					if(needReWriter)
					{
						needReWriter = false;
						sheetIndex++;
						rowIndex = 0;
						sheetName = sheetNamePrefix + (sheetIndex > 1 ? "_" + (sheetIndex) : "");
						writer = this.beginWorksheet(writer, sheetName, worksheetTemplate);
						this.beginTitleRow(writer, title, columns, titleTemplate, columnTemplate);
					}
		            insertRow(writer, ++rowIndex, datas, dataTemplate);
		            currRowCount++;
		            totalRowCount++;
		            rowCount++;
		            if(currRowCount >= maxRowNum && maxRowNum != -1)
		            {
		            	//结束该sheet，开启新的sheet
		            	this.endWorksheet(writer, worksheetTemplate);
		            	if(debug)
						{
							long time = System.currentTimeMillis() - startRowTime;
							logger.info(String.format("【%s】sheet【%s】写入%s行，当前Sheet共%s行，用时：%s毫秒", OPER_PREFIX, sheetName, rowCount, currRowCount, time));
							startRowTime = System.currentTimeMillis();
						}
				        currRowCount = 0;
				        rowCount = 0;
				        needReWriter = true;
		            }
				}
				if(rowCount > 0 && debug)
				{
					long time = System.currentTimeMillis() - startRowTime;
					logger.info(String.format("【%s】sheet【%s】写入%s行，当前Sheet共%s行，用时：%s毫秒", OPER_PREFIX, sheetName, rowCount, currRowCount, time));
					startRowTime = System.currentTimeMillis();
				}
				index++;
			}
			//结束sheet
			this.endWorksheet(writer, worksheetTemplate);
			//电子表格结束
			this.endWorkbook(writer, workbookTemplate);
        }
        finally
        {
        	if(writer != null)
        	{
        		try
        		{
        			writer.close();
        		}catch(Exception e){}
        	}
        	if(this.useDefaultTemplate)
        	{
        		try
        		{
        			new File(meta.getTemplateFilePath()).delete();
        		}catch(Exception e){}
        	}
        }
//        if(debug)
//		{
			long time = System.currentTimeMillis() - startTime;
			logger.info(String.format("【%s】数据写入到Excel完成，生成%s个sheet工作薄，每行%s列，共%s行记录，用时：%s毫秒-->%s", OPER_PREFIX, sheetIndex, columnNum, totalRowCount, 
					time, filePath));
//		}
	}
	
	public void beginTitleRow(Writer writer, String title, List<String> columns, RowTemplate titleTemplate, RowTemplate columnTemplate) throws Exception
	{
		if(StringUtils.isNotEmpty(title) || (this.useTemplate && this.templateIncloudTitleRow))
        {
			if(StringUtils.isNotEmpty(title) && titleTemplate == null)
			{
				titleTemplate = new RowTemplate();
				titleTemplate.setRowType(RowTypeEnum.TITLE.getCode());
				titleTemplate.setColumnCount(columns != null ? columns.size() : 0);
			}
            insertRow(writer, ++rowIndex, StringUtils.isNotEmpty(title) ? new String[]{title} : new String[0], titleTemplate);
        }
        if(!columns.isEmpty() || (this.useTemplate && this.templateIncloudColumnRow))
        {
            insertRow(writer, ++rowIndex, columns.toArray(new String[columns.size()]), columnTemplate);
        }
	}
	
	public abstract Writer beginWorkbook(Writer writer, String filePath, WorkbookTemplate workbookTemplate) throws Exception;
	
	public abstract void endWorkbook(Writer writer, WorkbookTemplate workbookTemplate) throws Exception;
	
	public abstract Writer beginWorksheet(Writer writer, String sheetName, WorksheetTemplate worksheetTemplate) throws Exception;
	
	public abstract void endWorksheet(Writer writer, WorksheetTemplate worksheetTemplate) throws Exception;
	/**
	 * 插入行
	 * @param writer
	 * @param rowNum
	 * @param columns		行数据（如果是标题行OR栏标题行，则该值优先于RowTemplate中的值）
	 * @param rowTemplate
	 * @throws Exception
	 */
	public abstract void insertRow(Writer writer, int rowNum, String[] columns, RowTemplate rowTemplate) throws Exception;

}
