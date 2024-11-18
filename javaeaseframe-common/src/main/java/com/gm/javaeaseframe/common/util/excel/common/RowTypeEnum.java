package com.gm.javaeaseframe.common.util.excel.common;

public enum RowTypeEnum
{
	DATA(0, "数据行"),
	TITLE(1, "主标题行"),
	COLUMN(2, "栏标题行")
	;
	
	private int code;
	private String desc;
	RowTypeEnum(int code, String desc)
	{
		this.code = code;
		this.desc = desc;
	}
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
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
	public static RowTypeEnum findByValue(int code) {
        for (RowTypeEnum strategy : values()) {
            if (strategy.code == code) {
                return strategy;
            }
        }
        return null;
    }
	
}
