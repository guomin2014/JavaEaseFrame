package com.gm.javaeaseframe.core.context.web;

import java.util.HashMap;
import java.util.Map;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.model.PageInfo;

public class BaseForm {

    /** 分页信息 */
    private PageInfo pageInfo = new PageInfo();
    /** 字符数组 */
    public String[] groupName;
    /** 消息传递model */
    private Map<String, Object> model = new HashMap<String, Object>();
    
    /** 操作执行结果 */
    public int excuteResult;
    /** cookie名称[ajax下载文件使用] */
    private String cookieName;
    /** cookie值[ajax下载文件使用] */
    private String cookieValue;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    /**
     * 清除消息队列
     */
    public void clearMessage() {
        model.clear();
    }

    /**
     * 服务器段校验表单
     * 
     * @return
     * @throws AppException
     */
    public boolean validate() throws BusinessException {
        return true;
    }

    public String[] getGroupName() {
        return groupName;
    }

    public void setGroupName(String[] groupName) {
        this.groupName = groupName;
    }

    /**
     * 操作执行结果
     * @return
     */
    public int getExcuteResult() {
        return excuteResult;
    }

    public void setExcuteResult(int excuteResult) {
        this.excuteResult = excuteResult;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }
}
