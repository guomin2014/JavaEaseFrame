package com.gm.javaeaseframe.core.context.model;

import java.util.HashMap;
import java.util.Map;

import com.gm.javaeaseframe.core.context.service.IUser;

/**
 * 请求上下文信息
 * 
 * @author GM
 * @date 2023年10月27日
 */
public class Context {

	/** 当前用户 */
    private IUser user;
    /** 请求ID */
    private String requestId;
    /** 相关属性 */
    private Map<String, Object> map = new HashMap<String, Object>();
    
    public Context() {}
    public Context(IUser user) {
        this.user = user;
    }
    public Context(IUser user, String requestId) {
    	this.user = user;
    	this.requestId = requestId;
    }

    public void setAttribute(String key, Object value) {
        map.put(key, value);
    }

    public Object getAttribute(String key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, Class<T> clazz) {
        return (T) map.get(key);
    }

    public Object removeAttribute(String key) {
        return map.remove(key);
    }
    public void putAll(Map<String, Object> map){
        map.putAll(map);
    }
    public boolean containsKey(String key){
        return map.containsKey(key);
    }
	public IUser getUser() {
		return user;
	}
	public void setUser(IUser user) {
		this.user = user;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
    
}
