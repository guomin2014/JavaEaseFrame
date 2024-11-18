package com.gm.javaeaseframe.common.util.excel.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
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
import com.gm.javaeaseframe.common.util.excel.common.Workbook;
import com.gm.javaeaseframe.common.util.excel.common.Worksheet;
import com.gm.javaeaseframe.common.util.excel.common.XSSFDataType;
/**
 * Excel2003内容解析器
 * @author	GuoMin
 * @date	2017年7月18日
 */
public class Excel2003ContentHandler<T extends Workbook> extends AbstractContentHandler<Workbook>
{
	/** 所有sheet的集合 */
	private List<Worksheet> sheetList = new ArrayList<>();
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
	
	private Workbook workbook;
	
	public Excel2003ContentHandler(String prefix, boolean printLog, Log logger, IDataReaderCallback rowReader, ExcelReaderMeta meta)
	{
		super(prefix, printLog, logger, rowReader, meta);
		this.value = new StringBuffer();
		rowElementFilter.add("Row");
		rowElementFilter.add("Cell");
		rowElementFilter.add("Data");
		rowAttrFilters.add("r");
		cellAttrFilters.add("r");
		cellAttrFilters.add("t");
	}
	
	@Override
	public HandlerResult<Workbook> processSheet(int sheetIndex, String sheetName, InputStream sheetInputStream, boolean useTemplate) throws Exception
	{
		super.sheetIndexEnable = sheetIndex;
		super.useTemplate = useTemplate;
		HandlerResult<Workbook> result = new HandlerResult<Workbook>();
		try
		{
			if(useTemplate)//如果是解析模板，直接使用dom4j
			{
				workbook = this.parserTemplate(sheetInputStream);
			}
			else
			{
				workbook = new Workbook();
				parser.get().parse(new InputSource(sheetInputStream));
				workbook.setSheetList(sheetList);
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
		workbook.setTotalRowCount(super.totalRowCount);
		result.setData(workbook);
		return result;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
	{
		if(super.isSkipSheet() && !"Worksheet".equalsIgnoreCase(name))
		{
			return;
		}
		if("Worksheet".equalsIgnoreCase(name))//开始一个sheet
		{
			super.beginWorksheet(attributes.getValue("ss:Name"));
			this.rowIndex = 0;
		}
		if ("Data".equalsIgnoreCase(name))
		{
			isCellDataBegin = true;
			// Clear contents cache
			value.setLength(0);
			// Set up defaults.
			this.cellDataType = XSSFDataType.INLINESTR;
		}
		// c => cell
		else if ("Cell".equalsIgnoreCase(name))
		{
			this.columnIndex++;
		}
		else if("Row".equalsIgnoreCase(name))
		{
			rowIndex++;
			columnIndex = -1;
		}

	}
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{
		if(super.isSkipSheet() && !"Worksheet".equalsIgnoreCase(name))
		{
			return;
		}
		String thisStr = null;
		// v => contents of a cell
		if ("Data".equalsIgnoreCase(name))
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
				thisStr = value.toString().trim();
				break;
			case NUMBER:
				thisStr = value.toString();
				break;
			default:
				thisStr = "(TODO: Unexpected type: " + cellDataType + ")";
				break;
			}
			dataMap.put(columnIndex, StringUtils.trim(thisStr));
			isCellDataBegin = false;
		}
		// c => cell
		else if ("Cell".equalsIgnoreCase(name))//单元格结束时，判断是否有内容，保证每行都有相同数量的单元格
		{
			if(value.length() == 0)
			{
				dataMap.put(columnIndex, StringUtils.trim(thisStr));
			}
		}
		else if ("Row".equalsIgnoreCase(name))
		{
			super.addRowDataWithException(rowIndex, dataMap.values().toArray(new String[dataMap.size()]));
		}
		else if("Worksheet".equalsIgnoreCase(name))//sheet结束
		{
			Worksheet sheet = super.endWorksheet();
			if(sheet != null)
			{
				this.sheetList.add(sheet);
			}
			if(super.isEndWorkbook())
			{
				throw new RuntimeException(CodeEnum.SUCCESS_END_WORKBOOK.getCode());
			}
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
	}

	@SuppressWarnings("unchecked")
	private Workbook parserTemplate(InputStream inputStream) throws Exception
	{
		Workbook workbook = new Workbook();
		SAXReader reader = new SAXReader();  
		Document document = reader.read(inputStream);
		//获取根节点元素对象
        Element root = document.getRootElement();
        Iterator<Attribute> it = root.attributeIterator();
        while(it.hasNext())
        {
        	Attribute attr = it.next();
        	workbook.getAttributes().put(attr.getName(), attr.getText());
        }
        int totalRowCount = 0;
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
        		workbook.getAttributes().put(nsName, text);
        	}
        	else if(obj instanceof DefaultElement)
        	{
        		DefaultElement element = (DefaultElement)obj;
        		String name = element.getQualifiedName();
        		String text = element.asXML();
        		text = text.replaceAll("[\\s]*xmlns[^=]*=[^\"]*\"[^\"]*\"", "");
        		if(name.equalsIgnoreCase("DocumentProperties"))//文档属性，作者，创建时间等，需要动态设置
        		{
        			continue;
        		}
        		else if(name.equalsIgnoreCase("Worksheet"))
        		{
        			isHeader = false;
        			Worksheet sheet = new Worksheet();
        			Iterator<Attribute> sheetAttrs = element.attributeIterator();
        	        while(sheetAttrs.hasNext())
        	        {
        	        	Attribute attr = sheetAttrs.next();
        	        	String attrName = attr.getQualifiedName();
        	        	String attrText = attr.getText();
        	        	if(StringUtils.isBlank(attrName))
        	        	{
        	        		continue;
        	        	}
        	        	if(attrName.equalsIgnoreCase("ss:Name"))
        	        	{
        	        		sheet.setSheetName(attrText);
        	        		sheet.setSheetIndex(workbook.getSheetCount() + 1);
        	        		continue;
        	        	}
        	        	sheet.getAttributes().put(attr.getName(), attr.getText());
        	        }
        	        Element table = element.element("Table");
        	        StringBuffer tableSB = new StringBuffer();
        	        tableSB.append("<Table");
        	        Iterator<Attribute> tableAttrs = table.attributeIterator();
        	        while(tableAttrs.hasNext())
        	        {
        	        	Attribute attr = tableAttrs.next();
        	        	String attrName = attr.getQualifiedName();
        	        	String attrText = StringUtils.trim(attr.getText());
        	        	if(StringUtils.isBlank(attrName))
        	        	{
        	        		continue;
        	        	}
        	        	if(attrName.equalsIgnoreCase("ss:ExpandedColumnCount"))
        	        	{
        	        		attrText = "256";//设置最大列数
        	        	}
        	        	else if(attrName.equalsIgnoreCase("ss:ExpandedRowCount"))
        	        	{
        	        		attrText = "65535";//设置最大行数
        	        	}
        	        	tableSB.append(" ").append(attrName).append("=").append("\"" + attrText + "\"");
        	        }
        	        tableSB.append(">");
        	        sheet.getHeaderDefaultElements().add(tableSB.toString());
        	        boolean isSheetHeader = true;
        	        int rowIndex = 0;
        	        List<?> childList = table.content();
        	        for(Object ele : childList)
        	        {
        	        	if(ele instanceof DefaultElement)
        	        	{
        	        		DefaultElement childElement = (DefaultElement)ele;
        	        		String childName = childElement.getQualifiedName();
        	        		String childText = childElement.asXML();
        	        		childText = childText.replaceAll("[\\s]*xmlns[^=]*=[^\"]*\"[^\"]*\"", "");
        	        		if(childName.equalsIgnoreCase("Row"))
        	        		{
        	        			isSheetHeader = false;
        	        			rowIndex++;
                	        	totalRowCount++;
                	        	//行标从1开始
                	        	RowTypeEnum rowType = super.getRowType(rowIndex);
                    			if(rowType == null)
                    			{
                    				continue;
                    			}
                    			RowTemplate rowTemplate = new RowTemplate();
                	        	rowTemplate.setAttributes(this.convertMap(childElement.attributes()));
                	        	Map<Integer, CellTemplate> cellTemplates = new HashMap<>();
                	        	List<Element> cells = childElement.elements("Cell");
                	        	int cellIndex = 0;
                	        	for(Element cell : cells)
                	        	{
                	        		cellIndex++;
                	        		CellTemplate cellTemplate = new CellTemplate();
                	        		cellTemplate.setAttributes(this.convertMap(childElement.attributes()));
                	        		cellTemplate.setText(cell.getTextTrim());
                	        		cellTemplates.put(cellIndex, cellTemplate);
                	        	}
                	        	rowTemplate.setCellTemplates(cellTemplates);
                    			rowTemplate.setRowType(rowType.getCode());
                    			if(rowType == RowTypeEnum.TITLE)
                    			{
                    				sheet.setTitleTemplate(rowTemplate);
                    			}
                    			else if(rowType == RowTypeEnum.COLUMN)
                    			{
                    				sheet.setColumnTemplate(rowTemplate);
                    			}
                    			else if(rowType == RowTypeEnum.DATA)
                    			{
                    				sheet.setDataTemplate(rowTemplate);
                    				break;
                    			}
        	        		}
        	        		else
        	        		{
        	        			if(isSheetHeader)
        	        			{
        	        				sheet.getHeaderDefaultElements().add(childText);
        	        			}
        	        			else
        	        			{
        	        				sheet.getFooterDefaultElements().add(childText);
        	        			}
        	        		}
        	        	}
        	        }
        	        sheet.getFooterDefaultElements().add("</Table>");
        	        sheet.setRowCount(rowIndex);
        	        if(workbook.getSheetList() == null)
        	        {
        	        	List<Worksheet> sheetList = new ArrayList<>();
        	        	sheetList.add(sheet);
        	        	workbook.setSheetList(sheetList);
        	        }
        	        else
        	        {
        	        	workbook.getSheetList().add(sheet);
        	        }
        		}
        		else
        		{
        			if(isHeader)
        			{
        				workbook.getHeaderDefaultElements().add(text);
        			}
        			else
        			{
        				workbook.getFooterDefaultElements().add(text);
        			}
        		}
        	}
        }
        super.totalRowCount = totalRowCount;
        workbook.setTotalRowCount(totalRowCount);
        return workbook;
	}
}
