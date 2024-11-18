package com.gm.javaeaseframe.common.util.excel.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.xml.sax.Attributes;

import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.CodeEnum;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.RowTypeEnum;
import com.gm.javaeaseframe.common.util.excel.common.Worksheet;
import com.gm.javaeaseframe.common.util.excel.common.WorksheetData;

public class ReaderHandler
{
	
	protected Log logger;
	
	protected String OPER_PREFIX = "数据导入";
	
	/** 是否打印日志，true：打印，默认false */
	protected boolean debug = false;
	
	protected IDataReaderCallback dataCallback;
	
	protected ExcelReaderMeta meta;
	
	/** 每个sheet最大读取多少行，-1：表示全部 */
	protected int maxRowPreSheet = -1;
	/** 每次最大读取多少行 */
	protected int maxRowForEveryTime = 1000;
	/** 标题栏的行数 */
	protected int titleRowCount = 0;
	/** 包含主标题行 */
	protected boolean incloudTitleRow = false;
	/** 包含栏标题行 */
	protected boolean incloudColumnRow = false;
	/** 当前sheet的序号 */
	protected int sheetIndex = 0;
	/** 当前sheet的名称 */
	protected String sheetName;
	/** 已读取所有sheet的总行数 */
	protected int totalRowCount = 0;
	/** 需要解析的Sheet的序号，-1：表示全部 */
	protected int sheetIndexEnable = -1;
	/** 需要过滤的Sheet的序号，-1：表示不过滤 */
	protected int sheetIndexFilter = -1;
	/** 跳过sheet解析 */
	protected boolean skipSheet = false;
	/** 结束sheet解析 */
	protected boolean endSheet = false;
	/** 限制读取sheet */
	protected boolean limitSheet = false;
	/** 是否开始解析sheet */
	protected boolean isSheetBegin = false;
	/** sheet开始解析时间 */
	protected long sheetStartTime = 0;
	/** 标题 */
	protected String title;
	/** 列栏标题 */
	protected String[] columns;
	/** 当前sheet的行标 */
	protected int rowIndex = 0;
	/** 当前sheet的行数 */
	protected int rowCount = 0;
	/** 已读取数据 */
	protected List<String[]> dataList = new ArrayList<>();
	
	public ReaderHandler(boolean printLog, Log logger, ExcelReaderMeta meta)
	{
		this(null, printLog, logger, meta);
	}
	public ReaderHandler(String prefix, boolean printLog, Log logger, ExcelReaderMeta meta)
	{
		this(prefix, printLog, logger, null, meta);
	}
	public ReaderHandler(boolean printLog, Log logger, IDataReaderCallback dataCallback, ExcelReaderMeta meta)
	{
		this(null, printLog, logger, dataCallback, meta);
	}
	public ReaderHandler(String prefix, boolean printLog, Log logger, IDataReaderCallback dataCallback, ExcelReaderMeta meta)
	{
		this.debug = printLog;
		this.logger = logger;
		this.dataCallback = dataCallback;
		this.meta = meta;
		this.incloudTitleRow = meta.getIncloudTitleRow();
		this.incloudColumnRow = meta.getIncloudColumnRow();
		this.maxRowPreSheet = meta.getMaxRowPreSheet();
		this.sheetIndexEnable = meta.getSheetIndex();
		this.maxRowForEveryTime = meta.getMaxRowForEveryTime();
		if(prefix != null)
		{
			this.OPER_PREFIX = prefix;
		}
		if(this.logger == null)
		{
			this.logger = LogFactory.getLog(this.getClass());
		}
		if(this.sheetIndexEnable != -1)
		{
			limitSheet = true;
		}
		else
		{
			limitSheet = false;
		}
	}
	protected int beginWorksheet(int sheetIndex, String sheetName)
	{
		this.sheetIndex = sheetIndex;
		this.sheetName = sheetName;
		this.rowCount = 0;
		this.rowIndex = 0;
		this.isSheetBegin = true;
		this.endSheet = false;
		if (debug)
		{
			sheetStartTime = System.currentTimeMillis();
			logger.info(String.format("【%s】开始解析第%s个sheet工作薄【%s】", OPER_PREFIX, sheetIndex, sheetName));
		}
		return this.sheetIndex;
	}
	/**
	 * 开始sheet解析
	 * @param sheetName
	 * @return
	 */
	protected int beginWorksheet(String sheetName)
	{
		int sheetIndex = this.sheetIndex + 1;
		return this.beginWorksheet(sheetIndex, sheetName);
	}
	/**
	 * 结束sheet解析
	 */
	protected Worksheet endWorksheet()
	{
		Worksheet sheet = null;
		if(this.isSheetBegin)
		{
			this.isSheetBegin = false;
			if(debug)
			{
				long time = System.currentTimeMillis() - sheetStartTime;
				if (sheetIndexEnable != -1 && sheetIndexEnable != sheetIndex)
				{
					logger.info(String.format("【%s】跳过解析第%s个sheet工作薄【%s】，不满足条件，用时：%s毫秒", OPER_PREFIX, sheetIndex, sheetName, time));
				}
				else
				{
					logger.info(String.format("【%s】完成解析第%s个sheet工作薄【%s】，共%s行记录，用时：%s毫秒", OPER_PREFIX, sheetIndex, sheetName, rowCount, time));
				}
			}
			if (sheetIndexEnable == -1 || sheetIndexEnable == sheetIndex)
			{
				sheet = new Worksheet(sheetIndex, sheetName, rowCount, columns == null ? 0 : columns.length);
				if(columns != null)
				{
					sheet.setColumns(Arrays.asList(columns));
				}
				sheet.setTitle(this.title);
			}
			this.dataCall(true);//将本sheet未回调的数据进行回调
		}
		return sheet;
	}
	/**
	 * 是否结束workbook的解析
	 * @return
	 */
	protected boolean isEndWorkbook()
	{
		return limitSheet && sheetIndex >= this.sheetIndexEnable;
	}
	
	
	protected RowTypeEnum getRowType(int rowIndex)
	{
		RowTypeEnum rowType = RowTypeEnum.DATA;
    	//行标从1开始
		if(rowIndex <= 2)
		{
	    	if(this.incloudTitleRow || this.incloudColumnRow)
			{
				if(rowIndex == 1 && this.incloudTitleRow)
				{
					rowType = RowTypeEnum.TITLE;
				}
				else if((rowIndex == 1 && !this.incloudTitleRow && this.incloudColumnRow) || (rowIndex == 2 && this.incloudTitleRow && this.incloudColumnRow))
				{
					rowType = RowTypeEnum.COLUMN;
				}
			}
		}
    	return rowType;
	}
	protected void addRowDataWithException(int rowIndex, String[] datas)
	{
		CodeEnum code = this.addRowData(rowIndex, datas);
		switch(code)
		{
			case FAILURE:
				throw new RuntimeException(code.getCode());
			case SUCCESS_END_SHEET:
				this.skipCurrSheet();
			default:
				break;
		}
	}
	/**
	 * 回调解析出的行数据
	 * @param rowIndex
	 * @param datas
	 */
	protected CodeEnum addRowData(int rowIndex, String[] datas)
	{
		this.rowIndex = rowIndex;
		if(datas == null || datas.length == 0)
		{
			return CodeEnum.SUCCESS;
		}
		boolean needCall = true;
		RowTypeEnum rowType = this.getRowType(rowIndex);
		switch(rowType)
		{
			case TITLE:
				this.title = datas[0];
				needCall = false;
				break;
			case COLUMN:
				this.columns = datas;
				needCall = false;
				break;
			default:
				needCall = true;
				break;
		}
		if (needCall)
		{
			rowCount++;
			totalRowCount++;
			boolean endSheet = false;
			if(maxRowPreSheet != -1 && rowCount >= maxRowPreSheet)
			{
				endSheet = true;
			}
			dataList.add(datas);
			boolean notifySuccess = this.dataCall(endSheet);
			if(!notifySuccess)
			{
				return CodeEnum.FAILURE;
			}
			if(endSheet)
			{
				if(debug)
				{
					logger.info(String.format("【%s】解析第%s个sheet工作薄【%s】且已读取%s行数据，将结束解析", OPER_PREFIX, sheetIndex, sheetName, rowCount));
				}
				return CodeEnum.SUCCESS_END_SHEET;
			}
		}
		return CodeEnum.SUCCESS;
	}
	
	/**
	 * 数据回调
	 * @param forceCall	是否强制回调
	 * @return
	 */
	protected boolean dataCall(boolean forceCall)
	{
		boolean notifySuccess = false;
		if(dataList.size() == 0)
		{
			return true;
		}
		if(dataCallback == null)//不需要回调
		{
			if(debug)
			{
				logger.info(String.format("【%s】未设置回调对象，不需要数据回调通知", OPER_PREFIX));
			}
			dataList.clear();
			return true;
		}
		if(!forceCall && dataList.size() < maxRowForEveryTime)
		{
			return true;
		}
//		if(debug)
//		{
//			logger.info(String.format("【%s】第%s个sheet工作薄【%s】数据回调，本次共%s行", OPER_PREFIX, sheetIndex, sheetName, dataList.size()));
//		}
		WorksheetData resultVO = new WorksheetData(sheetIndex, sheetName, rowIndex, rowCount, totalRowCount);
		resultVO.setTitle(title);
		resultVO.setColumns(columns);
		List<String[]> dataListTmp = new ArrayList<>();
		dataListTmp.addAll(dataList);
		resultVO.setDataList(dataListTmp);
		dataList.clear();
		try
		{
			// 每行结束时， 调用getRows() 方法
			notifySuccess = dataCallback.doCallback(resultVO);
			if(!notifySuccess)
			{
				if(debug)
				{
					logger.info(String.format("【%s】解析第%s个sheet工作薄【%s】的第%s行数据时，回调异常，将结束解析", OPER_PREFIX, sheetIndex, sheetName, rowIndex));
				}
			}
		}
		catch(Throwable e)
		{
			notifySuccess = false;
			if(debug)
			{
				logger.info(String.format("【%s】解析第%s个sheet工作薄【%s】的第%s行数据时，回调异常，将结束解析", OPER_PREFIX, sheetIndex, sheetName, rowIndex), e);
			}
		}
		return notifySuccess;
	}
	
	protected Map<String, String> convertMap(List<Attribute> attrList)
	{
		return this.convertMap(attrList, null);
	}
	
	protected Map<String, String> convertMap(List<Attribute> attrList, Set<String> filters)
	{
		Map<String, String> attrs = new LinkedHashMap<>();
		if(attrList != null)
		{
			for(Attribute attr : attrList)
			{
				String attrName = attr.getQualifiedName();
	        	String attrText = StringUtils.trim(attr.getText());
	        	if(StringUtils.isEmpty(attrName))
	        	{
	        		continue;
	        	}
	        	if(filters == null || (filters != null && !filters.contains(attrName)))
				{
					attrs.put(attrName, attrText);
				}
			}
		}
		return attrs;
	}
	
	protected String convertXML(String name, Attributes attributes)
	{
		return this.convertXML(name, attributes, true);
	}
	
	protected String convertXML(String name, Attributes attributes, boolean isEnd)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("<").append(name);
		int len = attributes == null ? 0 : attributes.getLength();
		int index = 0;
		while(index < len)
		{
			String attrQName = attributes.getQName(index);
			String text = StringUtils.trim(attributes.getValue(index));
			if(StringUtils.isNotEmpty(attrQName))
			{
				ret.append(" ").append(attrQName).append("=\"").append(text).append("\"");
			}
			index++;
		}
		ret.append(isEnd ? " />" : ">");
		return ret.toString();
	}
	/**
	 * 是否跳过当前sheet的解析
	 * @return
	 */
	public boolean isSkipSheet()
	{
		return (sheetIndexEnable != -1 && sheetIndexEnable != sheetIndex) || sheetIndex == sheetIndexFilter;
	}
	
	public void skipSheet(int sheetIndex)
	{
		this.sheetIndexFilter = sheetIndex;
	}
	
	public void skipCurrSheet()
	{
		this.sheetIndexFilter = this.sheetIndex;
	}
	
	public int getSheetIndex()
	{
		return sheetIndex;
	}
	
}
