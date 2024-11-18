package com.gm.javaeaseframe.core.context.web;

import java.io.Serializable;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.model.BaseEntity;

public abstract class BaseCRUDForm<T extends BaseEntity<PK>, PK extends Serializable> extends BaseForm {

    /** 操作执行结果 */
    public int excuteResult;
    /** cookie名称[ajax下载文件使用] */
    private String cookieName;
    /** cookie值[ajax下载文件使用] */
    private String cookieValue;

    /**
     * 获取实体对象<br/>
     * 必须子类实现具体化才能把前端请求数据映射到实体类属性上
     * @return
     */
    public abstract T getEntity();

    /**
     * 获取查询对象<br/>
     * 必须子类实现具体化才能把前端请求数据映射到实体类属性上
     * @return
     */
    public abstract T getQuery();

    /**
     * 必须子类实现具体化才能把前端请求数据映射到属性上
     * @return
     */
    public abstract PK[] getId();

    /**
     * 必须子类实现具体化才能把前端请求数据映射到属性上
     * @param id
     */
    public abstract void setId(PK[] id);

    /**
     * 清除消息队列
     */
    public void clearMessage() {
        getModel().clear();
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
