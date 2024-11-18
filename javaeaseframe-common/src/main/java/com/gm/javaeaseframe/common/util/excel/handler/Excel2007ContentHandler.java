package com.gm.javaeaseframe.common.util.excel.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.CellTemplate;
import com.gm.javaeaseframe.common.util.excel.common.CodeEnum;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.HandlerResult;
import com.gm.javaeaseframe.common.util.excel.common.RowTemplate;
import com.gm.javaeaseframe.common.util.excel.common.RowTypeEnum;
import com.gm.javaeaseframe.common.util.excel.common.Worksheet;
import com.gm.javaeaseframe.common.util.excel.common.XSSFDataType;
/**
 * Excel2007内容解析器
 * @author	GuoMin
 * @date	2017年7月18日
 */
public class Excel2007ContentHandler<T extends Worksheet> extends AbstractContentHandler<Worksheet>
{
	/** Table with styles */
	private StylesTable stylesTable;
	/** Table with unique strings */
	private ReadOnlySharedStringsTable sharedStringsTable;
	/** 格式化单元格的值的地址 */
	private short formatIndex;
	private String formatString;
	private DataFormatter formatter;
	/** 当前sheet的行标 */
	private int rowIndex = 0;
	/** 单元格列标 */
	private int columnIndex = -1;
	/** 是否开始解析单元格的值 */
	private boolean isCellDataBegin;
	/** 单元格的值类型 */
	private XSSFDataType cellDataType;
	/** 单元格的值 */
	private StringBuffer value;
	/** 当前行的数据，key：列标，value：单元格值 */
	private Map<Integer, String> dataMap = new TreeMap<>();
	/** 是否是空行 */
	private boolean isEmptyRow = false;
	
	public Excel2007ContentHandler(String subffix, boolean printLog, Log logger, IDataReaderCallback rowReader, ExcelReaderMeta meta, 
			ReadOnlySharedStringsTable sharedStringsTable, StylesTable stylesTable)
	{
		super(subffix, printLog, logger, rowReader, meta);
		this.sharedStringsTable = sharedStringsTable;
		this.stylesTable = stylesTable;
		this.formatter = new DataFormatter();
		this.value = new StringBuffer();
		
		worksheetElementFilters.add("dimension");
		worksheetElementFilters.add("sheetViews");
		worksheetElementFilters.add("sheetData");
		rowElementFilter.add("row");
		rowElementFilter.add("v");
		rowElementFilter.add("t");
		rowElementFilter.add("c");
		rowAttrFilters.add("r");
		cellAttrFilters.add("r");
		cellAttrFilters.add("t");
		
	}
	
	/**
	 * Parses and shows the content of one sheet using the specified styles and shared-strings tables.
	 * @param sheetIndex
	 * @param sheetName
	 * @param sheetInputStream
	 * @throws Exception
	 */
	public HandlerResult<Worksheet> processSheet(int sheetIndex, String sheetName, InputStream sheetInputStream, boolean useTemplate) throws Exception
	{
		super.useTemplate = useTemplate;
		HandlerResult<Worksheet> result = new HandlerResult<>();
		super.beginWorksheet(sheetIndex, sheetName);
		this.rowIndex = 0;
		// 过滤sheet，只解析需要的sheet
		if(super.isSkipSheet())
		{
			if(debug)
			{
				logger.info(String.format("【%s】跳过解析第%s个sheet工作薄【%s】，不满足条件，用时：%s毫秒", OPER_PREFIX, sheetIndex, sheetName, 0));
			}
			result.setCode(CodeEnum.FAILURE_END_SHEET.getCode());
			result.setDesc("不满足条件被过虑");
			return result;
		}
		Worksheet sheet = null;
		try
		{
			if(useTemplate)
			{
				sheet = this.parserTemplate(sheetInputStream);
			}
			else
			{
				parser.get().parse(new InputSource(sheetInputStream));
			}
			result.setCode(CodeEnum.SUCCESS.getCode());
		}
		catch(RuntimeException e)
		{
			String msg = e.getMessage();
			CodeEnum code = CodeEnum.findByValue(msg);
			if(code != null)
			{
				result.setCode(code.getCode());
			}
			else
			{
				throw e;
			}
		}
		finally
		{
			Worksheet sheetTmp = this.endWorksheet();
			if(!useTemplate)
			{
				sheet = sheetTmp;
			}
		}
		if(super.isEndWorkbook())//指定解析sheet已完成，结束解析其它sheet
		{
			result.setCode(CodeEnum.SUCCESS_END_WORKBOOK.getCode());
		}
		result.setData(sheet);
		return result;
	}
	@Override
	/**
	 * 使用共享数据(SharedStringsTable)的格式：
	 * <row r="1" spans="1:5" ht="30" customHeight="1" x14ac:dyDescent="0.15">
	 * <c r="A1" s="2" t="s"><v>0</v></c>
	 * <c r="B1" s="2" t="s"><v>1</v></c>
	 * </row>
	 * 直接使用数据的格式：
	 * <row r="1" spans="1:5" ht="30" customHeight="1" x14ac:dyDescent="0.15">
	 * <c r="A1" s="2" t="inlineStr"><is><t>姓名</t></is></c>
	 * <c r="B1" s="2" t="inlineStr"><is><t>性别</t></is></c>
	 * </row>
	 */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
	{
		if ("v".equals(name) || "t".equals(name))
		{
			isCellDataBegin = true;
			// Clear contents cache
			value.setLength(0);
		}
		// c => cell
		else if ("c".equals(name))
		{
//			value.setLength(0);
			// Get the cell reference
			String r = attributes.getValue("r");
			columnIndex = ExcelHandler.number26ToInt(r);

			// Set up defaults.
			this.cellDataType = XSSFDataType.NUMBER;
			this.formatIndex = -1;
			this.formatString = null;
			String cellType = attributes.getValue("t");
			String cellStyleStr = attributes.getValue("s");
			if ("b".equals(cellType))
				cellDataType = XSSFDataType.BOOL;
			else if ("e".equals(cellType))
				cellDataType = XSSFDataType.ERROR;
			else if ("inlineStr".equals(cellType))
				cellDataType = XSSFDataType.INLINESTR;
			else if ("s".equals(cellType))
				cellDataType = XSSFDataType.SSTINDEX;
			else if ("str".equals(cellType))
				cellDataType = XSSFDataType.FORMULA;
			else if (cellStyleStr != null)
			{
				// It's a number Or empty, but almost certainly one
				// with a special style or format
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
				this.formatIndex = style.getDataFormat();
				this.formatString = style.getDataFormatString();
				if (this.formatString == null)
					this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
			}
		}
		else if("row".equals(name))
		{
			rowIndex++;
			isEmptyRow = true;
		}

	}
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{
		String thisStr = null;
		// v => contents of a cell
		if ("v".equals(name) || "t".equals(name))
		{
			// Process the value contents as required.
			// Do now, as characters() may be called more than once
			switch (cellDataType)
			{
			case BOOL:
				char first = value.charAt(0);
				thisStr = first == '0' ? "FALSE" : "TRUE";
				break;
			case ERROR:
				thisStr = "\"ERROR:" + value.toString() + '"';
				break;
			case FORMULA:
				// A formula could result in a string value, so always add double-quote characters.
				thisStr = '"' + value.toString() + '"';
				break;
			case INLINESTR:
				//have seen an example of this, so it's untested.
				thisStr = value.toString();
				//XSSFRichTextString 使用该类，总耗时将是不使用的3倍左右
//				XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
//				if (rtsi != null)
//				{
//					thisStr = rtsi.toString().trim().replaceAll("\\s*", "");
//				}
				break;
			case SSTINDEX:
				String sstIndex = value.toString();
				try
				{
					int idx = Integer.parseInt(sstIndex);
					thisStr = StringUtils.trim(sharedStringsTable.getEntryAt(idx));
//					XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
//					if (rtss != null)
//					{
//						thisStr = rtss.toString().trim().replaceAll("\\s*", "");
//					}
				}
				catch (NumberFormatException ex)
				{
					logger.error("Failed to parse SST index '" + sstIndex + "': " + ex.toString(), ex);
				}
				break;
			case NUMBER:
				String n = value.toString();
				if (this.formatString != null)
					thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
				else
					thisStr = n;
				break;
			default:
				thisStr = "(TODO: Unexpected type: " + cellDataType + ")";
				break;
			}
			dataMap.put(columnIndex, StringUtils.trim(thisStr));
			isCellDataBegin = false;
		}
		// c => cell
		else if ("c".equals(name))//单元格结束时，判断是否有内容，保证每行都有相同数量的单元格
		{
			if(value.length() == 0)
			{
				dataMap.put(columnIndex, "");
			}
			else
			{
				isEmptyRow = false;
			}
		}
		else if ("row".equals(name))
		{
			if(!isEmptyRow)
			{
				super.addRowDataWithException(rowIndex, dataMap.values().toArray(new String[dataMap.size()]));
				isEmptyRow = true;
			}
			dataMap.clear();
		}
	}
	
	/**
	 * Captures characters only if a suitable element is open. Originally was just "v"; extended for inlineStr also.
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (isCellDataBegin)
			value.append(ch, start, length);
	}
	
	@Override
	public void skipCurrSheet()
	{
		super.skipCurrSheet();
		throw new RuntimeException(CodeEnum.SUCCESS_END_SHEET.getCode());
	}
	
	@SuppressWarnings("unchecked")
	private Worksheet parserTemplate(InputStream inputStream) throws Exception
	{
		Worksheet worksheet = new Worksheet();
		SAXReader reader = new SAXReader();  
		Document document = reader.read(inputStream);
		//获取根节点元素对象
        Element root = document.getRootElement();
        Iterator<Attribute> it = root.attributeIterator();
        while(it.hasNext())
        {
        	Attribute attr = it.next();
        	worksheet.getAttributes().put(attr.getName(), attr.getText());
        }
        int totalRowCount = 0;
        int rowIndex = 0;
        String title = "";
        int columnCount = 0;
        List<String> columnList = new ArrayList<>();
        boolean isHeader = true;
        List<?> list = root.content();
        for(Object obj : list)
        {
        	if(obj instanceof Namespace)
        	{
        		Namespace ns = (Namespace)obj;
        		String prefix = ns.getPrefix();
        		String text = ns.getURI();
        		String nsName = "xmlns";
        		if(StringUtils.isNotBlank(prefix))
        		{
        			nsName = nsName + ":" + prefix;
        		}
        		worksheet.getAttributes().put(nsName, text);
        	}
        	else if(obj instanceof DefaultElement)
        	{
        		DefaultElement element = (DefaultElement)obj;
        		String name = element.getQualifiedName();
        		String text = element.asXML();
        		text = text.replaceAll("[\\s]*xmlns[^=]*=[^\"]*\"[^\"]*\"", "");
        		if(name.equalsIgnoreCase("sheetData"))
        		{
        			isHeader = false;
        			String standalone = element.attributeValue("standalone");
        			boolean standaloneTemplate = true;// 是否是标准模板（是：行高数据单位转换，磅-->像素，否：不转换）
        			if(StringUtils.isNotBlank(standalone) && standalone.equalsIgnoreCase("no"))
        			{
        				standaloneTemplate = false;
        			}
        	        List<Element> rows = element.elements("row");
        	        for(Element row : rows)
        	        {
        	        	rowIndex++;
        	        	totalRowCount++;
        	        	RowTypeEnum rowType = super.getRowType(rowIndex);
        	        	if(rowType == null)
        	        	{
        	        		continue;
        	        	}
            			RowTemplate rowTemplate = new RowTemplate();
        	        	rowTemplate.setAttributes(this.convertMap(row.attributes(), super.rowAttrFilters));
        	        	if(standaloneTemplate)
    					{
    						try
    						{
    							//将像素与磅转换，获取的值是像素，但设置值时为磅
    							String attrText = rowTemplate.getAttributes().get("ht");
    							//英文字体的1磅，相当于1/72 英寸，约等于1/2.8mm，屏幕96dpi
								//5磅(7px) ==(5/72)*96=6.67 =6px
								attrText = String.valueOf((int)Math.floor(Double.parseDouble(attrText) * 72 / 96));
								rowTemplate.getAttributes().put("ht", attrText);
    						}catch(Exception e){}
    					}
        	        	Map<Integer, CellTemplate> cellTemplates = new HashMap<>();
        	        	List<Element> cells = row.elements("c");
        	        	int cellIndex = 0;
        	        	String[] values = new String[cells.size()];
        	        	for(Element cell : cells)
        	        	{
        	        		cellIndex++;
        	        		CellTemplate cellTemplate = new CellTemplate();
        	        		cellTemplate.setAttributes(this.convertMap(cell.attributes(), super.cellAttrFilters));
        	        		cellTemplate.setText(this.getCellValue(cell));
        	        		cellTemplates.put(cellIndex, cellTemplate);
        	        		values[cellIndex - 1] = cellTemplate.getText();
        	        	}
        	        	rowTemplate.setCellTemplates(cellTemplates);
            			rowTemplate.setRowType(rowType.getCode());
            			columnCount = Math.max(columnCount, values.length);
            			if(rowType == RowTypeEnum.TITLE)
            			{
            				worksheet.setTitleTemplate(rowTemplate);
            				title = values.length > 0 ? values[0] : "";
            			}
            			else if(rowType == RowTypeEnum.COLUMN)
            			{
            				worksheet.setColumnTemplate(rowTemplate);
            				columnList.addAll(Arrays.asList(values));
            			}
            			else if(rowType == RowTypeEnum.DATA)
            			{
            				worksheet.setDataTemplate(rowTemplate);
            				break;
            			}
        	        }
        	        worksheet.setRowCount(rowIndex);
        		}
        		else
        		{
        			if(super.worksheetElementFilters.contains(name))
    				{
            			continue;
    				}
        			if(isHeader)
        			{
        				worksheet.getHeaderDefaultElements().add(text);
        			}
        			else
        			{
        				worksheet.getFooterDefaultElements().add(text);
        			}
        		}
        	}
        }
        worksheet.setRowCount(totalRowCount);
        worksheet.setSheetIndex(super.sheetIndex);
        worksheet.setSheetName(super.sheetName);
        worksheet.setColumnCount(columnCount);
		if(columnList != null && columnList.size() > 0)
		{
			worksheet.setColumns(columnList);
		}
		worksheet.setTitle(title);
		super.rowCount = totalRowCount;
		return worksheet;
	}
	
	private String getCellValue(Element cell)
	{
		String thisStr = "";
		// Set up defaults.
		String cellType = cell.attributeValue("t");
		String cellStyleStr = cell.attributeValue("s");
		String text = this.getElementValueMaxDepth(cell);
		if(StringUtils.isBlank(cellType) && StringUtils.isBlank(text))
		{
			return thisStr;
		}
		else if ("b".equals(cellType))
		{
			thisStr = text.equals("0") ? "FALSE" : "TRUE";
		}
		else if ("e".equals(cellType))
		{
			thisStr = "\"ERROR:" + text + '"';
		}
		else if ("inlineStr".equals(cellType))//have seen an example of this, so it's untested.
		{
			XSSFRichTextString rtsi = new XSSFRichTextString(text);
			if (rtsi != null)
			{
				thisStr = rtsi.toString().trim().replaceAll("\\s*", "");
			}
		}
		else if ("s".equals(cellType))//use share table index
		{
			String sstIndex = text;
			try
			{
				int idx = Integer.parseInt(sstIndex);
				XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
				if (rtss != null)
				{
					thisStr = rtss.toString().trim().replaceAll("\\s*", "");
				}
			}
			catch (NumberFormatException ex)
			{
				logger.error("Failed to parse SST index '" + sstIndex + "': " + ex.toString(), ex);
			}
		}
		else if ("str".equals(cellType))//A formula could result in a string value, so always add double-quote characters.
		{
			thisStr = '"' + text + '"';
		}
		else if (cellStyleStr != null)
		{
			// It's a number Or empty, but almost certainly one
			// with a special style or format
			int styleIndex = Integer.parseInt(cellStyleStr);
			XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
			int formatIndex = style.getDataFormat();
			String formatString = style.getDataFormatString();
			if (formatString == null)
				formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
			String n = text;
			if (formatString != null)
				thisStr = formatter.formatRawCellContents(Double.parseDouble(n), formatIndex, formatString);
			else
				thisStr = n;
		}
		return thisStr;
	}
	
	@SuppressWarnings("unchecked")
	private String getElementValueMaxDepth(Element cell)
	{
		List<Element> list = cell.elements();
		if(list != null && list.size() > 0)
		{
			for(Element element : list)
			{
				String name = element.getName();
				if("v".equals(name) || "t".equals(name))
				{
					return element.getTextTrim();
				}
				else
				{
					String value = getElementValueMaxDepth(element);
					if(StringUtils.isNotBlank(value))
					{
						return value;
					}
				}
			}
		}
		return "";
	}
	
}
