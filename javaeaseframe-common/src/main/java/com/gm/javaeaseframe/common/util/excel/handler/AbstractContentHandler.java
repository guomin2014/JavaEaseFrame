package com.gm.javaeaseframe.common.util.excel.handler;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.HandlerResult;

public abstract class AbstractContentHandler<T> extends ReaderHandler implements ContentHandler
{
	/** 是否获取模板信息 */
	protected boolean useTemplate = false;
	/** 模板信息--需要过滤的默认元素 */
	protected Set<String> worksheetElementFilters = new HashSet<>();
	/** 模板信息--需要过滤的行属性 */
	protected Set<String> rowAttrFilters = new HashSet<>();
	/** 行过滤元素 */
	protected Set<String> rowElementFilter = new HashSet<>();
	/** 模板信息--需要过滤的列属性 */
	protected Set<String> cellAttrFilters = new HashSet<>();
	/** 将parser作为线程变量 */  
	protected ThreadLocal<XMLReader> parser = new ThreadLocal<XMLReader>();
	
	public AbstractContentHandler(String prefix, boolean printLog, Log logger, IDataReaderCallback rowReader, ExcelReaderMeta meta)
	{
		super(prefix, printLog, logger, rowReader, meta);
		try
		{
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			saxFactory.setNamespaceAware(true);
			SAXParser saxParser = saxFactory.newSAXParser();
			XMLReader sheetParser = saxParser.getXMLReader();
			sheetParser.setContentHandler(this);
			parser.set(sheetParser);
		}
		catch (Exception e)
		{
			if(debug)
			{
				logger.error("初始化XML解析器异常", e);
			}
			throw new RuntimeException(e);
		}
		
	}
	
	public void setDocumentLocator (Locator locator)
    {
        // no op
    }
    public void startDocument () throws SAXException
    {
        // no op
    }
    public void endDocument () throws SAXException
    {
        // no op
    }
    public void startPrefixMapping (String prefix, String uri) throws SAXException
    {
        // no op
    }
    public void endPrefixMapping (String prefix) throws SAXException
    {
        // no op
    }
    public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        // no op
    }
    public void endElement (String uri, String localName, String qName) throws SAXException
    {
        // no op
    }
    public void characters (char ch[], int start, int length) throws SAXException
    {
        // no op
    }
    public void ignorableWhitespace (char ch[], int start, int length) throws SAXException
    {
        // no op
    }
    public void processingInstruction (String target, String data) throws SAXException
    {
        // no op
    }
    public void skippedEntity (String name) throws SAXException
    {
        // no op
    }
    
	/**
	 * 解析sheet
	 * @param sheetIndex		sheet的序号
	 * @param sheetName			sheet的名称
	 * @param sheetInputStream	sheet的数据流
	 * @param useTemplate		是否解析成模板
	 * @return
	 * @throws Exception
	 */
	public abstract HandlerResult<T> processSheet(int sheetIndex, String sheetName, InputStream sheetInputStream, boolean useTemplate) throws Exception;
}
