package com.gm.javaeaseframe.common.util.excel.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.gm.javaeaseframe.common.util.FileUtil;
import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.CodeEnum;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderTemplateMeta;
import com.gm.javaeaseframe.common.util.excel.common.HandlerResult;
import com.gm.javaeaseframe.common.util.excel.common.Workbook;
import com.gm.javaeaseframe.common.util.excel.common.Worksheet;
import com.gm.javaeaseframe.common.util.excel.handler.AbstractContentHandler;
import com.gm.javaeaseframe.common.util.excel.handler.CustomReadOnlySharedStringsTable;
import com.gm.javaeaseframe.common.util.excel.handler.Excel2007ContentHandler;

public class Excel2007Reader extends AbstractExcelReader
{
	/** 当前正在执行解析的状态 */
	private boolean isRun = true;
	
	public Excel2007Reader(String prefix, boolean printLog, IDataReaderCallback rowReader, ExcelReaderMeta meta)
	{
		super(prefix, printLog, rowReader, meta);
	}

	/**
	 * 解析工作薄，获取sheet信息
	 * @param inputStream
	 * @return		key：sheetId，value：sheetName
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> processWorkbook(InputStream inputStream) throws Exception
	{
		Map<String, String> retMap = new HashMap<>();
		String content = FileUtil.read(inputStream, "UTF-8");
		Document document = DocumentHelper.parseText(content);
		Element root = document.getRootElement();
		Element sheets = root.element("sheets");
		Iterator<Element> iterator = sheets.elementIterator();  
        while(iterator.hasNext()){  
            Element e = iterator.next();  
            String name = e.attributeValue("name");
            String sheetId = e.attributeValue("sheetId");
            retMap.put(sheetId, name);
        }
        return retMap;
	}
	
	public Workbook processWorkbook() throws Exception
	{
		OPCPackage xlsxPackage = null;
		List<Worksheet> sheetList = new ArrayList<>();
		int totalRowCount = 0;
		try
		{
			String filePath = meta.getFilePath();
			int sheetIndexTmp = meta.getSheetIndex();
			if (StringUtils.isNotBlank(meta.getFilePath())) {
				xlsxPackage = OPCPackage.open(filePath, PackageAccess.READ);
			} else if (meta.getInput() != null) {
				xlsxPackage = OPCPackage.open(meta.getInput());
			}
			ReadOnlySharedStringsTable sharedStringsTable = new CustomReadOnlySharedStringsTable(xlsxPackage);
			XSSFReader xssfReader = new XSSFReader(xlsxPackage);
			StylesTable stylesTable = xssfReader.getStylesTable();
			AbstractContentHandler<Worksheet> handler = new Excel2007ContentHandler<>(super.OPER_PREFIX, debug, logger, dataCallback, meta, sharedStringsTable, stylesTable);
			Map<String, String> sheetMap = processWorkbook(xssfReader.getWorkbookData());
			if(sheetIndexTmp != -1 && !sheetMap.containsKey(String.valueOf(sheetIndexTmp)))
			{
				throw new Exception("指定sheetIndex的工作薄不存在");
			}
			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			int sheetIndex = 0;
			isRun = true;
			while(isRun && sheets.hasNext())
			{
				InputStream sheetInputStream = sheets.next();
//				InputStream sheetInputStream = new BufferedInputStream(sheets.next());
				sheetIndex++;
				String sheetName = "";
				try
				{
					sheetName = sheetMap.get(String.valueOf(sheetIndex));
				}catch(Exception e){}
				try
				{
					HandlerResult<Worksheet> result = handler.processSheet(sheetIndex, sheetName, sheetInputStream, useTemplate);
					if(result != null)
					{
						CodeEnum code = CodeEnum.findByValue(result.getCode());
						if(code != null)
						{
							switch(code)
							{
								case SUCCESS:
								case SUCCESS_END_SHEET:
									sheetList.add(result.getData());
									totalRowCount += result.getData().getRowCount();
									break;
								case SUCCESS_END_WORKBOOK:
									sheetList.add(result.getData());
									totalRowCount += result.getData().getRowCount();
									isRun = false;
									break;
								case FAILURE:
								case FAILURE_END_WORKBOOK:
									isRun = false;
									break;
								case FAILURE_END_SHEET:
									break;
								default:
									break;
							}
						}
					}
				}
				finally
				{
					if(sheetInputStream != null)
					{
						try
						{
							sheetInputStream.close();
						}catch(Exception e){}
					}
				}
			}
		}
		finally
		{
			if(xlsxPackage != null)
			{
				try
				{
					xlsxPackage.close();
				}catch(Throwable e){}
			}
		}
		Workbook result = new Workbook();
		result.setTotalRowCount(totalRowCount);
		result.setSheetList(sheetList);
		return result;
	}

	@Override
	public String createDefaultTemplate() throws Exception
	{
		ExcelReaderTemplateMeta tmeta = (ExcelReaderTemplateMeta)meta;
		String title = tmeta.getTitle();
		String[] columns = tmeta.getColumnName();
		int columnCount = 0;
		if(columns != null)
		{
			columnCount = columns.length;
		}
		// 建立工作簿和电子表格对象
        XSSFWorkbook wb = new XSSFWorkbook();
     // 设置字体  
		XSSFFont defaultFont = wb.createFont();// 创建字体对象
		defaultFont.setFontHeightInPoints((short) 12);// 设置字体大小
//		defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		defaultFont.setFontName("宋体");// 设置为宋体字
		XSSFFont titleFont = wb.createFont();// 创建字体对象
		titleFont.setFontHeightInPoints((short) 14);// 设置字体大小
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		titleFont.setFontName("宋体");// 设置为宋体字
		XSSFFont columnFont = wb.createFont();// 创建字体对象
		columnFont.setFontHeightInPoints((short) 12);// 设置字体大小
		columnFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		columnFont.setFontName("宋体");// 设置为宋体字

		XSSFCellStyle titleStyle = wb.createCellStyle();// 创建样式对象
		// 设置字体
		titleStyle.setFont(titleFont);
		// 设置对齐
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
		XSSFCellStyle columnStyle = wb.createCellStyle();// 创建样式对象
		// 设置字体
		columnStyle.setFont(columnFont);
		// 设置对齐
		columnStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		columnStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
		XSSFCellStyle dataStyle = wb.createCellStyle();// 创建样式对象
		// 设置字体
		dataStyle.setFont(defaultFont);
		// 设置对齐
//		columnStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
        XSSFSheet sheet = wb.createSheet();
        sheet.setDefaultRowHeightInPoints(20);
		sheet.setDefaultColumnWidth(10);
		int rownum = 0;
		if(StringUtils.isNotBlank(title))
		{
			XSSFRow row = sheet.createRow(rownum);
			row.setHeightInPoints(36);
			XSSFCell cell = row.createCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(title);
			if(columnCount > 1)
			{
				CellRangeAddress range = new CellRangeAddress(rownum, rownum, 0, columnCount - 1);
				sheet.addMergedRegion(range);
			}
			cell.setCellStyle(titleStyle);
			rownum++;
		}
		if(columns != null && columns.length > 0)
		{
			XSSFRow row = sheet.createRow(rownum++);
			row.setHeightInPoints(20);
			for(int index = 0; index < columns.length; index++)
			{
				XSSFCell cell = row.createCell(index);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(StringUtils.trim(columns[index]));
				cell.setCellStyle(columnStyle);
			}
		}
		//设置数据行
		if(columnCount == 0)
		{
			columnCount = 10;
		}
		XSSFRow row = sheet.createRow(rownum++);
		row.setHeightInPoints(20);
		for(int index = 0; index < columnCount; index++)
		{
			XSSFCell cell = row.createCell(index);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(dataStyle);
		}
        File file = new File(meta.getFilePath());
		String filePath = StringUtils.appendIfMissing(file.getParent(), "/") + "template-2007.xlsx";
		OutputStream os = null;
		try
		{
			os = new FileOutputStream(filePath);
			wb.write(os);
		}
		finally
		{
			if(os != null)
			{
				try
				{
					os.close();
				}catch(Exception e){}
			}
		}
		return filePath;
	}

}
