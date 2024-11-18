package com.gm.javaeaseframe.core.context.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamDto {

    /**
     * 数据列集合 <br/>
     * key 列名 <br/>
     * value 更新时为列的值； 查询时忽略,可设为null <br/>
     * <br/>
     * 根据colPickMode 确定需要的列: <br/>
     * select语句，data为空时，全选择。 data不为空时，按以下方式选择:<br/>
     * 0 不选择data中的列，1 只选择data中的列 <br/>
     * <br/>
     * update语句，data为空时，全不选择。data不为空时，按以下方式选择:<br/>
     * 0 只选择data中的列 ，1 不选择data中的列
     * 
     */
    private Map<String, Object> data;
    /** 条件列集合 */
    private Map<String, Object> condition = new HashMap<String, Object>();
    /** 排序列--按XML配置排序 */
    private Map<String, String> orderCol = new HashMap<String, String>();
    /** 排序列--按程序控制排序 */
    private List<OrderCol> orderColList = new ArrayList<>();
    /** group选择列 */
    private List<String> groupList = new ArrayList<>();
    /** 匹配模式 */
    private DbMatchMode matchMode = DbMatchMode.WHOLE_WORD;
    /** 列存在模式，取值：0，1，使用方式参数data说明 */
    private int colPickMode = 0;
    /** 实体映射的表名称 */
    private String tableName;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getCondition() {
        if (condition == null)
            condition = new HashMap<>();
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public Map<String, String> getOrderCol() {
        if (orderCol == null)
            orderCol = new HashMap<>();
        return orderCol;
    }

    public void addOrderCol(String orderCol, DbOrderKind orderKind) {
        getOrderCol().put(orderCol, orderKind.name());
    }

    public void addOrderCol(String orderCol) {
        getOrderCol().put(orderCol, DbOrderKind.ASC.name());
    }

    public void setOrderCol(Map<String, String> orderCol) {
        this.orderCol = orderCol;
    }
    
     public List<OrderCol> getOrderColList() {
    	 if(orderColList == null)
    	 {
    		 orderColList = new ArrayList<OrderCol>();
    	 }
    	 return orderColList;
     }
    
     public void setOrderColList(List<OrderCol> orderColList) {
    	 this.orderColList = orderColList;
//	     if (orderColList != null && !orderColList.isEmpty()) {
//		     if (orderCol == null) {
//		    	 orderCol = new HashMap<String, String>();
//		     }
//		     for (OrderCol col : orderColList) {
//		    	 orderCol.put(col.getColName(), col.getSortKind());
//		     }
//	     }
     }

    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
