package com.gm.javaeaseframe.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelAgent {

	private static final String XLSX = "xlsx";

	private static Log log = LogFactory.getLog(ExcelAgent.class);
	
	private static Pattern pattern = null;
	
	public static void parseXls(String filePath, List<String> mobiles, List<String> errMobiles) {

		if (mobiles == null)
			mobiles = new ArrayList<String>();
		if (errMobiles == null)
			errMobiles = new ArrayList<String>();
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(filePath);
			POIFSFileSystem fs = new POIFSFileSystem(fileIn);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowsNum = sheet.getLastRowNum();
			for (int i = 0; i <= rowsNum; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;
				HSSFCell cell = row.getCell(0);
				if (cell == null) {
					continue;
				}
				String mobile = "";
				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					mobile = StringUtils.trim(cell.getStringCellValue());
				}
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					BigDecimal b = new BigDecimal(cell.getNumericCellValue());
					mobile = StringUtils.trim(b.toPlainString());
				}
				if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
					mobile = StringUtils.trim(cell.getCellFormula());
				}
				if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
					continue;
				}
				if (isLegalMobile(mobile))
					mobiles.add(mobile);
				else {
					errMobiles.add(mobile);
				}
			}

		} catch (FileNotFoundException e) {
			log.error("Excel文件不存在", e);
		} catch (IOException e) {
			log.error("解析Excel文件", e);
		} catch (Exception e) {
			log.error("解析Excel文件", e);
		} finally {
			closeInOut(fileIn);
		}

	}

	public static void parseXlsx(String filePath, List<String> mobiles, List<String> errMobiles) {

		if (mobiles == null)
			mobiles = new ArrayList<String>();
		if (errMobiles == null)
			errMobiles = new ArrayList<String>();
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(filePath);

			XSSFWorkbook wb = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowsNum = sheet.getLastRowNum();
			for (int i = 0; i <= rowsNum; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;
				XSSFCell cell = row.getCell(0);
				if (cell == null) {
					continue;
				}
				String mobile = "";
				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					mobile = StringUtils.trim(cell.getStringCellValue());
				}
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					BigDecimal b = new BigDecimal(cell.getNumericCellValue());
					mobile = StringUtils.trim(b.toPlainString());
				}
				if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
					mobile = StringUtils.trim(cell.getCellFormula());
				}
				if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
					continue;
				}
				if (isLegalMobile(mobile))
					mobiles.add(mobile);
				else {
					errMobiles.add(mobile);
				}
			}

		} catch (FileNotFoundException e) {
			log.error("Excel文件不存在", e);
		} catch (IOException e) {
			log.error("解析Excel文件", e);
		} catch (Exception e) {
			log.error("解析Excel文件", e);
		} finally {
			closeInOut(fileIn);
		}

	}

	private static void closeInOut(FileInputStream fileIn) {

		try {
			if (fileIn != null)
				fileIn.close();
		} catch (IOException e) {
			log.error("关闭文件流", e);
		}
	}

	/**
	 * 解析Excel手机号码
	 * 
	 * @param filePath
	 * @param mobileLength	号码限制长度
	 * @return	key: success,  error
	 */
	public static Map<String, List<String>> parseExcel(String filePath, int mobileLength) {
		Map<String, List<String>> retMap = new HashMap<String, List<String>>();
		List<String> mobiles = new ArrayList<String>(); 
		List<String> errors = new ArrayList<String>();
		pattern = Pattern.compile("^1[3|4|5|8][0-9]\\d{" + (mobileLength-3) + "}$");
		File excelFile = new File(filePath);
		if (excelFile.getName().toLowerCase().endsWith(XLSX)) {
			parseXlsx(filePath, mobiles, errors);
		} else {
			parseXls(filePath, mobiles, errors);
		}
		retMap.put("success", mobiles);
		retMap.put("error", errors);
		return retMap;
	}
	
	public static List<String[]> parseExcel(String filePath, boolean includeHeader) throws Exception
	{
		File excelFile = new File(filePath);
		if(!excelFile.exists())
		{
			throw new Exception("文件不存在【 " + filePath + "】");
		}
		if(excelFile.getName().toLowerCase().endsWith(XLSX))
		{
			return parseXlsx(filePath, includeHeader);
		}
		else
		{
			return parseXls(filePath, includeHeader);
		}
	}
	/**
	 * 解析xls
	 * @param filePath
	 * @param fis
	 * @param maxRow
	 * @param list
	 * @return	结果行数，如果结果行数小于maxRow，则表示文件已经读取完成
	 * @throws Exception
	 */
	public static int parseExcel(String filePath, int skipRow, int maxRow, List<String[]> list) throws Exception
	{
		File excelFile = new File(filePath);
		if(!excelFile.exists())
		{
			throw new Exception("文件不存在【 " + filePath + "】");
		}
		if(excelFile.getName().toLowerCase().endsWith(XLSX))
		{
			return parseXlsx(filePath, skipRow, maxRow, list);
		}
		else
		{
			return parseXls(filePath, skipRow, maxRow, list);
		}
	}
	
	private static int parseXls(String filePath, int skipRow, int maxRow, List<String[]> list) throws Exception
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filePath);
			POIFSFileSystem fs = new POIFSFileSystem(fis);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowsNum = sheet.getLastRowNum();
			int startRow = skipRow;
			for(int i = startRow; i <= rowsNum; i++)
			{
				HSSFRow row = sheet.getRow(i);
				if(row == null)
				{
					continue;
				}
				List<String> values = new ArrayList<String>();
				int columns = row.getLastCellNum();
				for(int n = 0; n <= columns; n++)
				{
					values.add(converHSSFCellValue(row.getCell(n)));
				}
				list.add(values.toArray(new String[0]));
				if(list.size() >= maxRow)
				{
					break;
				}
			}
		}
		finally
		{
			if(fis != null)
			{
				try
				{
					fis.close();
				}
				catch(Exception e){}
			}
		}
		return list.size();
	}
	
	private static int parseXlsx(String filePath, int skipRow, int maxRow, List<String[]> list) throws Exception
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowsNum = sheet.getLastRowNum();
			int startRow = skipRow;
			for (int i = startRow; i <= rowsNum; i++)
			{
				XSSFRow row = sheet.getRow(i);
				if (row == null)
				{
					continue;
				}
				List<String> values = new ArrayList<String>();
				int columns = row.getLastCellNum();
				for(int n = 0; n <= columns; n++)
				{
					values.add(converXSSFCellValue(row.getCell(n)));
				}
				list.add(values.toArray(new String[0]));
				if(list.size() >= maxRow)
				{
					break;
				}
			}
		}
		finally
		{
			if(fis != null)
			{
				try
				{
					fis.close();
				}
				catch(Exception e){}
			}
		}
		return list.size();
	}
	
	private static List<String[]> parseXls(String filePath, boolean includeHeader) throws Exception
	{
		List<String[]> retList = new ArrayList<String[]>();
		FileInputStream fileIn = null;
		try
		{
			fileIn = new FileInputStream(filePath);
			POIFSFileSystem fs = new POIFSFileSystem(fileIn);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowsNum = sheet.getLastRowNum();
			int startRow = 0;
			if(includeHeader)
			{
				startRow = 1;
			}
			for(int i = startRow; i <= rowsNum; i++)
			{
				HSSFRow row = sheet.getRow(i);
				if(row == null)
				{
					continue;
				}
				List<String> values = new ArrayList<String>();
				int columns = row.getLastCellNum();
				for(int n = 0; n <= columns; n++)
				{
					values.add(converHSSFCellValue(row.getCell(n)));
				}
				retList.add(values.toArray(new String[0]));
			}
		}
		finally
		{
			closeInOut(fileIn);
		}
		return retList;
	}
	
	private static List<String[]> parseXlsx(String filePath, boolean includeHeader) throws Exception
	{
		List<String[]> retList = new ArrayList<String[]>();
		FileInputStream fileIn = null;
		try
		{
			fileIn = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowsNum = sheet.getLastRowNum();
			int startRow = 0;
			if(includeHeader)
			{
				startRow = 1;
			}
			for (int i = startRow; i <= rowsNum; i++)
			{
				XSSFRow row = sheet.getRow(i);
				if (row == null)
				{
					continue;
				}
				List<String> values = new ArrayList<String>();
				int columns = row.getLastCellNum();
				for(int n = 0; n <= columns; n++)
				{
					values.add(converXSSFCellValue(row.getCell(n)));
				}
				retList.add(values.toArray(new String[0]));
			}
		}
		finally
		{
			closeInOut(fileIn);
		}
		return retList;
	}
	
	private static String converHSSFCellValue(HSSFCell cell)
	{
		String value = "";
		if(cell == null)
		{
			return value;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
		{
			value = StringUtils.trim(cell.getStringCellValue());
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
		{
			BigDecimal b = new BigDecimal(cell.getNumericCellValue());
			value = StringUtils.trim(b.toPlainString());
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA)
		{
			value = StringUtils.trim(cell.getCellFormula());
		}
		else
		{
			value = StringUtils.trim(cell.getStringCellValue());
		}
		return value;
	}
	private static String converXSSFCellValue(XSSFCell cell)
	{
		String value = "";
		if(cell == null)
		{
			return value;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
		{
			value = StringUtils.trim(cell.getStringCellValue());
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
		{
			BigDecimal b = new BigDecimal(cell.getNumericCellValue());
			value = StringUtils.trim(b.toPlainString());
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA)
		{
			value = StringUtils.trim(cell.getCellFormula());
		}
		else
		{
			value = StringUtils.trim(cell.getStringCellValue());
		}
		return value;
	}
	
	private static boolean isLegalMobile(String mobile)
	{
		if(pattern == null)
		{
			return true;
		}
		return pattern.matcher(mobile).find();
	}
	
	
	/**
	 * 导出文件
	 * 
	 * @param setInfo
	 * @param outputExcelFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean export2File(ExcelExportData setInfo, String outputExcelFileName) throws Exception
	{
		return FileUtil.write(outputExcelFileName, export2ByteArray(setInfo), true, true);
	}

	/**
	 * 导出到byte数组
	 * 
	 * @param setInfo
	 * @return
	 * @throws Exception
	 */
	public static byte[] export2ByteArray(ExcelExportData setInfo) throws Exception
	{
		return export2Stream(setInfo).toByteArray();
	}
	
	/**
	 * 导出到流
	 * 
	 * @param setInfo
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream export2Stream(ExcelExportData setInfo) throws Exception
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		
		short borderColor = IndexedColors.GREY_50_PERCENT.index;
		// 标题行字体
		Font titleFont = wb.createFont();
		titleFont.setFontName("华文楷体");
		titleFont.setFontHeightInPoints((short) 20);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setCharSet(Font.DEFAULT_CHARSET);
		titleFont.setColor(IndexedColors.BLUE_GREY.index);
		//标题行样式
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setFont(titleFont);
		titleStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
		//表头行字体
		Font headFont = wb.createFont();
		headFont.setFontName("宋体");
		headFont.setFontHeightInPoints((short) 10);
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headFont.setCharSet(Font.DEFAULT_CHARSET);
		headFont.setColor(IndexedColors.BLUE_GREY.index);
		//表头行样式
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headStyle.setFont(headFont);
		headStyle.setFillBackgroundColor(IndexedColors.YELLOW.index);
		headStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		headStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headStyle.setBorderRight(CellStyle.BORDER_THIN);
		headStyle.setTopBorderColor(borderColor);
		headStyle.setBottomBorderColor(borderColor);
		headStyle.setLeftBorderColor(borderColor);
		headStyle.setRightBorderColor(borderColor);
		//内容行字体
		Font contentFont = wb.createFont();
		contentFont.setFontName("宋体");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLUE_GREY.index);
		//内容行样式
		CellStyle contentStyle = wb.createCellStyle();
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_THIN);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);
		contentStyle.setBorderRight(CellStyle.BORDER_THIN);
		contentStyle.setTopBorderColor(borderColor);
		contentStyle.setBottomBorderColor(borderColor);
		contentStyle.setLeftBorderColor(borderColor);
		contentStyle.setRightBorderColor(borderColor);
		contentStyle.setWrapText(true); // 字段换行

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		String title = setInfo.getTitle();
		String[] columns = setInfo.getColumnName();
		int columnSize = columns.length;
		HSSFSheet sheet = wb.createSheet(title);
		int rowIndex = 0;
		//设置表头
		CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, columnSize-1);
		sheet.addMergedRegion(titleRange);
		HSSFRow titleRow = sheet.createRow(rowIndex++);
		titleRow.setHeight((short) 800);
		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(title);
		//设置数据标题
		HSSFRow headRow = sheet.createRow(rowIndex++);
		headRow.setHeight((short) 350);
		// 列头名称
		for (int num = 0; num < columnSize; num++)
		{
			HSSFCell headCell = headRow.createCell(num);
			headCell.setCellStyle(headStyle);
			headCell.setCellValue(columns[num]);
		}
		//设置数据行
		List<String[]> dataList = setInfo.getDataList();
		if(dataList != null)
		{
			for(String[] datas : dataList)
			{
				if(datas != null && datas.length > 0)
				{
					HSSFRow dataRow = sheet.createRow(rowIndex++);
					for(int col = 0; col < datas.length; col++)
					{
						HSSFCell headCell = dataRow.createCell(col);
						headCell.setCellStyle(contentStyle);
						headCell.setCellValue(datas[col]);
					}
				}
			}
		}
		adjustColumnSize(sheet, columnSize);
		//设置描述信息(比如统计数据等信息)
		wb.write(outputStream);
		return outputStream;
	}
	
	/**
	 * @Description: 自动调整列宽
	 */
	private static void adjustColumnSize(HSSFSheet sheet, int columnSize)
	{
		for (int i = 0; i < columnSize; i++)
		{
			sheet.autoSizeColumn(i, true);
		}
	}
	
	public static void main(String[] args) {
		Map<String, List<String>> result = parseExcel("D:\\mobile.xls", 7);
		System.out.println(result);
	}
	
}
