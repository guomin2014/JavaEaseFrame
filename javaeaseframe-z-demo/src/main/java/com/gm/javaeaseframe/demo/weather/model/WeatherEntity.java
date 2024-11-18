/**
* 文件：WeatherEntity.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/

package com.gm.javaeaseframe.demo.weather.model;

import java.math.BigDecimal;
import java.util.Date;

import com.gm.javaeaseframe.core.context.model.BaseEntityLong;

/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherEntity  </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */
public class WeatherEntity extends BaseEntityLong{
	private static final long serialVersionUID = 1699003991915L;
	
/** 区域ID */
	private Long areaId;

	/** 城市名称 */
	private String city;

	/** 城市编号，用于同步天气信息 */
	private String cityCode;

	/** 日期，格式：yyyyMMdd */
	private Integer date;

	/** 天气发布时间，格式：yyyy-MM-dd HH:mm:ss */
	private String reportTime;

	/** 日出时间，格式：hh:mm */
	private String sunriseTime;

	/** 日落时间，格式：hh:mm */
	private String sunsetTime;

	/** 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherType;

	/** 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForNight;

	/** 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForReal;

	/** 天气(白天)，晴、多云、雨 */
	private String weather;

	/** 天气(夜间)，晴、多云、雨 */
	private String weatherForNight;

	/** 天气(实时)，晴、多云、雨 */
	private String weatherForReal;

	/** 空气温度(白天)，单位：摄氏度 */
	private BigDecimal airTemperature;

	/** 空气温度(夜间)，单位：摄氏度 */
	private BigDecimal airTemperatureForNight;

	/** 空气温度(实时)，单位：摄氏度 */
	private BigDecimal airTemperatureForReal;

	/** 空气湿度(白天)，单位：百分比 */
	private BigDecimal airHumidity;

	/** 空气湿度(实时)，单位：百分比 */
	private BigDecimal airHumidityForReal;

	/** 空气质量，优、良、差 */
	private String airQuality;

	/** 空气指数 */
	private BigDecimal airQI;

	/** 最高温(白天)，白天温度，单位：摄氏度 */
	private BigDecimal airMaxTemperature;

	/** 最低温(夜间)，夜间温度，单位：摄氏度 */
	private BigDecimal airMinTemperature;

	/** PM2.5，单位：ug/m3 */
	private BigDecimal airPM25Value;

	/** PM10，单位：ug/m3 */
	private BigDecimal airPM10Value;

	/** 风向(白天)，东风、西风 */
	private String windDirection;

	/** 风向(夜间)，东风、西风 */
	private String windDirectionForNight;

	/** 风向(实时)，东风、西风 */
	private String windDirectionForReal;

	/** 风力(白天) */
	private String windPower;

	/** 风力(夜间) */
	private String windPowerForNight;

	/** 风力(实时) */
	private String windPowerForReal;

	/** 大气压力，单位：hPa */
	private BigDecimal barometric;

	/** 紫外线，单位：W/㎡ */
	private BigDecimal ultravioletRays;

	/** 紫外线指数，单位：W/㎡ */
	private BigDecimal ultraVioletIndex;

	/** 天气描述 */
	private String remark;

	/** 天气扩展 */
	private String ext;

	/** 最后同步时间 */
	private Date lastSyncTime;
	public WeatherEntity(){
		
	}
/**
	 * 获取 区域ID
	 * @return areaId
	 */
	public Long getAreaId(){
		return this.areaId;
	}

	/**
	 * 设置 区域ID
	 * @param areaId
	 */
	public void setAreaId(Long areaId){
		this.areaId = areaId;
	}

	/**
	 * 获取 城市名称
	 * @return city
	 */
	public String getCity(){
		return this.city;
	}

	/**
	 * 设置 城市名称
	 * @param city
	 */
	public void setCity(String city){
		this.city = city;
	}

	/**
	 * 获取 城市编号，用于同步天气信息
	 * @return cityCode
	 */
	public String getCityCode(){
		return this.cityCode;
	}

	/**
	 * 设置 城市编号，用于同步天气信息
	 * @param cityCode
	 */
	public void setCityCode(String cityCode){
		this.cityCode = cityCode;
	}

	/**
	 * 获取 日期，格式：yyyyMMdd
	 * @return date
	 */
	public Integer getDate(){
		return this.date;
	}

	/**
	 * 设置 日期，格式：yyyyMMdd
	 * @param date
	 */
	public void setDate(Integer date){
		this.date = date;
	}

	/**
	 * 获取 天气发布时间，格式：yyyy-MM-dd HH:mm:ss
	 * @return reportTime
	 */
	public String getReportTime(){
		return this.reportTime;
	}

	/**
	 * 设置 天气发布时间，格式：yyyy-MM-dd HH:mm:ss
	 * @param reportTime
	 */
	public void setReportTime(String reportTime){
		this.reportTime = reportTime;
	}

	/**
	 * 获取 日出时间，格式：hh:mm
	 * @return sunriseTime
	 */
	public String getSunriseTime(){
		return this.sunriseTime;
	}

	/**
	 * 设置 日出时间，格式：hh:mm
	 * @param sunriseTime
	 */
	public void setSunriseTime(String sunriseTime){
		this.sunriseTime = sunriseTime;
	}

	/**
	 * 获取 日落时间，格式：hh:mm
	 * @return sunsetTime
	 */
	public String getSunsetTime(){
		return this.sunsetTime;
	}

	/**
	 * 设置 日落时间，格式：hh:mm
	 * @param sunsetTime
	 */
	public void setSunsetTime(String sunsetTime){
		this.sunsetTime = sunsetTime;
	}

	/**
	 * 获取 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherType
	 */
	public Integer getWeatherType(){
		return this.weatherType;
	}

	/**
	 * 设置 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherType
	 */
	public void setWeatherType(Integer weatherType){
		this.weatherType = weatherType;
	}

	/**
	 * 获取 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForNight
	 */
	public Integer getWeatherTypeForNight(){
		return this.weatherTypeForNight;
	}

	/**
	 * 设置 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForNight
	 */
	public void setWeatherTypeForNight(Integer weatherTypeForNight){
		this.weatherTypeForNight = weatherTypeForNight;
	}

	/**
	 * 获取 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForReal
	 */
	public Integer getWeatherTypeForReal(){
		return this.weatherTypeForReal;
	}

	/**
	 * 设置 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForReal
	 */
	public void setWeatherTypeForReal(Integer weatherTypeForReal){
		this.weatherTypeForReal = weatherTypeForReal;
	}

	/**
	 * 获取 天气(白天)，晴、多云、雨
	 * @return weather
	 */
	public String getWeather(){
		return this.weather;
	}

	/**
	 * 设置 天气(白天)，晴、多云、雨
	 * @param weather
	 */
	public void setWeather(String weather){
		this.weather = weather;
	}

	/**
	 * 获取 天气(夜间)，晴、多云、雨
	 * @return weatherForNight
	 */
	public String getWeatherForNight(){
		return this.weatherForNight;
	}

	/**
	 * 设置 天气(夜间)，晴、多云、雨
	 * @param weatherForNight
	 */
	public void setWeatherForNight(String weatherForNight){
		this.weatherForNight = weatherForNight;
	}

	/**
	 * 获取 天气(实时)，晴、多云、雨
	 * @return weatherForReal
	 */
	public String getWeatherForReal(){
		return this.weatherForReal;
	}

	/**
	 * 设置 天气(实时)，晴、多云、雨
	 * @param weatherForReal
	 */
	public void setWeatherForReal(String weatherForReal){
		this.weatherForReal = weatherForReal;
	}

	/**
	 * 获取 空气温度(白天)，单位：摄氏度
	 * @return airTemperature
	 */
	public BigDecimal getAirTemperature(){
		return this.airTemperature;
	}

	/**
	 * 设置 空气温度(白天)，单位：摄氏度
	 * @param airTemperature
	 */
	public void setAirTemperature(BigDecimal airTemperature){
		this.airTemperature = airTemperature;
	}

	/**
	 * 获取 空气温度(夜间)，单位：摄氏度
	 * @return airTemperatureForNight
	 */
	public BigDecimal getAirTemperatureForNight(){
		return this.airTemperatureForNight;
	}

	/**
	 * 设置 空气温度(夜间)，单位：摄氏度
	 * @param airTemperatureForNight
	 */
	public void setAirTemperatureForNight(BigDecimal airTemperatureForNight){
		this.airTemperatureForNight = airTemperatureForNight;
	}

	/**
	 * 获取 空气温度(实时)，单位：摄氏度
	 * @return airTemperatureForReal
	 */
	public BigDecimal getAirTemperatureForReal(){
		return this.airTemperatureForReal;
	}

	/**
	 * 设置 空气温度(实时)，单位：摄氏度
	 * @param airTemperatureForReal
	 */
	public void setAirTemperatureForReal(BigDecimal airTemperatureForReal){
		this.airTemperatureForReal = airTemperatureForReal;
	}

	/**
	 * 获取 空气湿度(白天)，单位：百分比
	 * @return airHumidity
	 */
	public BigDecimal getAirHumidity(){
		return this.airHumidity;
	}

	/**
	 * 设置 空气湿度(白天)，单位：百分比
	 * @param airHumidity
	 */
	public void setAirHumidity(BigDecimal airHumidity){
		this.airHumidity = airHumidity;
	}

	/**
	 * 获取 空气湿度(实时)，单位：百分比
	 * @return airHumidityForReal
	 */
	public BigDecimal getAirHumidityForReal(){
		return this.airHumidityForReal;
	}

	/**
	 * 设置 空气湿度(实时)，单位：百分比
	 * @param airHumidityForReal
	 */
	public void setAirHumidityForReal(BigDecimal airHumidityForReal){
		this.airHumidityForReal = airHumidityForReal;
	}

	/**
	 * 获取 空气质量，优、良、差
	 * @return airQuality
	 */
	public String getAirQuality(){
		return this.airQuality;
	}

	/**
	 * 设置 空气质量，优、良、差
	 * @param airQuality
	 */
	public void setAirQuality(String airQuality){
		this.airQuality = airQuality;
	}

	/**
	 * 获取 空气指数
	 * @return airQI
	 */
	public BigDecimal getAirQI(){
		return this.airQI;
	}

	/**
	 * 设置 空气指数
	 * @param airQI
	 */
	public void setAirQI(BigDecimal airQI){
		this.airQI = airQI;
	}

	/**
	 * 获取 最高温(白天)，白天温度，单位：摄氏度
	 * @return airMaxTemperature
	 */
	public BigDecimal getAirMaxTemperature(){
		return this.airMaxTemperature;
	}

	/**
	 * 设置 最高温(白天)，白天温度，单位：摄氏度
	 * @param airMaxTemperature
	 */
	public void setAirMaxTemperature(BigDecimal airMaxTemperature){
		this.airMaxTemperature = airMaxTemperature;
	}

	/**
	 * 获取 最低温(夜间)，夜间温度，单位：摄氏度
	 * @return airMinTemperature
	 */
	public BigDecimal getAirMinTemperature(){
		return this.airMinTemperature;
	}

	/**
	 * 设置 最低温(夜间)，夜间温度，单位：摄氏度
	 * @param airMinTemperature
	 */
	public void setAirMinTemperature(BigDecimal airMinTemperature){
		this.airMinTemperature = airMinTemperature;
	}

	/**
	 * 获取 PM2.5，单位：ug/m3
	 * @return airPM25Value
	 */
	public BigDecimal getAirPM25Value(){
		return this.airPM25Value;
	}

	/**
	 * 设置 PM2.5，单位：ug/m3
	 * @param airPM25Value
	 */
	public void setAirPM25Value(BigDecimal airPM25Value){
		this.airPM25Value = airPM25Value;
	}

	/**
	 * 获取 PM10，单位：ug/m3
	 * @return airPM10Value
	 */
	public BigDecimal getAirPM10Value(){
		return this.airPM10Value;
	}

	/**
	 * 设置 PM10，单位：ug/m3
	 * @param airPM10Value
	 */
	public void setAirPM10Value(BigDecimal airPM10Value){
		this.airPM10Value = airPM10Value;
	}

	/**
	 * 获取 风向(白天)，东风、西风
	 * @return windDirection
	 */
	public String getWindDirection(){
		return this.windDirection;
	}

	/**
	 * 设置 风向(白天)，东风、西风
	 * @param windDirection
	 */
	public void setWindDirection(String windDirection){
		this.windDirection = windDirection;
	}

	/**
	 * 获取 风向(夜间)，东风、西风
	 * @return windDirectionForNight
	 */
	public String getWindDirectionForNight(){
		return this.windDirectionForNight;
	}

	/**
	 * 设置 风向(夜间)，东风、西风
	 * @param windDirectionForNight
	 */
	public void setWindDirectionForNight(String windDirectionForNight){
		this.windDirectionForNight = windDirectionForNight;
	}

	/**
	 * 获取 风向(实时)，东风、西风
	 * @return windDirectionForReal
	 */
	public String getWindDirectionForReal(){
		return this.windDirectionForReal;
	}

	/**
	 * 设置 风向(实时)，东风、西风
	 * @param windDirectionForReal
	 */
	public void setWindDirectionForReal(String windDirectionForReal){
		this.windDirectionForReal = windDirectionForReal;
	}

	/**
	 * 获取 风力(白天)
	 * @return windPower
	 */
	public String getWindPower(){
		return this.windPower;
	}

	/**
	 * 设置 风力(白天)
	 * @param windPower
	 */
	public void setWindPower(String windPower){
		this.windPower = windPower;
	}

	/**
	 * 获取 风力(夜间)
	 * @return windPowerForNight
	 */
	public String getWindPowerForNight(){
		return this.windPowerForNight;
	}

	/**
	 * 设置 风力(夜间)
	 * @param windPowerForNight
	 */
	public void setWindPowerForNight(String windPowerForNight){
		this.windPowerForNight = windPowerForNight;
	}

	/**
	 * 获取 风力(实时)
	 * @return windPowerForReal
	 */
	public String getWindPowerForReal(){
		return this.windPowerForReal;
	}

	/**
	 * 设置 风力(实时)
	 * @param windPowerForReal
	 */
	public void setWindPowerForReal(String windPowerForReal){
		this.windPowerForReal = windPowerForReal;
	}

	/**
	 * 获取 大气压力，单位：hPa
	 * @return barometric
	 */
	public BigDecimal getBarometric(){
		return this.barometric;
	}

	/**
	 * 设置 大气压力，单位：hPa
	 * @param barometric
	 */
	public void setBarometric(BigDecimal barometric){
		this.barometric = barometric;
	}

	/**
	 * 获取 紫外线，单位：W/㎡
	 * @return ultravioletRays
	 */
	public BigDecimal getUltravioletRays(){
		return this.ultravioletRays;
	}

	/**
	 * 设置 紫外线，单位：W/㎡
	 * @param ultravioletRays
	 */
	public void setUltravioletRays(BigDecimal ultravioletRays){
		this.ultravioletRays = ultravioletRays;
	}

	/**
	 * 获取 紫外线指数，单位：W/㎡
	 * @return ultraVioletIndex
	 */
	public BigDecimal getUltraVioletIndex(){
		return this.ultraVioletIndex;
	}

	/**
	 * 设置 紫外线指数，单位：W/㎡
	 * @param ultraVioletIndex
	 */
	public void setUltraVioletIndex(BigDecimal ultraVioletIndex){
		this.ultraVioletIndex = ultraVioletIndex;
	}

	/**
	 * 获取 天气描述
	 * @return remark
	 */
	public String getRemark(){
		return this.remark;
	}

	/**
	 * 设置 天气描述
	 * @param remark
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}

	/**
	 * 获取 天气扩展
	 * @return ext
	 */
	public String getExt(){
		return this.ext;
	}

	/**
	 * 设置 天气扩展
	 * @param ext
	 */
	public void setExt(String ext){
		this.ext = ext;
	}

	/**
	 * 获取 最后同步时间
	 * @return lastSyncTime
	 */
	public Date getLastSyncTime(){
		return this.lastSyncTime;
	}

	/**
	 * 设置 最后同步时间
	 * @param lastSyncTime
	 */
	public void setLastSyncTime(Date lastSyncTime){
		this.lastSyncTime = lastSyncTime;
	}
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof WeatherEntity) {
			WeatherEntity tmp = (WeatherEntity) obj;
			if (this.getId().longValue() == tmp.getId().longValue()) {
				return true;
			}
		}
		return false;
	}
	public String toString(){
StringBuilder sb = new StringBuilder("");
		sb.append("id:").append(getId())
		  .append(",areaId:").append(getAreaId())
		  .append(",city:").append(getCity())
		  .append(",cityCode:").append(getCityCode())
		  .append(",date:").append(getDate())
		  .append(",reportTime:").append(getReportTime())
		  .append(",sunriseTime:").append(getSunriseTime())
		  .append(",sunsetTime:").append(getSunsetTime())
		  .append(",weatherType:").append(getWeatherType())
		  .append(",weatherTypeForNight:").append(getWeatherTypeForNight())
		  .append(",weatherTypeForReal:").append(getWeatherTypeForReal())
		  .append(",weather:").append(getWeather())
		  .append(",weatherForNight:").append(getWeatherForNight())
		  .append(",weatherForReal:").append(getWeatherForReal())
		  .append(",airTemperature:").append(getAirTemperature())
		  .append(",airTemperatureForNight:").append(getAirTemperatureForNight())
		  .append(",airTemperatureForReal:").append(getAirTemperatureForReal())
		  .append(",airHumidity:").append(getAirHumidity())
		  .append(",airHumidityForReal:").append(getAirHumidityForReal())
		  .append(",airQuality:").append(getAirQuality())
		  .append(",airQI:").append(getAirQI())
		  .append(",airMaxTemperature:").append(getAirMaxTemperature())
		  .append(",airMinTemperature:").append(getAirMinTemperature())
		  .append(",airPM25Value:").append(getAirPM25Value())
		  .append(",airPM10Value:").append(getAirPM10Value())
		  .append(",windDirection:").append(getWindDirection())
		  .append(",windDirectionForNight:").append(getWindDirectionForNight())
		  .append(",windDirectionForReal:").append(getWindDirectionForReal())
		  .append(",windPower:").append(getWindPower())
		  .append(",windPowerForNight:").append(getWindPowerForNight())
		  .append(",windPowerForReal:").append(getWindPowerForReal())
		  .append(",barometric:").append(getBarometric())
		  .append(",ultravioletRays:").append(getUltravioletRays())
		  .append(",ultraVioletIndex:").append(getUltraVioletIndex())
		  .append(",remark:").append(getRemark())
		  .append(",ext:").append(getExt())
		  .append(",lastSyncTime:").append(getLastSyncTime());
		return sb.toString();
	}
	public void initAttrValue(){
this.areaId = null;
		this.city = null;
		this.cityCode = null;
		this.date = null;
		this.reportTime = null;
		this.sunriseTime = null;
		this.sunsetTime = null;
		this.weatherType = null;
		this.weatherTypeForNight = null;
		this.weatherTypeForReal = null;
		this.weather = null;
		this.weatherForNight = null;
		this.weatherForReal = null;
		this.airTemperature = null;
		this.airTemperatureForNight = null;
		this.airTemperatureForReal = null;
		this.airHumidity = null;
		this.airHumidityForReal = null;
		this.airQuality = null;
		this.airQI = null;
		this.airMaxTemperature = null;
		this.airMinTemperature = null;
		this.airPM25Value = null;
		this.airPM10Value = null;
		this.windDirection = null;
		this.windDirectionForNight = null;
		this.windDirectionForReal = null;
		this.windPower = null;
		this.windPowerForNight = null;
		this.windPowerForReal = null;
		this.barometric = null;
		this.ultravioletRays = null;
		this.ultraVioletIndex = null;
		this.remark = null;
		this.ext = null;
		this.lastSyncTime = null;
	}
}