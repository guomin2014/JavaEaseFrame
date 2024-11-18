package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTreeEntity<PK extends Serializable> extends BaseEntity<PK> {

	private static final long serialVersionUID = 2533211576690848338L;
	
	/** 上级节点ID */
	protected PK parentId;
	/** 节点名称 */
	protected String name;
	/** 子节点数量 */
	protected Integer childSize;
	/** 节点层级 */
	protected Integer level;
	/** 节点描述 */
	protected String remark;
	/** 节点类型，0：区域，1：部门 */
	protected Integer type;
    /** 排序编号 */
	protected Integer orderId;
    /** 子节点 */
	protected List<BaseTreeEntity<PK>> childList = new ArrayList<>();
    
    public abstract PK getParentId();
    public abstract void resetParentId(PK id);
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getChildSize()
	{
		return childSize;
	}

	public void setChildSize(Integer childSize)
	{
		this.childSize = childSize;
	}

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Integer orderId)
    {
        this.orderId = orderId;
    }

    public List<BaseTreeEntity<PK>> getChildList() {
        return childList;
    }

    public void setChildList(List<BaseTreeEntity<PK>> childList) {
        this.childList = childList;
    }
    
}
