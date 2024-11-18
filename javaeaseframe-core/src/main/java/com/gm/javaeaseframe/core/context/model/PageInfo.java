package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;

public class PageInfo implements Serializable{

	private static final long serialVersionUID = 9027146231858488888L;

	/**
     * 开始行
     */
    private int beginIndex;

    /**
     * 每页记录数，如果值为-1则表示查询所有记录
     */
    private int prePageResult;

    /**
     * 总记录数
     */
    private int totalResult;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currPage;
    /**
     * 显示页码数量
     */
    private int displayPageSize;

    /**
     * 是否还有下一页
     */
    private boolean hasNextPage;

    /**
     * 是否还有上一页
     */
    private boolean hasPrePage;

    public PageInfo() {
        this(10);
    }

    public PageInfo(int prePageResult) {
    	if(prePageResult == 0)
    	{
    		prePageResult = 10;
    	}
        this.beginIndex = 0;
        this.prePageResult = prePageResult;
        this.totalResult = 0;
        this.totalPage = 0;
        this.currPage = 1;
        this.displayPageSize = 5;
        this.hasPrePage = false;
        this.hasNextPage = false;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
    	if(currPage <= 0)
    	{
    		currPage = 1;
    	}
    	this.currPage = currPage <= 0 ? 1 : currPage;
        this.beginIndex = (this.currPage - 1) * this.prePageResult;
        if(this.beginIndex < 0)
        {
        	this.beginIndex = 0;
        }
    }

    public int getPrePageResult() {
        return prePageResult;
    }

    public void setPrePageResult(int prePageResult) {
    	this.prePageResult = prePageResult == 0 ? 10 : prePageResult;
        this.beginIndex = (this.currPage - 1) * this.prePageResult;
        if(this.beginIndex < 0)
        {
        	this.beginIndex = 0;
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
        if(this.prePageResult == 0)
        {
        	this.prePageResult = 10;
        }
        this.totalPage = this.prePageResult < 0 ? 1 : this.totalResult / this.prePageResult + ((this.totalResult % this.prePageResult > 0) ? 1 : 0);
        this.hasPrePage = this.currPage > 1;
        this.hasNextPage = this.currPage < this.totalPage;
    }

    public boolean getHasPrePage() {
        return hasPrePage;
    }

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public int getDisplayPageSize() {
        return displayPageSize;
    }

    public void setDisplayPageSize(int displayPageSize) {
        this.displayPageSize = displayPageSize;
    }
}
