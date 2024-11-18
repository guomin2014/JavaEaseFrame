package com.gm.javaeaseframe.demo.common.code;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 天气类型
 * 1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨，14：雷阵雨，15：阵雨
 * @author Shave
 *
 */
public enum WeatherTypeEnum{
	SUN(1, "晴"),
	CLOUDY(2, "多云"),
	OVERCAST(3, "阴"),
	RAIN_LIGHT(4, "小雨"),
	RAIN_MODERATE(5, "中雨"),
	RAIN_HEAVY(6, "大雨"),
	RAIN_STORM(7, "暴雨"),
	RAIN_WITH_SNOW(8, "雨夹雪"),
	SNOW_LIGHT(9, "小雪"),
	SNOW_MODERATE(10, "中雪"),
	SNOW_HEAVY(11, "大雪"),
	SNOW_STORM(12, "暴风雪"),
	CLOUDY_TO_RAIN(13, "多云转雨"),
	RAIN_WITH_THUNDER(14, "雷阵雨"),
	RAIN_SHOWER(15, "阵雨"),
	;
    private int value;
    private String desc;
    

    WeatherTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
        
    }

    
    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

	public static WeatherTypeEnum getByValue(int value) {
        for (WeatherTypeEnum examStatus : WeatherTypeEnum.values()) {
            if (examStatus.getValue() == value) {
                return examStatus;
            }
        }
        return null;
    }
	/**
	 * 获取Map集合
	 * @param eItem 不包含项
	 * @return
	 */
	public static Map<String,String> getEnumMap(int... eItem) {
		Map<String,String> resultMap= new LinkedHashMap<String,String>();
		for (WeatherTypeEnum item : WeatherTypeEnum.values()) {
			try{
				boolean hasE = false;
				for (int e : eItem){
					if(item.getValue()==e){
						hasE = true;
						break;
					}
				}
				if(!hasE){
					resultMap.put(item.getValue()+"", item.getDesc());
				}
			}catch(Exception ex){
			}
		}
		return resultMap;
	}
}
