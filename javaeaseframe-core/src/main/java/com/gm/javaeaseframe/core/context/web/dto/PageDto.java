package com.gm.javaeaseframe.core.context.web.dto;

import java.io.Serializable;

import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;

public class PageDto implements Serializable {

	private static final long serialVersionUID = -5060881027252730658L;

	/**
     * 每页显示条数，默认 -1
     */
	@CustomApiModelProperty(value = "每页显示条数，默认 -1，表示不分页")
    private int pageSize = -1;

    /**
     * 当前页
     */
	@CustomApiModelProperty(value = "当前页数")
    private int currentPage = 1;

    /**
     * 总页数
     */
	@CustomApiModelProperty(value = "总页数")
    private int totalPage = 1;

    /**
     * 总数
     */
	@CustomApiModelProperty(value = "总记录数")
    private int totalSize = 0;

    /**
     * 是否有前一页
     */
	@CustomApiModelProperty(value = "是否有前一页")
    private boolean previous = false;

    /**
     * 是否有后一页
     */
	@CustomApiModelProperty(value = "是否有后一页")
    private boolean next = false;


    public PageDto() {

    }

    public PageDto(int pageSize, int currentPage, int totalSize) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalSize = totalSize;
        this.previous = hasPrevious();
        this.next = hasNext();
        this.totalPage = getTotalPage();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.previous = hasPrevious();
        this.next = hasNext();
        this.totalPage = getTotalPage();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        this.previous = hasPrevious();
        this.next = hasNext();
        this.totalPage = getTotalPage();
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
        this.previous = hasPrevious();
        this.next = hasNext();
        this.totalPage = getTotalPage();
    }


    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public boolean isPrevious() {
		return previous;
	}

	public boolean isNext() {
		return next;
	}

	/**
     * 是否存在上一页
     *
     * @return true / false
     */
    private boolean hasPrevious() {
        return this.currentPage > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    private boolean hasNext() {
        return this.currentPage < this.getTotalPage();
    }

    public int getTotalPage() {
        if (pageSize <= 0) {
            return 1;
        }
        int pages = totalSize / pageSize;
        if (totalSize % pageSize != 0) {
            pages++;
        }
        return pages;
    }
    
}
