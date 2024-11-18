package com.gm.javaeaseframe.common.util.excel.writer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gm.javaeaseframe.common.util.FileUtil;
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
 * 写入excel并填充内容,一个sheet只能写65536行以下，超出会报异常
 * @author	GuoMin
 * @date	2017年7月15日
 */
public class Excel2003Writer extends AbstractExcelWriter
{
	/** 是否开始sheet */
	private boolean isOpenSheet = false;
	
	public Excel2003Writer(boolean printLog, IDataWriterCallback writer, ExcelWriterMeta meta)
	{
		super(printLog, writer, meta);
		if(meta.getMaxRowPreSheet() <= 0)
		{
			meta.setMaxRowPreSheet(65530);
		}
	}
	
	public String loadST(String name) throws Exception
	{
		InputStream is = this.getClass().getResourceAsStream(name);
		return FileUtil.read(is, "UTF-8");
//		java.net.URL url = this.getClass().getResource(name);
//		if(url == null)
//		{
//			return "";
//		}
//		String urlStr = url.toString(); 
//    	if(urlStr.startsWith("jar:file:"))//在Jar包中
//    	{
//    		// 找到!/ 截断之前的字符串 
//            String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2); 
//            java.net.URL jarURL = new java.net.URL(jarPath); 
//            java.net.JarURLConnection jarCon = (java.net.JarURLConnection) jarURL.openConnection(); 
//            java.util.jar.JarFile jarFile = jarCon.getJarFile(); 
//            java.util.Enumeration<java.util.jar.JarEntry> jarEntrys = jarFile.entries(); 
//            while (jarEntrys.hasMoreElements()) { 
//            	java.util.jar.JarEntry entry = jarEntrys.nextElement(); 
//                // 简单的判断路径，如果想做到想Spring，Ant-Style格式的路径匹配需要用到正则。 
//                String nameTmp = entry.getName(); 
//                if (nameTmp.endsWith(name)) { 
//                	
//                } 
//            }
//    	}
//    	else//在class path中
//    	{
//    		return FileUtils.read(url.getPath(), "UTF-8");
//    	}
//    	return "";
	}
	
	public Writer beginWorkbook(Writer writer, String filePath, WorkbookTemplate workbookTemplate) throws IOException
	{
		if(writer == null)
		{
			writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(filePath)));
		}
		StringBuffer wb = new StringBuffer();
		wb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		wb.append("<Workbook");
		if(workbookTemplate != null && workbookTemplate.getAttributes() != null && workbookTemplate.getAttributes().size() > 0)
		{
			wb.append(workbookTemplate.attrsToStr());
		}
		else
		{
			wb.append(" xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
			wb.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
			wb.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
			wb.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"");
			wb.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\"");
		}
		wb.append(">");
		wb.append("\n");
		String currDate = ExcelHandler.getCurrGMTDateTime();
		wb.append("<DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">");
		wb.append("<Author>" + StringUtils.trim(meta.getCreateUser()) + "</Author>");
		wb.append("<Created>" + currDate + "</Created>");
		wb.append("<LastSaved>" + currDate + "</LastSaved>");
		wb.append("<Version>15</Version>");
		wb.append("</DocumentProperties>");
		wb.append("\n");
		if(workbookTemplate != null && workbookTemplate.getHeaderDefaultElements() != null && workbookTemplate.getHeaderDefaultElements().size() > 0)
		{
			for(String element : workbookTemplate.getHeaderDefaultElements())
			{
				wb.append(element).append("\n");
			}
		}
		else
		{
			wb.append("<OfficeDocumentSettings xmlns=\"urn:schemas-microsoft-com:office:office\"><RemovePersonalInformation /></OfficeDocumentSettings>");
			wb.append("\n");
			wb.append("<ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">");
			wb.append("<WindowHeight>4530</WindowHeight>");
			wb.append("<WindowWidth>8505</WindowWidth>");
			wb.append("<WindowTopX>480</WindowTopX>");
			wb.append("<WindowTopY>120</WindowTopY>");
			wb.append("<AcceptLabelsInFormulas />");
			wb.append("<ProtectStructure>False</ProtectStructure>");
			wb.append("<ProtectWindows>False</ProtectWindows>");
			wb.append("</ExcelWorkbook>");
			wb.append("\n");
			wb.append("<Styles>");
			wb.append("\n");
			wb.append("<Style ss:ID=\"Default\" ss:Name=\"Normal\">");
			wb.append("<Alignment ss:Vertical=\"Bottom\" />");
			wb.append("<Borders />");
			wb.append("<Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"12\" />");
			wb.append("<Interior />");
			wb.append("<NumberFormat />");
			wb.append("<Protection />");
			wb.append("</Style>");
			wb.append("\n");
			wb.append("<Style ss:ID=\"Default_title\" ss:Name=\"Normal_title\">");
			wb.append("<Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" />");
			wb.append("<Borders />");
			wb.append("<Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"14\" ss:Bold=\"1\" />");
			wb.append("<Interior />");
			wb.append("<NumberFormat />");
			wb.append("<Protection />");
			wb.append("</Style>");
			wb.append("\n");
			wb.append("<Style ss:ID=\"Default_title_column\" ss:Name=\"Normal_title_column\">");
			wb.append("<Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" />");
			wb.append("<Borders />");
			wb.append("<Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"12\" ss:Bold=\"1\" />");
			wb.append("<Interior />");
			wb.append("<NumberFormat />");
			wb.append("<Protection />");
			wb.append("</Style>");
			wb.append("\n");
			wb.append("</Styles>");
			wb.append("\n");
		}
		// 写入excel文件头部信息
//		ST head = new ST(super.worksheetTemplate.getTempleteXmlWorkbook(), '$', '$');
//		head.add("Author", StringUtils.trim(meta.getCreateUser()));
//		head.add("Created", currDate);
//		head.add("LastSaved", currDate);
//		head.add("version", 15);
//		writer.write(head.render());
		writer.write(wb.toString());
		writer.flush();
		return writer;
	}
	
	public void endWorkbook(Writer writer, WorkbookTemplate workbookTemplate) throws IOException
	{
		// 写入excel文件尾部
		writer.write("</Workbook>");
		writer.flush();
	}

	@Override
	public Writer beginWorksheet(Writer writer, String sheetName, WorksheetTemplate worksheetTemplate) throws Exception
	{
		isOpenSheet = true;
		StringBuffer sb = new StringBuffer();
		sb.append("<Worksheet ss:Name=\"" + sheetName + "\"");
		if(worksheetTemplate != null && StringUtils.isNotBlank(worksheetTemplate.attrsToStr()))
		{
			sb.append(" ").append(worksheetTemplate.attrsToStr());
		}
		sb.append(">\n");
		if(worksheetTemplate != null)
		{
			List<String> elements = worksheetTemplate.getHeaderDefaultElements();
			for(String element : elements)
			{
				sb.append(element).append("\n");
			}
		}
		else
		{
			sb.append("<Table ss:ExpandedColumnCount=\"256\" ss:ExpandedRowCount=\"65535\"");
			sb.append(" x:FullColumns=\"1\" x:FullRows=\"1\" ss:DefaultColumnWidth=\"65\" ss:DefaultRowHeight=\"20\">");
		}
		writer.write(sb.toString());
		return writer;
	}

	@Override
	public void endWorksheet(Writer writer, WorksheetTemplate worksheetTemplate) throws Exception
	{
		if(isOpenSheet)
		{
			isOpenSheet = false;
			StringBuffer sb = new StringBuffer();
			if(worksheetTemplate != null)
			{
				List<String> elements = worksheetTemplate.getFooterDefaultElements();
				for(String element : elements)
				{
					sb.append(element).append("\n");
				}
			}
			else
			{
				sb.append("</Table>\n");
			}
			sb.append("</Worksheet>");
			writer.write(sb.toString());
			writer.flush();
		}
	}

	@Override
	public void insertRow(Writer writer, int rowNum, String[] columns, RowTemplate rowTemplate) throws Exception
	{
		StringBuffer row = new StringBuffer();
		row.append("<Row");
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
			String text = columns[columnIndex];
	    	CellTemplate cell = cellMap == null ? null : cellMap.get(columnIndex + 1);
			row.append("<Cell" + (cell != null ? cell.attrsToStr() : "") + ">");
			row.append("<Data ss:Type=\"String\">" + XMLEncoder.encode(text) + "</Data>");
			row.append("</Cell>");
		}
		row.append("</Row>\n");
		writer.write(row.toString());
	}
	
}
