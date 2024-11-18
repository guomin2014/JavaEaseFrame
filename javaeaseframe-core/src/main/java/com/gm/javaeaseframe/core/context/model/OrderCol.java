package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;
/**
 * 排序列
 * @author Administrator
 *
 */
public class OrderCol implements Serializable{
	private static final long serialVersionUID = 8760496522293494885L;
	
	/** 降序 */
	public static final String DESCENDING = "desc";
	/** 升序 */
	public static final String ASCENDING = "asc";

	/** 列名 */
	private String colName;
	
	/** 排序方式 */
	private String sortKind = "asc";

	public OrderCol(){
	}
	public OrderCol(String colName){
		this.colName = colName;
	}
	public OrderCol(String colName, String sortKind){
		this.colName = colName;
		this.sortKind = sortKind;
	}
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getSortKind() {
		return sortKind;
	}

	public void setSortKind(String sortKind) {
		this.sortKind = sortKind;
	}
}