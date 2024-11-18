package com.gm.javaeaseframe.core.context.web.dto;

import java.io.Serializable;

import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;

/**
 * 通用响应对象
 *
 * @param <T>
 * @author GM
 * @date 2023年9月19日
 */
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1356156116174723849L;
    /**
     * 是否成功
     */
    @CustomApiModelProperty(value = "是否成功")
    private boolean success;
    /**
     * 状态码
     */
    @CustomApiModelProperty(value = "状态码，0：成功，其它为失败")
    private int code;
    /**
     * 描述
     */
    @CustomApiModelProperty(value = "失败描述")
    private String msg;
    /**
     * 响应数据
     */
    @CustomApiModelProperty(value = "响应数据")
    private T data;

    public CommonResult() {
    }

    public CommonResult(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
        this.success = true;
    }

    public CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.success = code == 0 ? true : false;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        this.success = code == 0 ? true : false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static <T> CommonResult<T> createSuccessResult(T data) {
    	CommonResult<T> result = new CommonResult<>();
    	result.setCode(0);
    	result.setMsg("success");
    	result.setData(data);
    	return result;
    }
    
    public static <T> CommonResult<T> createFailureResult(int code, String msg) {
    	return new CommonResult<>(code, msg);
    }
}
