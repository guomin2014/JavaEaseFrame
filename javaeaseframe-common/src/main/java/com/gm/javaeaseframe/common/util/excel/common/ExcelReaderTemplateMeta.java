package com.gm.javaeaseframe.common.util.excel.common;

public class ExcelReaderTemplateMeta extends ExcelReaderMeta
{
	/** 主题 */
	private String title;
	/** 标题 */
	private String[] columnName;
	/** 是否使用默认模板 */
	private boolean useDefaultTemplate;
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String[] getColumnName()
	{
		return columnName;
	}
	public void setColumnName(String[] columnName)
	{
		this.columnName = columnName;
	}
	public boolean getUseDefaultTemplate()
	{
		return useDefaultTemplate;
	}
	public void setUseDefaultTemplate(boolean useDefaultTemplate)
	{
		this.useDefaultTemplate = useDefaultTemplate;
	}
	
}
