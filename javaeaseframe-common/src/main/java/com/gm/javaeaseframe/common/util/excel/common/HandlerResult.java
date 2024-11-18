package com.gm.javaeaseframe.common.util.excel.common;

public class HandlerResult<T>
{
	/** 处理结果代码 */
	private String code;
	/** 处理结果描述 */
	private String desc;
	/** 处理结果数据 */
	private T data;
	
	public HandlerResult(){}
	
	public HandlerResult(CodeEnum code)
	{
		if(code != null)
		{
			this.code = code.getCode();
			this.desc = code.getDesc();
		}
	}
	
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public T getData()
	{
		return data;
	}
	public void setData(T data)
	{
		this.data = data;
	}
}
