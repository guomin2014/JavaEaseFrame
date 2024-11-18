package com.gm.javaeaseframe.common.util.excel.writer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gm.javaeaseframe.common.util.excel.IDataWriterCallback;
import com.gm.javaeaseframe.common.util.excel.common.CellTemplate;
import com.gm.javaeaseframe.common.util.excel.common.ExcelWriterMeta;
import com.gm.javaeaseframe.common.util.excel.common.RowTemplate;
import com.gm.javaeaseframe.common.util.excel.common.RowTypeEnum;
import com.gm.javaeaseframe.common.util.excel.common.WorkbookTemplate;
import com.gm.javaeaseframe.common.util.excel.common.WorksheetTemplate;
import com.gm.javaeaseframe.common.util.excel.common.XMLEncoder;
import com.gm.javaeaseframe.common.util.excel.handler.ExcelHandler;

/**
 * excel2007读入器，先构建.xlsx一张模板，改写模板中的sheet.xml,使用这种方法写入.xlsx文件，不需要太大的内存
 * 原生 org.apache.poi.xssf.streaming.SXSSFWorkbook 也支持大数据写入
 * @author	GuoMin
 * @date	2017年7月15日
 */
public class Excel2007Writer extends AbstractExcelWriter
{
	private final String TEMPLATE_FILE_NAME = "template.xlsx";
	/** 数据映射文件，key：sheet的名称，value：sheet的data文件 */
	private Map<String, File> tmpDataMap = new LinkedHashMap<>();
	/** 是否开始sheet */
	private boolean isOpenSheet = false;
	/** 当前行记录 */
	private StringBuffer rows = new StringBuffer();
	/** 当前总行数 */
	private AtomicInteger rowTotalCount = new AtomicInteger();
	/** 每次最大写入行数 */
	private int maxCountPreWriter = 1000;
	
	public Excel2007Writer(boolean printLog, IDataWriterCallback writer, ExcelWriterMeta meta)
	{
		super(printLog, writer, meta);
		if(meta.getMaxRowPreSheet() <= 0)
		{
			meta.setMaxRowPreSheet(1048570);
		}
	}
	
    @Override
	public Writer beginWorkbook(Writer writer, String filePath, WorkbookTemplate workbookTemplate) throws Exception
	{
		return writer;
	}
	@Override
	public void endWorkbook(Writer writer, WorkbookTemplate workbookTemplate) throws Exception
	{
		if(writer != null)
		{
			writer.flush();
		}
		this.saveFile();
	}
	@Override
	public Writer beginWorksheet(Writer writer, String sheetName, WorksheetTemplate worksheetTemplate) throws Exception
	{
		File tmp = File.createTempFile("sheet", ".xml");  
		writer =  new PrintWriter(new BufferedOutputStream(new FileOutputStream(tmp)));
        tmpDataMap.put(sheetName, tmp);
		StringBuffer sheet = new StringBuffer();
    	sheet.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
    	sheet.append("\r\n");
    	sheet.append("<worksheet");
    	if(worksheetTemplate != null)
    	{
    		Map<String, String> attributes = worksheetTemplate.getAttributes();
    		if(attributes != null)
    		{
    			for(Map.Entry<String, String> entry : attributes.entrySet())
    			{
    				sheet.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
    			}
    		}
    	}
    	else
    	{
    		sheet.append(" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"");
    		sheet.append(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"");
        	sheet.append(" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\"");
        	sheet.append(" xmlns:x14ac=\"http://schemas.microsoft.com/office/spreadsheetml/2009/9/ac\"");
    	}
    	sheet.append(">");
    	if(worksheetTemplate != null)
    	{
    		List<String> attributes = worksheetTemplate.getHeaderDefaultElements();
    		if(attributes != null)
    		{
    			for(String value : attributes)
    			{
    				sheet.append(StringUtils.trim(value));
    			}
    		}
    	}
    	else
    	{
    		sheet.append("<sheetFormatPr baseColWidth=\"10\" defaultRowHeight=\"18\" x14ac:dyDescent=\"0.15\"/>");
    	}
    	sheet.append("<sheetData standalone=\"no\">");
    	writer.write(sheet.toString());
    	isOpenSheet = true;
		return writer;
	}
	@Override
	public void endWorksheet(Writer writer, WorksheetTemplate worksheetTemplate) throws Exception
	{
		if(isOpenSheet && writer != null)
    	{
			//检查是否有未写入的记录
			int count = rowTotalCount.get();
	    	if(count > 0)
	    	{
	    		writer.write(rows.toString());
	    		rows.setLength(0);
	    	}
	    	
	    	StringBuffer sheet = new StringBuffer();
	    	sheet.append("</sheetData>");
	    	if(worksheetTemplate != null)
	    	{
	    		List<String> attributes = worksheetTemplate.getFooterDefaultElements();
	    		if(attributes != null)
	    		{
	    			for(String value : attributes)
	    			{
	    				sheet.append(StringUtils.trim(value));
	    			}
	    		}
	    	}
	    	else
	    	{
	    		if(super.templateIncloudTitleRow)//有标题栏
		        {
		    		if(columnNum >= 2)//多列，需要合并
		    		{
		    			String columnName = ExcelHandler.getColumnName(columnNum);
		    			String mergeCell = "A1:" + columnName + "1";
		    			sheet.append("<mergeCells count=\"1\"><mergeCell ref=\"" + mergeCell + "\"/></mergeCells>");
		    		}
		        }
	    		sheet.append("<pageMargins left=\"0.75\" right=\"0.75\" top=\"0.5\" bottom=\"0.5\" header=\"0.5\" footer=\"0.5\"/>");	
//    			sheet.append("<pageSetup orientation=\"portrait\" horizontalDpi=\"300\" verticalDpi=\"300\" />");
	    	}
	    	sheet.append("</worksheet>");
	    	writer.write(sheet.toString());
	    	isOpenSheet = false;
	    	writer.flush();
			writer.close();
			writer = null;
    	}
	}
	
	public void insertRow(Writer writer, int rowNum, String[] columns, RowTemplate rowTemplate) throws IOException
    {
    	StringBuffer row = new StringBuffer();
    	row.append("<row r=\"" + rowNum + "\"");
    	if(rowTemplate != null)
    	{
    		row.append(rowTemplate.attrsToStr());
    	}
    	row.append(">");
    	int columnCount = columns.length;
    	Map<Integer, CellTemplate> cellMap = null;
    	if(rowTemplate != null)
    	{
    		cellMap = rowTemplate.getCellTemplates();
			if(cellMap == null)
			{
				return;
			}
			if(rowTemplate.getRowType() == RowTypeEnum.TITLE.getCode() || rowTemplate.getRowType() == RowTypeEnum.COLUMN.getCode())
			{
	    		if(columns == null || columns.length == 0)
	    		{
	    			columnCount = rowTemplate.getColumnCount();
					columns = new String[columnCount];
					for(int index = 0; index < columnCount; index++)
		    		{
						CellTemplate cellTemplate = cellMap.get(index + 1);
						columns[index] = cellTemplate == null ? "" : cellTemplate.getText();
		    		}
	    		}
			}
    	}
    	for(int columnIndex = 0; columnIndex < columnCount; columnIndex++)
    	{
//	    	String ref = new CellReference(rowNum - 1, columnIndex).formatAsString();//单元格下标从0开始
//	    	ref = ref.replace("$", "");
    		String ref = ExcelHandler.getColumnName(columnIndex + 1) + rowNum;
	    	String text = columns[columnIndex];
	    	CellTemplate cell = cellMap == null ? null : cellMap.get(columnIndex + 1);
	    	row.append("<c r=\"" + ref + "\"" + (cell != null ? cell.attrsToStr() : "") + " t=\"inlineStr\">");
	        row.append("<is><t>"+XMLEncoder.encode(text)+"</t></is>");
//	    	row.append("<is><t>" + XMLEncoder.encodeXML(text) + "</t></is>");
//	        row.append("<v>" + value + "</v>");//number数值
//	        row.append("<v>" + org.apache.poi.ss.usermodel.DateUtil.DateUtil.getExcelDate(value, false) + "</v>");//Calendar数值
	        row.append("</c>");
    	}
    	row.append("</row>");
		rows.append(row);
    	int count = rowTotalCount.incrementAndGet();
    	if(count >= maxCountPreWriter)
    	{
    		writer.write(rows.toString());
    		rows.setLength(0);
    	}
    }
	
	private void saveFile() throws Exception
    {
    	File templateFile = null;
		try
		{
	        if(debug)
			{
				logger.info(String.format("【%s】开始创建Excel临时模板文件", OPER_PREFIX));
			}
	        //key：sheet的引用地址，value：数据文件
	        Map<String, File> tmpMap = new HashMap<>();
			 // 建立工作簿和电子表格对象
	        XSSFWorkbook wb = null;
	        InputStream is = null;
	        if(super.useTemplate)
	        {
	        	is = new BufferedInputStream(new FileInputStream(meta.getTemplateFilePath()));
	        	wb = new XSSFWorkbook(is);
	        	//已存在的sheet只是当模板使用，故删除
	        	int sheetCount = wb.getNumberOfSheets();
		        while(sheetCount > 0)
		        {
		        	wb.removeSheetAt(0);
		        	sheetCount--;
		        }
	        }
	        else
	        {
	        	wb = new XSSFWorkbook();
	        }
	        org.apache.poi.POIXMLProperties.CoreProperties prop = wb.getProperties().getCoreProperties();
	        prop.setCreator(meta.getCreateUser());
	        prop.setCreated(ExcelHandler.getCurrGMTDateTime());
//	        prop.setTitle(sheetName);
	        for(Map.Entry<String, File> entry : tmpDataMap.entrySet())
	        {
	        	String name = entry.getKey();
	        	File tmpFile = entry.getValue();
	        	XSSFSheet sheet = wb.createSheet(name);
	        	
	        	// 持有电子表格数据的xml文件名 例如 /xl/worksheets/sheet1.xml
	        	String sheetRef = sheet.getPackagePart().getPartName().getName();
	        	tmpMap.put(sheetRef.substring(1), tmpFile);
	        }
	        OutputStream os = null;
	        try
	        {
		        // 保存模板
		        os = new BufferedOutputStream(new FileOutputStream(TEMPLATE_FILE_NAME));  
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
	        	if(is != null)
	        	{
	        		try
	        		{
	        			is.close();
	        		}catch(Exception e){}
	        	}
	        }
	        if(debug)
			{
				logger.info(String.format("【%s】开始将临时XML数据文件通过模板文件更新到输出文件", OPER_PREFIX));
			}
	        // 创建ZIP输出流，将xml数据临时文件数据写入到ZIP文件中
	        templateFile = new File(TEMPLATE_FILE_NAME);
	        FileOutputStream out = null;
	        try
	        {
		        out = new FileOutputStream(meta.getFilePath());
		        substitute(templateFile, tmpMap, out);
	        }
	        finally
			{
				if(out != null)
				{
					try
					{
						out.close();
					}catch(Exception e){}
				}
			}
		}
		finally
		{
			if(debug)
			{
				logger.info(String.format("【%s】开始清理Excel临时模板文件", OPER_PREFIX));
			}
			// 删除临时模板文件
			if(templateFile != null && templateFile.isFile() && templateFile.exists())
			{
				templateFile.delete();
			}
			for(Map.Entry<String, File> entry : tmpDataMap.entrySet())
	        {
				entry.getValue().delete();
	        }
			tmpDataMap.clear();
		}
    }
    
    /** 
     * 
     * @param zipfile the template file 
     * @param tmpMap	
     * 					key：the name of the sheet entry to substitute, e.g. xl/worksheets/sheet1.xml
     * 					value：the XML file with the sheet data
     * @param out the stream to write the result to 
     */  
    private void substitute(File zipfile, Map<String, File> tmpMap, OutputStream out) throws IOException {  
    	ZipFile zip = null;  
    	ZipOutputStream zos = null;
    	try
    	{
    		zip = new ZipFile(zipfile);
    		zos = new ZipOutputStream(out);
    		@SuppressWarnings("unchecked")  
    		Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
    		while (en.hasMoreElements()) {
    			ZipEntry ze = en.nextElement();
    			String name = ze.getName();
    			InputStream is = null;
    			try
				{
    				if(tmpMap.containsKey(name))
        			{
        				File tmpfile = tmpMap.get(name);
    		    		is = new FileInputStream(tmpfile);
        			}
        			else
        			{
        				is = zip.getInputStream(ze);
        			}
        			zos.putNextEntry(new ZipEntry(name));
        			if(debug)
        			{
        				logger.info(String.format("【%s】开始迁移sheet的XML临时数据-->%s，总大小：%s byte", OPER_PREFIX, name, is.available()));
        			}
					copyStream(is, zos);
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
    		}
    	}
    	finally
    	{
    		if(zos != null)
    		{
    			try
    			{
        			zos.close();
    			}catch(Exception e){}
    		}
    		if(zip != null)
    		{
    			try
    			{
    				zip.close();
    			}catch(Exception e){}
    		}
    	}
    }
  
    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] chunk = new byte[1024];  
        int count;  
        while ((count = in.read(chunk)) >= 0) {  
            out.write(chunk, 0, count);
        }
        //使用NIO中的管道到管道传输
//        int length = 2097152;//每次最大2M
//        java.nio.channels.FileChannel inC = in.getChannel();
//        java.nio.channels.FileChannel outC = out.getChannel();
//        int i=0;
//        while(true){
//            if(inC.position()==inC.size()){
//                inC.close();
//                outC.close();
//            }
//            if((inC.size()-inC.position())<20971520)
//                length=(int)(inC.size()-inC.position());
//            else
//                length=20971520;
//            inC.transferTo(inC.position(),length,outC);
//            inC.position(inC.position()+length);
//            i++;
//        }
    }
    /**
     * 原生方式创建
     * @throws Exception
     */
    public void process_old() throws Exception
    {
    	int rowIndex = 0;
        String filePath = meta.getFilePath();
		String title = meta.getTitle();
		String sheetName = meta.getSheetName();
		String[] columnNames = meta.getColumnName();
        List<String> columns = new ArrayList<String>();
		if(columnNames != null && columnNames.length > 0)
		{
			columns.addAll(Arrays.asList(columnNames));
		}
        FileOutputStream out = null;
        SXSSFWorkbook workbook = null;
        try
        {
        	workbook = new SXSSFWorkbook(-1); // turn off auto-flushing and accumulate all rows in memory
        	workbook.setCompressTempFiles(true); // temp files will be gzipped
            // 生成xml文件  
            Sheet sheet = workbook.createSheet(sheetName);
        	if(StringUtils.isNotBlank(title))
            {
            	//插入新行
        		org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
        		Cell cell = row.createCell(0);
        		cell.setCellValue(title);
            }
            if(!columns.isEmpty())
            {
            	//插入新行
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                for(int n = 0; n < columns.size(); n++)
                {
                	Cell cell = row.createCell(n);
            		cell.setCellValue(columns.get(n));
                }
            }
            if(rowIndex % 1000 == 0)//每1000行保存一次
            {
            	((SXSSFSheet)sheet).flushRows();
            }
			out = new FileOutputStream(filePath);
			workbook.write(out);
        }
        finally
        {
        	if(out != null)
        	{
        		out.close();
        	}
        	if(workbook != null)
        	{
	        	// dispose of temporary files backing this workbook on disk
	        	workbook.dispose();
        	}
        }
    }
    
}
