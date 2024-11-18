package com.gm.javaeaseframe.core.context.web.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;

public class BasePageDto<T> implements Serializable {

	private static final long serialVersionUID = 8143980267412248303L;
	/** 分页信息 */
	@CustomApiModelProperty(value = "分页信息")
	private PageDto page;
	/** 数据列表 */
	@CustomApiModelProperty(value = "数据列表")
	private List<T> records = Collections.emptyList();
	
	public PageDto getPage() {
		return page;
	}
	public void setPage(PageDto page) {
		this.page = page;
	}
	public List<T> getRecords() {
		return records;
	}
	public void setRecords(List<T> records) {
		this.records = records;
	}
	
}
