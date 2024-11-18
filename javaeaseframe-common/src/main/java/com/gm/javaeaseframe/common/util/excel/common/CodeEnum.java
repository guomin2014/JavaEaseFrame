package com.gm.javaeaseframe.common.util.excel.common;

public enum CodeEnum
{
	SUCCESS("000000", "成功"),
	
	FAILURE("100000", "失败"),
	
	SUCCESS_END_SHEET("300101", "成功并结束当前工作薄解析"),
	
	SUCCESS_END_WORKBOOK("300102", "成功并结束当前文档解析"),
	
	FAILURE_END_SHEET("300201", "失败并结束当前工作薄解析"),
	
	FAILURE_END_WORKBOOK("300202", "失败并结束当前文档解析"),
	;
	
	String code;
	String desc;
	
	CodeEnum(String code, String desc)
	{
		this.code = code;
		this.desc = desc;
	}
	public String getCode()
	{
		return code;
	}
	public String getDesc()
	{
		return desc;
	}
	
	public static CodeEnum findByValue(String code) {
        for (CodeEnum strategy : values()) {
            if (strategy.code.equals(code)) {
                return strategy;
            }
        }
        return null;
    }
}
