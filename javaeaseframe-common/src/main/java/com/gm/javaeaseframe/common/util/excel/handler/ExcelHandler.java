package com.gm.javaeaseframe.common.util.excel.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

public class ExcelHandler
{
	private static Map<Integer, String> indexMap = new HashMap<>();
	private static Map<String, Integer> nameMap = new HashMap<>();
	static
	{
		int max = 100;
		for(int index = 1; index <= max; index++)
		{
			String name = intToNumber26(index);
			indexMap.put(index, name);
			nameMap.put(name, index);
		}
	}
	/**
	 * 通过栏名获取栏序号：A->1，B->2
	 * @param columnName
	 * @return
	 */
	public static int getColumnIndex(String columnName)
	{
		if(!nameMap.containsKey(columnName))
		{
			int index = number26ToInt(columnName);
			indexMap.put(index, columnName);
			nameMap.put(columnName, index);
			return index;
		}
		return nameMap.get(columnName);
	}
	/**
	 * 通过栏序号获取栏名：1->A，2->B
	 * @param columnIndex
	 * @return
	 */
	public static String getColumnName(int columnIndex)
	{
		if(!indexMap.containsKey(columnIndex))
		{
			String columnName = intToNumber26(columnIndex);
			indexMap.put(columnIndex, columnName);
			nameMap.put(columnName, columnIndex);
			return columnName;
		}
		return indexMap.get(columnIndex);
	}
	
	/**
     * 将10进制数字转换成26进制字符，映射关系：[1-26] -> [A-Z]
     * 26进制：即字母A-Z
     * @param digst
     * @return
     */
    public static String intToNumber26(int digst)
    {
        String name = "";  
        while (digst > 0){  
            int m = digst % 26;  
            if (m == 0) m = 26;  
            name = (char)(m + ('A' - 1)) + name;  
            digst = (digst - m) / 26;  
        }  
        return name;
    }
    /**
     * 将26进制字符转换成10进制数字，映射关系：[A-Z] -> [1-26]
     * @param value
     * @return
     */
    public static int number26ToInt(String value)
    {
        if (StringUtils.isBlank(value)) return 0;   
        int n = 0;
        int j = 1;
        for (int i = value.length() - 1; i >= 0; i--){  
            char c = value.charAt(i);  
            if (c < 'A' || c > 'Z') continue;  
            n += ((int)c - 64) * j;
            j *= 26;
        }  
        return n;  
    }
    /**
     * 将日期时间转换成 pattern 格式的格林时间
     * @param date
     * @param pattern
     * @return
     */
	public final static String getGMTDateTime(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null)
			pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
		return sdf.format(date);
	}

	public final static String getGMTDateTime(Date date) {
		return getGMTDateTime(date, null);
	}

	public final static String getCurrGMTDateTime() {
		return getCurrGMTDateTime(null);
	}

	public final static String getCurrGMTDateTime(String pattern) {
		return getGMTDateTime(new Date(), pattern);
	}
}
