package com.gm.javaeaseframe.common.util.excel.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.gm.javaeaseframe.common.util.FileUtil;
import com.gm.javaeaseframe.common.util.excel.IDataReaderCallback;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderMeta;
import com.gm.javaeaseframe.common.util.excel.common.ExcelReaderTemplateMeta;

public class ExcelReaderFactory
{
	/** excel2003扩展名 */  
    private static final String EXCEL03_EXTENSION = "xls";  
    /** excel2007扩展名 */  
    private static final String EXCEL07_EXTENSION = "xlsx";
    
	public static IExcelReader createReader(boolean debug, IDataReaderCallback reader, ExcelReaderMeta meta) throws Exception
	{
		return createReader(null, debug, reader, meta);
	}
	
	public static IExcelReader createReader(String prefix, boolean debug, IDataReaderCallback reader, ExcelReaderMeta meta) throws Exception
	{
		String filePath = meta.getFilePath();
		BufferedInputStream bis = null;
		boolean useDefaultTemplate = false;
		try
		{
			if(meta instanceof ExcelReaderTemplateMeta)
			{
				ExcelReaderTemplateMeta tmeta = (ExcelReaderTemplateMeta)meta;
				useDefaultTemplate = tmeta.getUseDefaultTemplate();
			}
			if(useDefaultTemplate)
			{
				if (StringUtils.isBlank(filePath))
				{
					throw new Exception("文件路径不能为空");
				}
				return createReaderByExt(prefix, debug, reader, meta);
			}
			if (StringUtils.isNotBlank(filePath)) {
				bis = new BufferedInputStream(new FileInputStream(filePath));
			} else if (meta.getInput() != null){
				bis = new BufferedInputStream(meta.getInput());
			} else {
				throw new Exception("文件路径不能为空");
			}
			bis.mark(0);
			if(POIFSFileSystem.hasPOIFSHeader(bis))
			{
				return new Excel2003Reader(prefix, debug, reader, meta);
			}
			else if(POIXMLDocument.hasOOXMLHeader(bis))
			{
				return new Excel2007Reader(prefix, debug, reader, meta);
			}
			else
			{
				byte[] bys = new byte[64];
				int len = bis.read(bys);
				String cont = new String(bys, 0, len).replaceAll("\r", "").replaceAll("\n", "");
				String regex = "<\\?[\\s]*xml[\\s]+.*";
				Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher m = pattern.matcher(cont);
				if(m.find())
				{
					return new Excel2003XMLReader(prefix, debug, reader, meta);
				}
				throw new Exception("不能识别的文件格式");
			}
		}
		finally
		{
			if(bis != null)//文件流才关闭
			{
				bis.reset();
				if (StringUtils.isNotBlank(filePath)) {
					try
					{
						bis.close();
					}catch(Exception e){}
				}
			}
		}
	}
	
	private static IExcelReader createReaderByExt(String prefix, boolean debug, IDataReaderCallback reader, ExcelReaderMeta meta) throws Exception
	{
    	String ext = FileUtil.getExtend(meta.getFilePath());
		if (StringUtils.isBlank(ext))
		{
			throw new Exception("文件格式错误，文件的扩展名只能是xls或xlsx");
		}
		else if (ext.equalsIgnoreCase(EXCEL03_EXTENSION))
		{
			return new Excel2003Reader(prefix, debug, reader, meta);
		}
		else if (ext.equalsIgnoreCase(EXCEL07_EXTENSION))
		{
			return new Excel2007Reader(prefix, debug, reader, meta);
		}
		else
		{
			throw new Exception("文件格式错误，文件的扩展名只能是xls或xlsx");
		}
	}
	
}
