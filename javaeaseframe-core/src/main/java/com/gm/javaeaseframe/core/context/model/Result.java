package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result<T> implements Serializable{

	private static final long serialVersionUID = 1392895965844990398L;
	
	/** 分页信息 */
    private PageInfo pageInfo = new PageInfo();
    /** 数据列表 */
    private List<T> list = new ArrayList<T>();

    /**
     * 获取分页信息
     * @return
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * 设置分页信息
     * @param pageInfo
     */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * 获数据列表
     * @return
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 设置数据列表
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;
    }
}
