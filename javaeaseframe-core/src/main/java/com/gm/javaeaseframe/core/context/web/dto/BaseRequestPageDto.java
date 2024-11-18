package com.gm.javaeaseframe.core.context.web.dto;

import java.io.Serializable;

import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;

public class BaseRequestPageDto implements Serializable {

	private static final long serialVersionUID = -8677090644661746368L;
	
	/** 每页显示条数，默认 -1，表示不分页 */
	@CustomApiModelProperty(value = "每页显示条数，默认 -1，表示不分页", position = 2)
    private int pageSize = -1;

    /**  当前页数 */
	@CustomApiModelProperty(value = "当前页数，默认1", position = 2)
    private int currentPage = 1;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
