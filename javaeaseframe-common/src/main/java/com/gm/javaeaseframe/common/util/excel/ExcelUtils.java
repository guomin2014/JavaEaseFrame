package com.gm.javaeaseframe.common.util.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.gm.javaeaseframe.common.util.FileUtil;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.ExcelWriterMeta;
import com.gm.javaeaseframe.common.util.excel.common.WorksheetData;
import com.gm.javaeaseframe.common.util.excel.reader.ExcelReaderFactory;
import com.gm.javaeaseframe.common.util.excel.reader.IExcelReader;
import com.gm.javaeaseframe.common.util.excel.writer.Excel2003Writer;
import com.gm.javaeaseframe.common.util.excel.writer.Excel2007Writer;
import com.gm.javaeaseframe.common.util.excel.writer.IExcelWriter;

public class ExcelUtils
{
	/** 是否打印日志，true：打印，默认false */
	public static boolean DEBUG = false;
	
	/** excel2003扩展名 */  
    private static final String EXCEL03_EXTENSION = "xls";  
    /** excel2007扩展名 */  
    private static final String EXCEL07_EXTENSION = "xlsx";  
    
    /**
     * 读取Excel文件
     * @param filePath              文件路径
     * @param dataCallback
     * @throws Exception
     */
    public static void readExcel(String filePath, IDataReaderCallback dataCallback) throws Exception {
        readExcel(filePath, false, false, dataCallback);
    }
    /**
     * 读取Excel文件
     * @param filePath              文件路径
     * @param incloudTitleRow       包含主标题行，比如：XXX统计报表
     * @param incloudColumnRow      包含栏标题行，比如：姓名    出生日期    性别
     * @param dataCallback
     * @throws Exception
     */
    public static void readExcel(String filePath, boolean incloudTitleRow, boolean incloudColumnRow, IDataReaderCallback dataCallback) throws Exception {
        ExcelReaderMeta meta = new ExcelReaderMeta();
        meta.setFilePath(filePath);
        meta.setIncloudTitleRow(incloudTitleRow);
        meta.setIncloudColumnRow(incloudColumnRow);
        readExcel(meta, dataCallback);
    }
	/**
	 * 读取Excel文件
	 * 
	 * @param reader		读取数据回调对象
	 * @param meta			读文件描述信息
	 * @throws Exception
	 */
	public static void readExcel(ExcelReaderMeta meta, IDataReaderCallback dataCallback) throws Exception
	{
		IExcelReader excelReader = createReader(meta, dataCallback);
		excelReader.process();
	}
	/**
	 * 读取Excel文件
	 * @param queue		数据存储队列
	 * @param meta
	 * @throws Exception
	 */
	public static void readExcel(ExcelReaderMeta meta, final BlockingQueue<String[]> queue) throws Exception
	{
		readExcel(meta, new IDataReaderCallback()
		{
			@Override
			public boolean doCallback(WorksheetData result)
			{
				try
				{
					List<String[]> dataList = result.getDataList();
					if(dataList != null && dataList.size() > 0)
					{
						for(String[] data : dataList)
						{
							queue.put(data);
						}
					}
					return true;
				}
				catch(Exception e){}
				return false;
			}
		});
	}
	/**
	 * 读取Excel的值，每个sheet一个ExcelReaderResultVO对象
	 * @param meta
	 * @return
	 * @throws Exception
	 */
	public static List<WorksheetData> readExcel(ExcelReaderMeta meta) throws Exception
	{
		final List<WorksheetData> retList = new ArrayList<>();
		readExcel(meta, new IDataReaderCallback()
		{
			@Override
			public boolean doCallback(WorksheetData result)
			{
				try
				{
					retList.add(result);
					return true;
				}
				catch(Exception e){}
				return false;
			}
		});
		return retList;
	}
	/**
	 * 存储Excel文件
	 * @param filePath         文件路径
	 * @param dataCallback
	 * @throws Exception
	 */
	public static void writerExcel(String filePath, IDataWriterCallback dataCallback) throws Exception {
	    writerExcel(filePath, null, null, dataCallback);
	}
	/**
	 * 存储Excel文件
	 * @param filePath         文件路径
	 * @param title            标题
	 * @param columnName       标题栏
	 * @param dataCallback
	 * @throws Exception
	 */
	public static void writerExcel(String filePath, String title, String[] columnName, IDataWriterCallback dataCallback) throws Exception {
	    ExcelWriterMeta meta = new ExcelWriterMeta();
	    meta.setFilePath(filePath);
	    meta.setTitle(title);
	    meta.setColumnName(columnName);
	    writerExcel(meta, dataCallback);
	}
    /**
     * 存储Excel文件
     * @param writer		写数据回调对象
     * @param exportData
     * @throws Exception
     */
    public static void writerExcel(ExcelWriterMeta meta, IDataWriterCallback dataCallback) throws Exception
    {
    	IExcelWriter excelWriter = createWriter(meta, dataCallback);
    	excelWriter.process();
    }
    /**
     * 存储Excel文件
     * @param filePath      文件路径
     * @param dataList      数据集
     * @throws Exception
     */
    public static void writerExcel(String filePath, final List<String[]> dataList) throws Exception {
        writerExcel(filePath, null, null, dataList);
    }
    /**
     * 存储Excel文件
     * @param filePath          文件路径
     * @param title             标题
     * @param columnName        标题栏
     * @param dataList          数据集
     * @throws Exception
     */
    public static void writerExcel(String filePath, String title, String[] columnName, final List<String[]> dataList) throws Exception {
        ExcelWriterMeta meta = new ExcelWriterMeta();
        meta.setFilePath(filePath);
        meta.setTitle(title);
        meta.setColumnName(columnName);
        writerExcel(meta, dataList);
    }
    /**
     * 存储Excel文件
     * @param meta
     * @param dataList
     * @throws Exception
     */
    public static void writerExcel(ExcelWriterMeta meta, final List<String[]> dataList) throws Exception
    {
    	writerExcel(meta, new IDataWriterCallback()
		{
			@Override
			public List<String[]> doCallback(int index)
			{
				if(index <= 1)
				{
					return dataList;
				}
				return null;
			}
		});
    }
    
    private static IExcelReader createReader(ExcelReaderMeta meta, IDataReaderCallback dataCallback) throws Exception
    {
//    	String ext = FileUtils.getExtend(meta.getFilePath());
//		if (StringUtils.isEmpty(ext))
//		{
//			throw new Exception("文件格式错误，文件的扩展名只能是xls或xlsx");
//		}
//		else if (ext.equalsIgnoreCase(EXCEL03_EXTENSION))
//		{
//			return new Excel2003Reader(DEBUG, reader, meta);
//		}
//		else if (ext.equalsIgnoreCase(EXCEL07_EXTENSION))
//		{
//			return new Excel2007Reader(DEBUG, reader, meta);
//		}
//		else
//		{
//			throw new Exception("文件格式错误，文件的扩展名只能是xls或xlsx");
//		}
		return ExcelReaderFactory.createReader(DEBUG, dataCallback, meta);
    }
    private static IExcelWriter createWriter(ExcelWriterMeta meta, IDataWriterCallback writer) throws Exception
    {
		String ext = FileUtil.getExtend(meta.getFilePath());
		if (StringUtils.isBlank(ext))
		{
			throw new Exception("文件格式错误，文件的扩展名只能是xls或xlsx");
		}
		else if (ext.equalsIgnoreCase(EXCEL03_EXTENSION))
		{
			return new Excel2003Writer(DEBUG, writer, meta);
		}
		else if (ext.equalsIgnoreCase(EXCEL07_EXTENSION))
		{
			return new Excel2007Writer(DEBUG, writer, meta);
		}
		else
		{
			throw new Exception("文件格式错误，文件的扩展名只能是xls或xlsx");
		}
    }
	public static void main(String[] args) throws Exception{
		boolean isRead = false;
		boolean isWrite = !isRead;
		String filePath = "/Users/Desktop/output4.xlsx";
		String templateFilePath = "/Users/Desktop/流量产品信息.xlsx";
		templateFilePath = "/Users/Desktop/流量产品信息.xls";
		templateFilePath = "/Users/Desktop/module.xlsx";
		templateFilePath = "/Users/Desktop/module.xls";

		filePath = "/Users/Desktop/POI-2007-1000W-5-2.xlsx";
		filePath = "/Users/Desktop/POI-2003-1000W-5.xls";
//		filePath = "/Users/Desktop/POI-2003-10W.xls";
//		filePath = "/Users/Desktop/POI-2007-1W.xlsx";
		ExcelUtils.DEBUG = true;
		if(isRead)
		{
			ExcelReaderMeta readerMeta = new ExcelReaderMeta();
			readerMeta.setFilePath(filePath);
//			readerMeta.setIncloudTitleRow(true);
			readerMeta.setIncloudColumnRow(true);
//			readerMeta.setSheetIndex(2);
//			readerMeta.setMaxRowPreSheet(10);
//			readerMeta.setMaxRowForEveryTime(3);
			
			ExcelUtils.readExcel(readerMeta, new IDataReaderCallback()
			{
				@Override
				public boolean doCallback(WorksheetData result)
				{
//					System.out.println(String.format("%s-->%s-->%s-->%s-->%s-->%s", result.getSheetIndex(), result.getSheetName(), result.getCurrRowIndex(), result.getCurrRowCount(), 
//							result.getTotalRowCount(), StringUtils.converArray2Str(result.getDataList())));
					return true;
				}
			});
		}
		if(isWrite)
		{
		    long startTime = System.currentTimeMillis();
		    System.out.println("开始写...");
//			filePath = "/Users/Desktop/POI-2003-1W-5.xls";
			filePath = "/Users/Desktop/POI-2007-1000W.xlsx";
			ExcelWriterMeta exportData = new ExcelWriterMeta();
			exportData.setFilePath(filePath);
			exportData.setTemplateFilePath(templateFilePath);
//			exportData.setTemplateIncloudTitle(true);
			exportData.setTemplateIncloudColumn(true);
			exportData.setCreateUser("GM");
//			exportData.setSheetName("测试sheet");
//			exportData.setMaxRowPreSheet(10);
//			exportData.setTitle("测试主题");
			exportData.setColumnName(new String[]{"姓名", "性别", "生日", "行标", "列标", "职业", "职责", "Oh yes"});
			ExcelUtils.writerExcel(exportData, new IDataWriterCallback()
			{
				
				@Override
				public List<String[]> doCallback(int index)
				{
					int count = 1000000;
					if(index <= 1)
					{
						List<String[]> dataList2 = new ArrayList<>();
						for(int i = (index - 1) * count; i < index * count; i++)
						{
							dataList2.add(new String[]{"张三" + i, i % 5 == 0 ? "女" : "男", "2017-07-21", index + "-" + i, index + "-B" + i
//									, "工程师-" + index + "-" + i, "干活-" + index + "-" + i,  (i / 12) + "-" + index + "-" + i
//									, "6-" + index, "7-" + index, "8-" + index, "9-" + index, "10-" + index, "11-" + index
									});
						}
						return dataList2;
					}
					return null;
				}
			});
			System.out.println("结束写，用时：" + (System.currentTimeMillis() - startTime) / 1000 + "秒");
		}
	}
}
