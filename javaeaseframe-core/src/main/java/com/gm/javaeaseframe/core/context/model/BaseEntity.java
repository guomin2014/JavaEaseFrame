package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseEntity<PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1691693294834676971L;
    /** 排序列--按程序控制排序 */
    private transient List<OrderCol> orderColList = new ArrayList<>();
    /** 查询或更新列，参看paramDto.data参数说明 */
    private transient Map<String, Object> tabColMap = new HashMap<>();
    /** group选择列 */
    private transient List<String> groupList = new ArrayList<>();
    /** 同ParamDto */
    private transient int colPickMode = 0;

    private transient DbMatchMode matchMode = DbMatchMode.WHOLE_WORD;

    protected PK id;

    /** 创建时间 */
    protected Long createTime;
    /** 创建用户ID */
    protected Long createUserId;
    /** 创建用户名称 */
    protected String createUserName;
    /** 创建用户部门ID */
    protected Long createUserDeptId;
    /** 创建用户部门名称 */
    protected String createUserDeptName;

    /** 更新时间 */
    protected Long updateTime;
    /** 更新用户ID */
    protected Long updateUserId;
    /** 更新用户名称 */
    protected String updateUserName;
    /** 更新用户部门ID */
    protected Long updateUserDeptId;
    /** 更新用户部门名称 */
    protected String updateUserDeptName;
    
    /** 查询开始时间 */
	private transient String queryBeginTime;
	/** 查询结束时间 */
	private transient String queryEndTime;
	/** 查询时间 */
	private transient String queryTime;
	/** 查询时间格式（用于将数据库的时间按该格式返回，默认：%Y-%m-%d %H:%i:%s） */
	private transient String queryTimeFormat = "%Y-%m-%d %H:%i:%s";
    
    /** 实体映射表名，在分表存储数据的场景下使用 */
    private transient String tableName;

    public abstract PK getId();
    
    public abstract void resetId(PK id);

    public abstract boolean newEntity();

    public List<OrderCol> getOrderColList()
	{
		return orderColList;
	}
    /**
     * 设置排序列--按程序控制排序
     * @param orderColList
     */
	public void setOrderColList(List<OrderCol> orderColList)
	{
		this.orderColList = orderColList;
	}

	public Map<String, Object> getTabColMap() {
        if (tabColMap == null)
            tabColMap = new HashMap<>();
        return tabColMap;
    }

    public void setTabColMap(Map<String, Object> notSelectCols) {
        this.tabColMap = notSelectCols;
    }

    public DbMatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(DbMatchMode matchMode) {
        this.matchMode = matchMode;
    }

    public int getColPickMode() {
        return colPickMode;
    }

    public void setColPickMode(int colPickMode) {
        this.colPickMode = colPickMode;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

	public String getQueryBeginTime()
	{
		return queryBeginTime;
	}

	public void setQueryBeginTime(String queryBeginTime)
	{
		this.queryBeginTime = queryBeginTime;
	}

	public String getQueryEndTime()
	{
		return queryEndTime;
	}

	public void setQueryEndTime(String queryEndTime)
	{
		this.queryEndTime = queryEndTime;
	}

	public String getQueryTime()
	{
		return queryTime;
	}

	public void setQueryTime(String queryTime)
	{
		this.queryTime = queryTime;
	}

	public String getQueryTimeFormat()
	{
		return queryTimeFormat;
	}

	public void setQueryTimeFormat(String queryTimeFormat)
	{
		this.queryTimeFormat = queryTimeFormat;
	}

	public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public Long getCreateUserId()
	{
		return createUserId;
	}

	public void setCreateUserId(Long createUserId)
	{
		this.createUserId = createUserId;
	}

	public Long getCreateUserDeptId()
	{
		return createUserDeptId;
	}

	public void setCreateUserDeptId(Long createUserDeptId)
	{
		this.createUserDeptId = createUserDeptId;
	}

	public String getCreateUserDeptName()
	{
		return createUserDeptName;
	}

	public void setCreateUserDeptName(String createUserDeptName)
	{
		this.createUserDeptName = createUserDeptName;
	}

	public Long getUpdateUserId()
	{
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId)
	{
		this.updateUserId = updateUserId;
	}

	public Long getUpdateUserDeptId()
	{
		return updateUserDeptId;
	}

	public void setUpdateUserDeptId(Long updateUserDeptId)
	{
		this.updateUserDeptId = updateUserDeptId;
	}

	public String getUpdateUserDeptName()
	{
		return updateUserDeptName;
	}

	public void setUpdateUserDeptName(String updateUserDeptName)
	{
		this.updateUserDeptName = updateUserDeptName;
	}

	public void initAttrValue() {

    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
