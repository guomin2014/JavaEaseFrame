package com.gm.javaeaseframe.common.util.excel.reader;

import java.awt.Font;
import java.awt.FontMetrics;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.util.CellRangeAddress;

import com.gm.javaeaseframe.common.util.MD5Util;
import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.CellTemplate;
import com.gm.javaeaseframe.common.util.excel.common.CodeEnum;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderTemplateMeta;
import com.gm.javaeaseframe.common.util.excel.common.RowTemplate;
import com.gm.javaeaseframe.common.util.excel.common.RowTypeEnum;
import com.gm.javaeaseframe.common.util.excel.common.Workbook;
import com.gm.javaeaseframe.common.util.excel.common.Worksheet;

/**
 * Excel2003读取器，通过实现HSSFListener监听器，采用事件驱动模式解析excel2003中的内容，遇到特定事件才会触发，大大减少了内存的使用。
 * 
 * 基于POI HSSF的eventmodel 模型的时间解析方式
 * 优点：解析数据相当快
 * 缺点：仅仅支持97~2003版本的excel，不支持2007版本的excel
 * 
 * @author GuoMin
 * @date 2017年7月14日
 */
public class Excel2003Reader extends AbstractExcelReader implements HSSFListener
{

	/** Should we output the formula, or the value it has? */
	private boolean outputFormulaValues = true;
	/** For parsing Formulas */
	private SheetRecordCollectingListener workbookBuildingListener;
	/** excel2003工作薄 */
	private HSSFWorkbook stubWorkbook;

	/** SSTRecords store a array of unique strings used in Excel */
	private SSTRecord sstRecord;
	private FormatTrackingHSSFListener formatListener;
	
	// For handling formulas with string results
	private int nextRow;
	private int nextColumn;
	private boolean outputNextStringRecord;

	/** 所有sheet的集合 */
	private BoundSheetRecord[] orderedBSRs;
	private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();
	/** 所有sheet的集合 */
	private List<Worksheet> sheetList = new ArrayList<>();

	/** 行数据 */
	private Map<Integer, String> dataMap = new TreeMap<>();
	/** 是否是非空行 */
	private boolean isNotEmptyRow = true;
	/** 模板的风格集合，key：风格的摘要（MD5加密串），value：风格名称 */
	private Map<String, String> templateStyleMap = new HashMap<>();

	public Excel2003Reader(String prefix, boolean printLog, IDataReaderCallback rowReader, ExcelReaderMeta meta)
	{
		super(prefix, printLog, rowReader, meta);
	}
	
	public Workbook processWorkbook() throws Exception
	{
		InputStream is = null;
		try
		{
			if (StringUtils.isNotBlank(meta.getFilePath())) {
				is = new BufferedInputStream(new FileInputStream(meta.getFilePath()));
			} else if (meta.getInput() != null) {
				is = new BufferedInputStream(meta.getInput());
			}
			if(super.useTemplate)
			{
				return this.parserTemplate(is);
			}
			else
			{
				POIFSFileSystem fs = new POIFSFileSystem(is);
				MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
				formatListener = new FormatTrackingHSSFListener(listener);
				HSSFEventFactory factory = new HSSFEventFactory();
				HSSFRequest request = new HSSFRequest();
				if (outputFormulaValues)
				{
					request.addListenerForAllRecords(formatListener);
				}
				else
				{
					workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
					request.addListenerForAllRecords(workbookBuildingListener);
				}
				factory.processWorkbookEvents(request, fs);
			}
		}
		catch(RuntimeException e)
		{
			String msg = e.getMessage();
			CodeEnum code = CodeEnum.findByValue(msg);
			if(code == null)
			{
				throw e;
			}
		}
		finally
		{
			if(is != null)
			{
				try
				{
					is.close();
				}catch(Exception e){}
			}
		}
		Workbook result = new Workbook();
		result.setTotalRowCount(totalRowCount);
		result.setSheetList(sheetList);
		return result;
	}

	/**
	 * HSSFListener 监听方法，处理 Record
	 */
	public void processRecord(Record record)
	{
		short sid = record.getSid();
		// 过滤sheet，只解析需要的sheet
		if (super.isSkipSheet()
				&& sid != BoundSheetRecord.sid && sid != BOFRecord.sid && sid != SSTRecord.sid && sid != EOFRecord.sid)
		{
			return;
		}
		int thisRow = -1;
		int thisColumn = -1;
		String thisStr = null;
		String value = null;
		switch (sid)
		{
		case BOFRecord.sid:// 表示Workbook或Sheet区域的开始
			BOFRecord br = (BOFRecord) record;
			if (br.getType() == BOFRecord.TYPE_WORKSHEET)// 读取新的一个Sheet页
			{
				// 如果有需要，则建立子工作薄
				if (workbookBuildingListener != null && stubWorkbook == null)
				{
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}
				if (orderedBSRs == null)
				{
					orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
				}
				String sheetName = orderedBSRs[super.getSheetIndex()].getSheetname();
				this.beginWorksheet(sheetName);
			}
			break;
		case EOFRecord.sid:
			Worksheet sheet = this.endWorksheet();
			if(sheet != null)
			{
				this.sheetList.add(sheet);
			}
			if(super.isEndWorkbook())
			{
				throw new RuntimeException(CodeEnum.FAILURE_END_WORKBOOK.getCode());
			}
			break;
		case BoundSheetRecord.sid:// 开始解析Sheet的信息，记录sheet，这儿会把所有的sheet都顺序打印出来，如果有多个sheet的话，可以顺序记入到一个List里
			boundSheetRecords.add((BoundSheetRecord) record);
			break;
		case SSTRecord.sid:// SSTRecords store a array of unique strings used in Excel
			sstRecord = (SSTRecord) record;
			break;
		case RowRecord.sid:// 执行行记录事件
			// RowRecord rowrec = (RowRecord) record;
			// logger.debug("记录开始, first column at " + rowrec.getFirstCol() + " last column at " + rowrec.getLastCol());
			break;
		case BlankRecord.sid:
			BlankRecord brec = (BlankRecord) record;
			thisRow = brec.getRow();
			thisColumn = brec.getColumn();
			thisStr = "";
			dataMap.put(thisColumn, thisStr);
			if(StringUtils.isNotBlank(thisStr))
			{
				isNotEmptyRow = true;
			}
			break;
		case BoolErrRecord.sid: // 单元格为布尔类型
			BoolErrRecord berec = (BoolErrRecord) record;
			thisRow = berec.getRow();
			thisColumn = berec.getColumn();
			thisStr = berec.getBooleanValue() + "";
			dataMap.put(thisColumn, thisStr);
			if(StringUtils.isNotBlank(thisStr))
			{
				isNotEmptyRow = true;
			}
			break;

		case FormulaRecord.sid: // 单元格为公式类型
			FormulaRecord frec = (FormulaRecord) record;
			thisRow = frec.getRow();
			thisColumn = frec.getColumn();
			if (outputFormulaValues)
			{
				if (Double.isNaN(frec.getValue()))
				{
					// Formula result is a string
					// This is stored in the next record
					outputNextStringRecord = true;
					nextRow = frec.getRow();
					nextColumn = frec.getColumn();
				}
				else
				{
					thisStr = formatListener.formatNumberDateCell(frec);
				}
			}
			else
			{
				thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
			}
			dataMap.put(thisColumn, thisStr);
			if(StringUtils.isNotBlank(thisStr))
			{
				isNotEmptyRow = true;
			}
			break;
		case StringRecord.sid:// 单元格中公式的字符串
			if (outputNextStringRecord)
			{
				// String for formula
				StringRecord srec = (StringRecord) record;
				thisStr = srec.getString();
				thisRow = nextRow;
				thisColumn = nextColumn;
				outputNextStringRecord = false;
			}
			break;
		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;
			thisRow = lrec.getRow();
			thisColumn = lrec.getColumn();
			rowIndex = thisRow + 1;
			value = lrec.getValue().trim();
			dataMap.put(thisColumn, StringUtils.trim(value));
			if(StringUtils.isNotBlank(thisStr))
			{
				isNotEmptyRow = true;
			}
			break;
		case LabelSSTRecord.sid: // 单元格为字符串类型
			LabelSSTRecord lsrec = (LabelSSTRecord) record;
			thisRow = lsrec.getRow();
			thisColumn = lsrec.getColumn();
			rowIndex = thisRow + 1;
			if (sstRecord == null)
			{
				dataMap.put(thisColumn, "");
				thisStr = "";
			}
			else
			{
				value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
				thisStr = StringUtils.trim(value);
			}
			dataMap.put(thisColumn, thisStr);
			if(StringUtils.isNotBlank(thisStr))
			{
				isNotEmptyRow = true;
			}
			break;
		case NumberRecord.sid: // 单元格为数字类型，因为数字和日期都是用这个格式，所以要对数字进行日期识别
			NumberRecord numrec = (NumberRecord) record;
			thisRow = numrec.getRow();
			thisColumn = numrec.getColumn();
			rowIndex = thisRow + 1;
			value = formatListener.formatNumberDateCell(numrec).trim();
			// 向容器加入列值
			dataMap.put(thisColumn, StringUtils.trim(value));
			if(StringUtils.isNotBlank(thisStr))
			{
				isNotEmptyRow = true;
			}
			break;
		default:
			break;
		}

		// 空值的操作
		if (record instanceof MissingCellDummyRecord)
		{
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			rowIndex = thisRow + 1;
			dataMap.put(thisColumn, "");
		}
		// 行结束时的操作
		if (record instanceof LastCellOfRowDummyRecord)
		{
			if(isNotEmptyRow)
			{
				this.addRowDataWithException(rowIndex, dataMap.values().toArray(new String[dataMap.size()]));
				isNotEmptyRow = false;
			}
			// 清空容器
			dataMap.clear();
		}
	}
	
	/**
	 * 创建默认模板文件
	 * @return
	 * @throws Exception
	 */
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
		HSSFWorkbook wb = new HSSFWorkbook();
		// 设置字体  
		HSSFFont defaultFont = wb.createFont();// 创建字体对象  
		defaultFont.setFontHeightInPoints((short) 12);// 设置字体大小  
//        defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体  
		defaultFont.setFontName("宋体");//设置为宋体字
        HSSFFont titleFont = wb.createFont();// 创建字体对象  
        titleFont.setFontHeightInPoints((short) 14);// 设置字体大小  
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体  
        titleFont.setFontName("宋体");//设置为宋体字 
        HSSFFont columnFont = wb.createFont();// 创建字体对象  
        columnFont.setFontHeightInPoints((short) 12);// 设置字体大小  
        columnFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体  
        columnFont.setFontName("宋体");//设置为宋体字 
        
        HSSFCellStyle titleStyle = wb.createCellStyle();// 创建样式对象 
        //设置字体
        titleStyle.setFont(titleFont);
        //设置对齐
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中 
        // 设置边框  
//        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线  
//        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);// 设置为黑色  
//        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);// 底部边框粗线
//        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);// 设置为黑色 
//        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框  
//        titleStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框 
//        titleStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        HSSFCellStyle columnStyle = wb.createCellStyle();// 创建样式对象  
        //设置字体
        columnStyle.setFont(columnFont);
        //设置对齐
        columnStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
        columnStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中 
        // 设置边框  
//        columnStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线  
//        columnStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);// 底部边框粗线 
//        columnStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框  
//        columnStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
        HSSFCellStyle dataStyle = wb.createCellStyle();// 创建样式对象
		// 设置字体
		dataStyle.setFont(defaultFont);
		// 设置对齐
//		columnStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
        
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultRowHeightInPoints(20);
		sheet.setDefaultColumnWidth(10);
		int rownum = 0;
		if(StringUtils.isNotBlank(title))
		{
			HSSFRow row = sheet.createRow(rownum);
			row.setHeightInPoints(30);
			HSSFCell cell = row.createCell(0);
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
			HSSFRow row = sheet.createRow(rownum++);
			row.setHeightInPoints(20);
			for(int index = 0; index < columns.length; index++)
			{
				HSSFCell cell = row.createCell(index);
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
		HSSFRow row = sheet.createRow(rownum++);
		row.setHeightInPoints(20);
		for(int index = 0; index < columnCount; index++)
		{
			HSSFCell cell = row.createCell(index);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(dataStyle);
		}
		File file = new File(meta.getFilePath());
		String filePath = StringUtils.appendIfMissing(file.getParent(), "/") + "template-2003.xls";
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
	/**
	 * 解析模板
	 * @param is
	 * @return
	 */
	private Workbook parserTemplate(InputStream is)
	{
		Workbook workbook = new Workbook();
		try
		{
			int sheetIndex = 0;
			HSSFWorkbook wb = new HSSFWorkbook(is);
			HSSFFormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator(wb);
			HSSFSheet sheet = wb.getSheetAt(sheetIndex);
			if(sheet == null)
			{
				return null;
			}
			int columnCount = 0;
			Map<Integer, CellRangeAddress> margeMap = new HashMap<>();
			//获取样式
			int maxRowNum = Math.min(sheet.getLastRowNum() + 1, 3);
			int margeNum = sheet.getNumMergedRegions();
			for(int index = 0; index < margeNum; index++)
			{
				CellRangeAddress range = sheet.getMergedRegion(index);
//				int firstColumn = range.getFirstColumn();  
//				int lastColumn = range.getLastColumn();  
				int firstRow = range.getFirstRow();  
//				int lastRow = range.getLastRow();
				margeMap.put(firstRow, range);
			}
			Worksheet worksheet = new Worksheet(sheetIndex + 1, sheet.getSheetName(), maxRowNum, columnCount);
			List<String> styleList = new ArrayList<>();
			for(int rowIndex = 0; rowIndex < maxRowNum; rowIndex++)
			{
	        	RowTypeEnum rowType = super.getRowType(rowIndex + 1);//行标从1开始
				if(rowType == null)
				{
					continue;
				}
				RowTemplate rowTemplate = new RowTemplate();
				Map<Integer, CellTemplate> cellTemplates = new HashMap<>();
				rowTemplate.setCellTemplates(cellTemplates);
				rowTemplate.setRowType(rowType.getCode());
				HSSFRow row = sheet.getRow(rowIndex);
				Iterator<Cell> cells = row.cellIterator();
				int firstMargeColumn = -1;
				int mergeAcross = 0;
				if(margeMap.containsKey(rowIndex))
				{
					CellRangeAddress range = margeMap.get(rowIndex);
					firstMargeColumn = range.getFirstColumn();
					mergeAcross = range.getLastColumn();
				}
				int idx = 0;
				while(cells.hasNext())
				{
					idx++;
					String text = "";
					Cell cell = cells.next();
					int cellType = cell.getCellType();
					if(cellType == Cell.CELL_TYPE_FORMULA)
					{
						text = formulaEvaluator.evaluate(cell).getNumberValue() + "";//cell.getCellFormula();
					}
					else if(cellType == Cell.CELL_TYPE_NUMERIC)
					{
						text = cell.getNumericCellValue() + "";
					}
					else
					{
						text = cell.getStringCellValue();
					}
					CellTemplate cellTemplate = new CellTemplate();
					cellTemplate.setText(text);
					cellTemplate.setAttributes(this.convertStyle(wb, cell.getCellStyle(), styleList));
					if(idx - 1 == firstMargeColumn)
					{
						cellTemplate.getAttributes().put("ss:MergeAcross", mergeAcross + "");
					}
					else if(idx - 1 < mergeAcross)
					{
						continue;
					}
					cellTemplates.put(idx, cellTemplate);
				}
				columnCount = Math.max(columnCount, idx);
				rowTemplate.setAttributes(this.convertStyle(wb, row.getRowStyle(), styleList));
				float height = row.getHeightInPoints();
				if(height != 0)
				{
					rowTemplate.getAttributes().put("ss:AutoFitHeight", "0");
					rowTemplate.getAttributes().put("ss:Height", String.valueOf(height));
				}
				if(rowType == RowTypeEnum.TITLE)
    			{
					worksheet.setTitleTemplate(rowTemplate);
    			}
    			else if(rowType == RowTypeEnum.COLUMN)
    			{
    				worksheet.setColumnTemplate(rowTemplate);
    			}
    			else if(rowType == RowTypeEnum.DATA)
    			{
    				worksheet.setDataTemplate(rowTemplate);
    				break;
    			}
			}
			StringBuffer header = new StringBuffer();
			StringBuffer footer = new StringBuffer();
			int defaultColumnWidth = sheet.getDefaultColumnWidth();
			float defaultRowHeight = sheet.getDefaultRowHeightInPoints();
			int characterWidth = this.getDefaultCharacterWidth(wb);
			int defaultColumnWidthPX = this.convertPX(characterWidth, defaultColumnWidth);
			header.append("<Table ss:ExpandedColumnCount=\"256\" ss:ExpandedRowCount=\"65535\"");
			header.append(" x:FullColumns=\"1\" x:FullRows=\"1\" ss:DefaultColumnWidth=\"" + defaultColumnWidthPX + "\" ss:DefaultRowHeight=\"" + defaultRowHeight + "\">");
			for(int idx = 0; idx < columnCount; idx++)
			{
				//<Column ss:Index="6" ss:AutoFitWidth="0" ss:Width="102"/>
				int columnWidth = sheet.getColumnWidth(idx) / 256;
//				System.out.println(idx + "-->" + columnWidth + "-->" + sheet.getColumnWidth(idx));
				if(columnWidth != defaultColumnWidth)
				{
					int sIndex = idx + 1;
					int columnWidthPX = this.convertPX(characterWidth, columnWidth);
					header.append("<Column ss:Index=\"" + sIndex + "\" ss:AutoFitWidth=\"0\" ss:Width=\"" + columnWidthPX +"\"/>");
				}
			}
			footer.append("</Table>");
			worksheet.setColumnCount(columnCount);
			worksheet.getHeaderDefaultElements().add(header.toString());
			worksheet.getFooterDefaultElements().add(footer.toString());
			List<Worksheet> sheetList = new ArrayList<>();
			sheetList.add(worksheet);
			workbook.setSheetList(sheetList);
			workbook.setTotalRowCount(maxRowNum);
			if(styleList.size() > 0)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("<Styles>").append("\n");
				for(String style : styleList)
				{
					sb.append(style).append("\n");
				}
				sb.append("</Styles>");
				workbook.getHeaderDefaultElements().add(sb.toString());
			}
		}
		catch(Exception e)
		{
			if (debug)
			{
				e.printStackTrace();
				logger.info(String.format("【%s】解析excel文档异常-->%s，原因：%s", OPER_PREFIX, meta.getFilePath(), e.getMessage()));
			}
		}
		return workbook;
	}
	/**
	 * 获取默认的字符宽度
	 * @param wb
	 * @return
	 */
	private int getDefaultCharacterWidth(HSSFWorkbook wb)
	{
		try
		{
			HSSFFont font = wb.getFontAt((short)0);//默认字体
			Font f = new Font(font.getFontName(), Font.BOLD, font.getFontHeightInPoints());   
			FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
			return fm.charWidth('0') + (font.getFontName().equals("Arial") ? 1 : 0);
		}
		catch(Exception e)
		{
			return 8;
		}
	}
	/**
	 * 将字符个数转换成像素宽度
	 * 
	 * 像素 = 2个像素空白 + (字符个数 * 默认字符的宽度) + 2个像素空白 - (字符个数 - 1)
	 * 像素 = 5个像素空白 + (字符个数 * (默认字符的宽度 - 1))
	 * 
	 * @param characterWidth	字符宽度
	 * @param characterCount	字符个数
	 * @return
	 */
	private int convertPX(int characterWidth, int characterCount)
	{
		return 5 + (characterCount * (characterWidth - 1));
	}
	
	private Map<String, String> convertStyle(HSSFWorkbook wb, CellStyle style, List<String> headerElements)
	{
		Map<String, String> attributes = new LinkedHashMap<>();
		if(style == null)
		{
			return attributes;
		}
		StringBuffer sb = new StringBuffer();
		this.buildAlignment(sb, style);
		this.buildBorders(sb, style);
		this.buildFont(sb, wb, style);
		this.buildInterior(sb, style);
		String md5 = MD5Util.getSignAndMD5(sb.toString());
		if(templateStyleMap.containsKey(md5))
		{
			attributes.put("ss:StyleID", templateStyleMap.get(md5));
		}
		else
		{
			String name = "Cust-" + templateStyleMap.size();
			sb.insert(0, "<Style ss:ID=\"" + name + "\">");
			sb.append("</Style>");
			templateStyleMap.put(md5, name);
			headerElements.add(sb.toString());
			attributes.put("ss:StyleID", name);
		}
		return attributes;
	}
	
	private void buildAlignment(StringBuffer sb, CellStyle style)
	{
		sb.append("<Alignment");
		String horizontal = "";
		String vertical = "";
		short align = style.getAlignment();
		short valign = style.getVerticalAlignment();
		switch(align)
		{
			case CellStyle.ALIGN_GENERAL:
				break;
			case CellStyle.ALIGN_LEFT:
				horizontal = "Left";
				break;
			case CellStyle.ALIGN_CENTER:
				horizontal = "Center";
				break;
			case CellStyle.ALIGN_RIGHT:
				horizontal = "Right";
				break;
			case CellStyle.ALIGN_FILL:
				horizontal = "Fill";
				break;
			case CellStyle.ALIGN_JUSTIFY:
				horizontal = "Justify";
				break;
			case CellStyle.ALIGN_CENTER_SELECTION:
				horizontal = "CenterAcrossSelection";
				break;
		}
		switch(valign)
		{
			case CellStyle.VERTICAL_TOP:
				vertical = "Top";
				break;
			case CellStyle.VERTICAL_CENTER:
				vertical = "Center";
				break;
			case CellStyle.VERTICAL_BOTTOM:
				vertical = "Bottom";
				break;
			case CellStyle.VERTICAL_JUSTIFY:
				vertical = "Justify";
				break;
		}
		if(StringUtils.isNotEmpty(horizontal))
		{
			sb.append(" ss:Horizontal=\"" + horizontal + "\"");
		}
		if(StringUtils.isNotEmpty(vertical))
		{
			sb.append(" ss:Vertical=\"" + vertical + "\"");
		}
		sb.append("/>");
	}
	
	private void buildBorders(StringBuffer sb, CellStyle style)
	{
		short borderTop = style.getBorderTop();
		short borderTopColor = style.getTopBorderColor();
		short borderBottom = style.getBorderBottom();
		short borderBottomColor = style.getBottomBorderColor();
		short borderLeft = style.getBorderLeft();
		short borderLeftColor = style.getLeftBorderColor();
		short borderRight = style.getBorderRight();
		short borderRightColor = style.getRightBorderColor();
		
		if(borderTop != CellStyle.BORDER_NONE 
				|| borderBottom != CellStyle.BORDER_NONE
				|| borderLeft != CellStyle.BORDER_NONE
				|| borderRight != CellStyle.BORDER_NONE)
		{
			sb.append("<Borders>");
			this.buildBoder(sb, "Top", borderTop, borderTopColor);
			this.buildBoder(sb, "Bottom", borderBottom, borderBottomColor);
			this.buildBoder(sb, "Left", borderLeft, borderLeftColor);
			this.buildBoder(sb, "Right", borderRight, borderRightColor);
			sb.append("</Borders>");
		}
	}
	
	private void buildBoder(StringBuffer sb, String position, int border, int borderColor)
	{
		if(border != CellStyle.BORDER_NONE)
		{
			sb.append("<Border ss:Position=\"" + position + "\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"");
			String color = this.toHexFromColor(HSSFColor.getIndexHash().get((int)borderColor));
			if(StringUtils.isNotEmpty(color))
			{
				sb.append(" ss:Color=\"" + color + "\"");
			}
			sb.append("/>");
		}
	}
	
	private void buildFont(StringBuffer sb, HSSFWorkbook wb, CellStyle style)
	{
		HSSFFont font = wb.getFontAt(style.getFontIndex());
		if(font == null)
		{
			return;
		}
//		<Font ss:FontName="华文楷体" x:CharSet="134" ss:Size="20" ss:Color="#666699" ss:Bold="1"/>
		sb.append("<Font ss:FontName=\"" + font.getFontName() + "\" x:CharSet=\"" + font.getCharSet() 
		+ "\" ss:Size=\"" + font.getFontHeightInPoints() + "\" ss:Bold=\"" + font.getBoldweight()/700 + "\"");
		String color = this.toHexFromColor(HSSFColor.getIndexHash().get((int)font.getColor()));
		if(StringUtils.isNotEmpty(color))
		{
			sb.append(" ss:Color=\"" + color + "\"");
		}
		sb.append("/>");
	}
	
	private void buildInterior(StringBuffer sb, CellStyle style)
	{
		short pattern = style.getFillPattern();
//		Color background = style.getFillBackgroundColorColor();
		Color foreground = style.getFillForegroundColorColor();
		if(pattern == CellStyle.NO_FILL)
		{
			return;
		}
		String fillColor = "";
		String patternStr = "Solid";
		if(foreground != null)
		{
			fillColor = this.toHexFromColor((HSSFColor)foreground);
		}
		if(StringUtils.isEmpty(fillColor))
		{
			return;
		}
		//<Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
//		switch(pattern)
//		{
//		case CellStyle.NO_FILL:
//			break;
//		case CellStyle.SOLID_FOREGROUND:
//			patternStr = "Solid";
//			break;
//		default:
//			break;
//		}
		sb.append("<Interior ss:Color=\"" + fillColor + "\" ss:Pattern=\"" + patternStr + "\"/>");
	}
	
	/**
	 * Color对象转换成字符串
	 * @param color Color对象
	 * @return 16进制颜色字符串
	 * */
	private String toHexFromColor(HSSFColor color)
	{
		if(color == null)
		{
			return "";
		}
		short[] rgb = color.getTriplet();
		String r,g,b;
		StringBuilder su = new StringBuilder();
		r = Integer.toHexString(rgb[0]);
		g = Integer.toHexString(rgb[1]);
		b = Integer.toHexString(rgb[2]);
		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" +g : g;
		b = b.length() == 1 ? "0" + b : b;
		su.append("#");
		su.append(r);
		su.append(g);
		su.append(b);
		return su.toString().toUpperCase();
	}
}
