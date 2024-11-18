package com.gm.javaeaseframe.common.util;

import java.util.LinkedHashMap;
import java.util.List;

public class ExcelExportData {
	/**
	 * 导出数据 key:String 表示每个Sheet的名称 value:List<?> 表示每个Sheet里的所有数据行
	 */
	private LinkedHashMap<String, List<?>> dataMap;
	/**
	 * 每个Sheet里的顶部大标题
	 */
	private String[] titles;
	/**
	 * 单个sheet里的数据列标题
	 */
	private List<String[]> columnNames;

	/**
	 * 单个sheet里每行数据的列对应的对象属性名称
	 */
	private List<String[]> fieldNames;
	
	/** Sheet表格名称 */
    private String sheetName;
	/** 大标题 */
	private String title;
	/** 小标题 */
	private String subTitle;
	/** 列名 */
	private String[] columnName;
	/** 数据列表 */
	private List<String[]> dataList;
	
	public List<String[]> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String[]> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public List<String[]> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String[]> columnNames) {
		this.columnNames = columnNames;
	}

	public LinkedHashMap<String, List<?>> getDataMap() {
		return dataMap;
	}

	public void setDataMap(LinkedHashMap<String, List<?>> dataMap) {
		this.dataMap = dataMap;
	}

	public List<String[]> getDataList()
	{
		return dataList;
	}

	public void setDataList(List<String[]> dataList)
	{
		this.dataList = dataList;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSubTitle()
	{
		return subTitle;
	}

	public void setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
	}

	public String[] getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String[] columnName)
	{
		this.columnName = columnName;
	}

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

}
