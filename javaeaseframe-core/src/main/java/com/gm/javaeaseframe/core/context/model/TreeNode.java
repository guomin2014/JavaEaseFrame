package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> implements Serializable {

	private static final long serialVersionUID = -5204422644785876596L;
	/** 节点ID */
	private String id;
	/** 节点名称 */
	private String name;
	/** 父节点ID */
	private String pId;
	/** 是否被选中 */
	private boolean checked = false;
	/** 是否禁用选择 */
	private boolean chkDisabled = false;
	/** 是否是父节点 */
	private Boolean isParent; 
	/** 是否有子节点 */
    private Boolean hasChild; 
    /** 是否展开 */
    private boolean open = true;
    /** 节点描述 */
    private String remark;
    /** 节点数据 */
    private T data;
    /** 节点层级 */
    private Integer level;
    /** 子节点集合 */
    private List<TreeNode<T>> childList = new ArrayList<>();
    
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	public Boolean getIsParent()
	{
		return isParent;
	}
	public void setIsParent(Boolean isParent)
	{
		this.isParent = isParent;
	}
	public Boolean getHasChild()
	{
		return hasChild;
	}
	public void setHasChild(Boolean hasChild)
	{
		this.hasChild = hasChild;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    public void setChildList(List<TreeNode<T>> childList) {
        this.childList = childList;
    }

}
