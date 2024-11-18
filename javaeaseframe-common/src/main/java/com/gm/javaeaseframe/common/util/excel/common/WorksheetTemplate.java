package com.gm.javaeaseframe.common.util.excel.common;

/**
 * sheet模板
 * 属性说明详见：https://msdn.microsoft.com/en-us/library/office/documentformat.openxml.spreadsheet.worksheet.aspx
 * @author	GuoMin
 * @date	2017年7月28日
 */
public class WorksheetTemplate extends Template
{
	//------------------- excel 2003 模板属性 -----------------------
	
	//------------------- excel 2007 模板属性 -----------------------
//	/** Sheet Format Properties（子节点） */
//	private String sheetFormatPr;
//	/** Sheet Views（子节点） */
//	private String sheetViews;
//	/** Phonetic Properties（子节点） */
//	private String phoneticPr;
//	/** Page Margins（子节点） */
//	private String pageMargins;
//	/** Page Setup Settings（子节点） */
//	private String pageSetup;
//	/** Merge Cells（子节点） */
//	private String mergeCells;
//	/** Column Information（子节点） */
//	private String cols;
	/** 标题行模板 */
	private RowTemplate titleTemplate;
	/** 列标题行模板 */
	private RowTemplate columnTemplate;
	/** 数据行模板 */
	private RowTemplate dataTemplate;
	
	public RowTemplate getTitleTemplate()
	{
		return titleTemplate;
	}
	public void setTitleTemplate(RowTemplate titleTemplate)
	{
		this.titleTemplate = titleTemplate;
	}
	public RowTemplate getColumnTemplate()
	{
		return columnTemplate;
	}
	public void setColumnTemplate(RowTemplate columnTemplate)
	{
		this.columnTemplate = columnTemplate;
	}
	public RowTemplate getDataTemplate()
	{
		return dataTemplate;
	}
	public void setDataTemplate(RowTemplate dataTemplate)
	{
		this.dataTemplate = dataTemplate;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[titleTemplate:").append(titleTemplate == null ? "" : titleTemplate.toString()).append("]");
		sb.append("[columnTemplate:").append(columnTemplate == null ? "" : columnTemplate.toString()).append("]");
		sb.append("[dataTemplate:").append(dataTemplate == null ? "" : dataTemplate.toString()).append("]");
		return sb.toString();
	}
	
}
