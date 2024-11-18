package com.gm.javaeaseframe.common.util.excel.common;

/**
 * 单元格模板
 * 属性说明详见：https://msdn.microsoft.com/en-us/library/office/documentformat.openxml.spreadsheet.cell.aspx
 * @author	GuoMin
 * @date	2017年7月28日
 */
public class CellTemplate extends Template
{
//	/** Reference,An A1 style reference to the location of this cell */
//	private String r;
//	/** Style Index */
//	private String s;
//	/** Cell Data Type */
//	private String t;
	/** 单元格的值 */
	private String text;

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("attrbutes:【").append(this.getAttributes()).append("】");
		sb.append("text:" + this.text);
		return sb.toString();
	}
	
}
