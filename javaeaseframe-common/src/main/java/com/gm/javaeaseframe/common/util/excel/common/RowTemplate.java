package com.gm.javaeaseframe.common.util.excel.common;

import java.util.Map;

/**
 * 行模板
 * 属性说明详见：https://msdn.microsoft.com/en-us/library/office/documentformat.openxml.spreadsheet.row.aspx
 * @author	GuoMin
 * @date	2017年7月28日
 */
public class RowTemplate extends Template
{
//	/** 列范围 */
//	private String spans;
//	/** 是否手动设置行高，1：是 */
//	private String customHeight;
//	/** 行高(Row Height)，不包含margin和padding */
//	private String ht;
//	/** 边框线 */
//	private String outlineLevel;
//	/** 边框线是否合并 */
//	private String collapsed;
//	/** Row Index */
//	private String r;
//	/** Style Index */
//	private String s;
//	/** Thick Bottom */
//	private String thickBot;
//	/** Thick Top Border */
//	private String thickTop;
	/** 单元格的模板列表，key：单元格序号，从1开始，value：单元格的模板 */
	private Map<Integer, CellTemplate> cellTemplates;
	/** 行类型，0：数据行，1：标题行，2：栏标题行，详见 RowTypeEnum */
	private int rowType;
	/** 列数，一般情况等于cellTemplates.size */
	private int columnCount;
	
	public Map<Integer, CellTemplate> getCellTemplates()
	{
		return cellTemplates;
	}
	public void setCellTemplates(Map<Integer, CellTemplate> cellTemplates)
	{
		this.cellTemplates = cellTemplates;
		this.columnCount = cellTemplates == null ? 0 : cellTemplates.size();
	}
	public int getRowType()
	{
		return rowType;
	}
	public void setRowType(int rowType)
	{
		this.rowType = rowType;
	}
	public int getColumnCount()
	{
		return columnCount == 0 ? (cellTemplates == null ? 0 : cellTemplates.size()) : columnCount;
	}
	public void setColumnCount(int columnCount)
	{
		this.columnCount = columnCount;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("rowType:").append(rowType).append(",");
		sb.append("columnCount:").append(columnCount).append(",");
		sb.append("cellTemplates:").append(cellTemplates);
		return sb.toString();
	}
}
