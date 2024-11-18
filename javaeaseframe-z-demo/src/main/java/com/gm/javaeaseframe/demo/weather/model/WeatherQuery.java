/**
* 文件：WeatherQuery.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/

package com.gm.javaeaseframe.demo.weather.model;
import java.math.BigDecimal;
import java.util.List;
/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherQuery  </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */
public class WeatherQuery extends WeatherEntity{
	private static final long serialVersionUID = 1699003991921L;
	/** 开始 序号，主键，自增长 */
	private Long idStart;

	/** 结束 序号，主键，自增长 */
	private Long idEnd;

	/** 增加 序号，主键，自增长 */
	private Long idIncrement;

	/** 序号，主键，自增长 */
	private List<Long> idList;

	/** 开始 区域ID */
	private Long areaIdStart;

	/** 结束 区域ID */
	private Long areaIdEnd;

	/** 增加 区域ID */
	private Long areaIdIncrement;

	/** 区域ID */
	private List<Long> areaIdList;

	/** 城市名称 */
	private List<String> cityList;

	/** 城市编号，用于同步天气信息 */
	private List<String> cityCodeList;

	/** 开始 日期，格式：yyyyMMdd */
	private Integer dateStart;

	/** 结束 日期，格式：yyyyMMdd */
	private Integer dateEnd;

	/** 增加 日期，格式：yyyyMMdd */
	private Integer dateIncrement;

	/** 日期，格式：yyyyMMdd */
	private List<Integer> dateList;

	/** 天气发布时间，格式：yyyy-MM-dd HH:mm:ss */
	private List<String> reportTimeList;

	/** 日出时间，格式：hh:mm */
	private List<String> sunriseTimeList;

	/** 日落时间，格式：hh:mm */
	private List<String> sunsetTimeList;

	/** 开始 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeStart;

	/** 结束 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeEnd;

	/** 增加 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeIncrement;

	/** 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private List<Integer> weatherTypeList;

	/** 开始 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForNightStart;

	/** 结束 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForNightEnd;

	/** 增加 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForNightIncrement;

	/** 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private List<Integer> weatherTypeForNightList;

	/** 开始 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForRealStart;

	/** 结束 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForRealEnd;

	/** 增加 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private Integer weatherTypeForRealIncrement;

	/** 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨 */
	private List<Integer> weatherTypeForRealList;

	/** 天气(白天)，晴、多云、雨 */
	private List<String> weatherList;

	/** 天气(夜间)，晴、多云、雨 */
	private List<String> weatherForNightList;

	/** 天气(实时)，晴、多云、雨 */
	private List<String> weatherForRealList;

	/** 开始 空气温度(白天)，单位：摄氏度 */
	private BigDecimal airTemperatureStart;

	/** 结束 空气温度(白天)，单位：摄氏度 */
	private BigDecimal airTemperatureEnd;

	/** 增加 空气温度(白天)，单位：摄氏度 */
	private BigDecimal airTemperatureIncrement;

	/** 空气温度(白天)，单位：摄氏度 */
	private List<BigDecimal> airTemperatureList;

	/** 开始 空气温度(夜间)，单位：摄氏度 */
	private BigDecimal airTemperatureForNightStart;

	/** 结束 空气温度(夜间)，单位：摄氏度 */
	private BigDecimal airTemperatureForNightEnd;

	/** 增加 空气温度(夜间)，单位：摄氏度 */
	private BigDecimal airTemperatureForNightIncrement;

	/** 空气温度(夜间)，单位：摄氏度 */
	private List<BigDecimal> airTemperatureForNightList;

	/** 开始 空气温度(实时)，单位：摄氏度 */
	private BigDecimal airTemperatureForRealStart;

	/** 结束 空气温度(实时)，单位：摄氏度 */
	private BigDecimal airTemperatureForRealEnd;

	/** 增加 空气温度(实时)，单位：摄氏度 */
	private BigDecimal airTemperatureForRealIncrement;

	/** 空气温度(实时)，单位：摄氏度 */
	private List<BigDecimal> airTemperatureForRealList;

	/** 开始 空气湿度(白天)，单位：百分比 */
	private BigDecimal airHumidityStart;

	/** 结束 空气湿度(白天)，单位：百分比 */
	private BigDecimal airHumidityEnd;

	/** 增加 空气湿度(白天)，单位：百分比 */
	private BigDecimal airHumidityIncrement;

	/** 空气湿度(白天)，单位：百分比 */
	private List<BigDecimal> airHumidityList;

	/** 开始 空气湿度(实时)，单位：百分比 */
	private BigDecimal airHumidityForRealStart;

	/** 结束 空气湿度(实时)，单位：百分比 */
	private BigDecimal airHumidityForRealEnd;

	/** 增加 空气湿度(实时)，单位：百分比 */
	private BigDecimal airHumidityForRealIncrement;

	/** 空气湿度(实时)，单位：百分比 */
	private List<BigDecimal> airHumidityForRealList;

	/** 空气质量，优、良、差 */
	private List<String> airQualityList;

	/** 开始 空气指数 */
	private BigDecimal airQIStart;

	/** 结束 空气指数 */
	private BigDecimal airQIEnd;

	/** 增加 空气指数 */
	private BigDecimal airQIIncrement;

	/** 空气指数 */
	private List<BigDecimal> airQIList;

	/** 开始 最高温(白天)，白天温度，单位：摄氏度 */
	private BigDecimal airMaxTemperatureStart;

	/** 结束 最高温(白天)，白天温度，单位：摄氏度 */
	private BigDecimal airMaxTemperatureEnd;

	/** 增加 最高温(白天)，白天温度，单位：摄氏度 */
	private BigDecimal airMaxTemperatureIncrement;

	/** 最高温(白天)，白天温度，单位：摄氏度 */
	private List<BigDecimal> airMaxTemperatureList;

	/** 开始 最低温(夜间)，夜间温度，单位：摄氏度 */
	private BigDecimal airMinTemperatureStart;

	/** 结束 最低温(夜间)，夜间温度，单位：摄氏度 */
	private BigDecimal airMinTemperatureEnd;

	/** 增加 最低温(夜间)，夜间温度，单位：摄氏度 */
	private BigDecimal airMinTemperatureIncrement;

	/** 最低温(夜间)，夜间温度，单位：摄氏度 */
	private List<BigDecimal> airMinTemperatureList;

	/** 开始 PM2.5，单位：ug/m3 */
	private BigDecimal airPM25ValueStart;

	/** 结束 PM2.5，单位：ug/m3 */
	private BigDecimal airPM25ValueEnd;

	/** 增加 PM2.5，单位：ug/m3 */
	private BigDecimal airPM25ValueIncrement;

	/** PM2.5，单位：ug/m3 */
	private List<BigDecimal> airPM25ValueList;

	/** 开始 PM10，单位：ug/m3 */
	private BigDecimal airPM10ValueStart;

	/** 结束 PM10，单位：ug/m3 */
	private BigDecimal airPM10ValueEnd;

	/** 增加 PM10，单位：ug/m3 */
	private BigDecimal airPM10ValueIncrement;

	/** PM10，单位：ug/m3 */
	private List<BigDecimal> airPM10ValueList;

	/** 风向(白天)，东风、西风 */
	private List<String> windDirectionList;

	/** 风向(夜间)，东风、西风 */
	private List<String> windDirectionForNightList;

	/** 风向(实时)，东风、西风 */
	private List<String> windDirectionForRealList;

	/** 风力(白天) */
	private List<String> windPowerList;

	/** 风力(夜间) */
	private List<String> windPowerForNightList;

	/** 风力(实时) */
	private List<String> windPowerForRealList;

	/** 开始 大气压力，单位：hPa */
	private BigDecimal barometricStart;

	/** 结束 大气压力，单位：hPa */
	private BigDecimal barometricEnd;

	/** 增加 大气压力，单位：hPa */
	private BigDecimal barometricIncrement;

	/** 大气压力，单位：hPa */
	private List<BigDecimal> barometricList;

	/** 开始 紫外线，单位：W/㎡ */
	private BigDecimal ultravioletRaysStart;

	/** 结束 紫外线，单位：W/㎡ */
	private BigDecimal ultravioletRaysEnd;

	/** 增加 紫外线，单位：W/㎡ */
	private BigDecimal ultravioletRaysIncrement;

	/** 紫外线，单位：W/㎡ */
	private List<BigDecimal> ultravioletRaysList;

	/** 开始 紫外线指数，单位：W/㎡ */
	private BigDecimal ultraVioletIndexStart;

	/** 结束 紫外线指数，单位：W/㎡ */
	private BigDecimal ultraVioletIndexEnd;

	/** 增加 紫外线指数，单位：W/㎡ */
	private BigDecimal ultraVioletIndexIncrement;

	/** 紫外线指数，单位：W/㎡ */
	private List<BigDecimal> ultraVioletIndexList;

	/** 天气描述 */
	private List<String> remarkList;

	/** 天气扩展 */
	private List<String> extList;

	/** 开始 最后同步时间 */
	private String lastSyncTimeStart;

	/** 结束 最后同步时间 */
	private String lastSyncTimeEnd;

	/** OR条件集合，列表项之间是OR，项内容之间是AND，如：(list[0].1 and list[0].2) or (list[1].3 and list[1].4) */
	private List<WeatherQuery> orConditionList;

	/** AND条件集合，列表项之间是AND，项内容之间是OR，如：(list[0].1 or list[0].2) and (list[1].3 or list[1].4) */
	private List<WeatherQuery> andConditionList;


	public WeatherQuery(){
	}
	/**
	 * 获取 开始 序号，主键，自增长
	 * @return idStart
	 */
	public Long getIdStart(){
		return this.idStart;
	}

	/**
	 * 设置 开始 序号，主键，自增长
	 * @param idStart
	 */
	public void setIdStart(Long idStart){
		this.idStart = idStart;
	}

	/**
	 * 获取 结束 序号，主键，自增长
	 * @return idEnd
	 */
	public Long getIdEnd(){
		return this.idEnd;
	}

	/**
	 * 设置 结束 序号，主键，自增长
	 * @param idEnd
	 */
	public void setIdEnd(Long idEnd){
		this.idEnd = idEnd;
	}

	/**
	 * 获取 增加 序号，主键，自增长
	 * @return idIncrement
	 */
	public Long getIdIncrement(){
		return this.idIncrement;
	}

	/**
	 * 设置 增加 序号，主键，自增长
	 * @param idIncrement
	 */
	public void setIdIncrement(Long idIncrement){
		this.idIncrement = idIncrement;
	}

	/**
	 * 获取 序号，主键，自增长
	 * @return idList
	 */
	public List<Long> getIdList(){
		return this.idList;
	}

	/**
	 * 设置 序号，主键，自增长
	 * @param idList
	 */
	public void setIdList(List<Long> idList){
		this.idList = idList;
	}

	/**
	 * 获取 开始 区域ID
	 * @return areaIdStart
	 */
	public Long getAreaIdStart(){
		return this.areaIdStart;
	}

	/**
	 * 设置 开始 区域ID
	 * @param areaIdStart
	 */
	public void setAreaIdStart(Long areaIdStart){
		this.areaIdStart = areaIdStart;
	}

	/**
	 * 获取 结束 区域ID
	 * @return areaIdEnd
	 */
	public Long getAreaIdEnd(){
		return this.areaIdEnd;
	}

	/**
	 * 设置 结束 区域ID
	 * @param areaIdEnd
	 */
	public void setAreaIdEnd(Long areaIdEnd){
		this.areaIdEnd = areaIdEnd;
	}

	/**
	 * 获取 增加 区域ID
	 * @return areaIdIncrement
	 */
	public Long getAreaIdIncrement(){
		return this.areaIdIncrement;
	}

	/**
	 * 设置 增加 区域ID
	 * @param areaIdIncrement
	 */
	public void setAreaIdIncrement(Long areaIdIncrement){
		this.areaIdIncrement = areaIdIncrement;
	}

	/**
	 * 获取 区域ID
	 * @return areaIdList
	 */
	public List<Long> getAreaIdList(){
		return this.areaIdList;
	}

	/**
	 * 设置 区域ID
	 * @param areaIdList
	 */
	public void setAreaIdList(List<Long> areaIdList){
		this.areaIdList = areaIdList;
	}

	/**
	 * 获取 城市名称
	 * @return cityList
	 */
	public List<String> getCityList(){
		return this.cityList;
	}

	/**
	 * 设置 城市名称
	 * @param cityList
	 */
	public void setCityList(List<String> cityList){
		this.cityList = cityList;
	}

	/**
	 * 获取 城市编号，用于同步天气信息
	 * @return cityCodeList
	 */
	public List<String> getCityCodeList(){
		return this.cityCodeList;
	}

	/**
	 * 设置 城市编号，用于同步天气信息
	 * @param cityCodeList
	 */
	public void setCityCodeList(List<String> cityCodeList){
		this.cityCodeList = cityCodeList;
	}

	/**
	 * 获取 开始 日期，格式：yyyyMMdd
	 * @return dateStart
	 */
	public Integer getDateStart(){
		return this.dateStart;
	}

	/**
	 * 设置 开始 日期，格式：yyyyMMdd
	 * @param dateStart
	 */
	public void setDateStart(Integer dateStart){
		this.dateStart = dateStart;
	}

	/**
	 * 获取 结束 日期，格式：yyyyMMdd
	 * @return dateEnd
	 */
	public Integer getDateEnd(){
		return this.dateEnd;
	}

	/**
	 * 设置 结束 日期，格式：yyyyMMdd
	 * @param dateEnd
	 */
	public void setDateEnd(Integer dateEnd){
		this.dateEnd = dateEnd;
	}

	/**
	 * 获取 增加 日期，格式：yyyyMMdd
	 * @return dateIncrement
	 */
	public Integer getDateIncrement(){
		return this.dateIncrement;
	}

	/**
	 * 设置 增加 日期，格式：yyyyMMdd
	 * @param dateIncrement
	 */
	public void setDateIncrement(Integer dateIncrement){
		this.dateIncrement = dateIncrement;
	}

	/**
	 * 获取 日期，格式：yyyyMMdd
	 * @return dateList
	 */
	public List<Integer> getDateList(){
		return this.dateList;
	}

	/**
	 * 设置 日期，格式：yyyyMMdd
	 * @param dateList
	 */
	public void setDateList(List<Integer> dateList){
		this.dateList = dateList;
	}

	/**
	 * 获取 天气发布时间，格式：yyyy-MM-dd HH:mm:ss
	 * @return reportTimeList
	 */
	public List<String> getReportTimeList(){
		return this.reportTimeList;
	}

	/**
	 * 设置 天气发布时间，格式：yyyy-MM-dd HH:mm:ss
	 * @param reportTimeList
	 */
	public void setReportTimeList(List<String> reportTimeList){
		this.reportTimeList = reportTimeList;
	}

	/**
	 * 获取 日出时间，格式：hh:mm
	 * @return sunriseTimeList
	 */
	public List<String> getSunriseTimeList(){
		return this.sunriseTimeList;
	}

	/**
	 * 设置 日出时间，格式：hh:mm
	 * @param sunriseTimeList
	 */
	public void setSunriseTimeList(List<String> sunriseTimeList){
		this.sunriseTimeList = sunriseTimeList;
	}

	/**
	 * 获取 日落时间，格式：hh:mm
	 * @return sunsetTimeList
	 */
	public List<String> getSunsetTimeList(){
		return this.sunsetTimeList;
	}

	/**
	 * 设置 日落时间，格式：hh:mm
	 * @param sunsetTimeList
	 */
	public void setSunsetTimeList(List<String> sunsetTimeList){
		this.sunsetTimeList = sunsetTimeList;
	}

	/**
	 * 获取 开始 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeStart
	 */
	public Integer getWeatherTypeStart(){
		return this.weatherTypeStart;
	}

	/**
	 * 设置 开始 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeStart
	 */
	public void setWeatherTypeStart(Integer weatherTypeStart){
		this.weatherTypeStart = weatherTypeStart;
	}

	/**
	 * 获取 结束 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeEnd
	 */
	public Integer getWeatherTypeEnd(){
		return this.weatherTypeEnd;
	}

	/**
	 * 设置 结束 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeEnd
	 */
	public void setWeatherTypeEnd(Integer weatherTypeEnd){
		this.weatherTypeEnd = weatherTypeEnd;
	}

	/**
	 * 获取 增加 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeIncrement
	 */
	public Integer getWeatherTypeIncrement(){
		return this.weatherTypeIncrement;
	}

	/**
	 * 设置 增加 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeIncrement
	 */
	public void setWeatherTypeIncrement(Integer weatherTypeIncrement){
		this.weatherTypeIncrement = weatherTypeIncrement;
	}

	/**
	 * 获取 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeList
	 */
	public List<Integer> getWeatherTypeList(){
		return this.weatherTypeList;
	}

	/**
	 * 设置 天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeList
	 */
	public void setWeatherTypeList(List<Integer> weatherTypeList){
		this.weatherTypeList = weatherTypeList;
	}

	/**
	 * 获取 开始 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForNightStart
	 */
	public Integer getWeatherTypeForNightStart(){
		return this.weatherTypeForNightStart;
	}

	/**
	 * 设置 开始 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForNightStart
	 */
	public void setWeatherTypeForNightStart(Integer weatherTypeForNightStart){
		this.weatherTypeForNightStart = weatherTypeForNightStart;
	}

	/**
	 * 获取 结束 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForNightEnd
	 */
	public Integer getWeatherTypeForNightEnd(){
		return this.weatherTypeForNightEnd;
	}

	/**
	 * 设置 结束 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForNightEnd
	 */
	public void setWeatherTypeForNightEnd(Integer weatherTypeForNightEnd){
		this.weatherTypeForNightEnd = weatherTypeForNightEnd;
	}

	/**
	 * 获取 增加 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForNightIncrement
	 */
	public Integer getWeatherTypeForNightIncrement(){
		return this.weatherTypeForNightIncrement;
	}

	/**
	 * 设置 增加 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForNightIncrement
	 */
	public void setWeatherTypeForNightIncrement(Integer weatherTypeForNightIncrement){
		this.weatherTypeForNightIncrement = weatherTypeForNightIncrement;
	}

	/**
	 * 获取 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForNightList
	 */
	public List<Integer> getWeatherTypeForNightList(){
		return this.weatherTypeForNightList;
	}

	/**
	 * 设置 天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForNightList
	 */
	public void setWeatherTypeForNightList(List<Integer> weatherTypeForNightList){
		this.weatherTypeForNightList = weatherTypeForNightList;
	}

	/**
	 * 获取 开始 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForRealStart
	 */
	public Integer getWeatherTypeForRealStart(){
		return this.weatherTypeForRealStart;
	}

	/**
	 * 设置 开始 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForRealStart
	 */
	public void setWeatherTypeForRealStart(Integer weatherTypeForRealStart){
		this.weatherTypeForRealStart = weatherTypeForRealStart;
	}

	/**
	 * 获取 结束 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForRealEnd
	 */
	public Integer getWeatherTypeForRealEnd(){
		return this.weatherTypeForRealEnd;
	}

	/**
	 * 设置 结束 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForRealEnd
	 */
	public void setWeatherTypeForRealEnd(Integer weatherTypeForRealEnd){
		this.weatherTypeForRealEnd = weatherTypeForRealEnd;
	}

	/**
	 * 获取 增加 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForRealIncrement
	 */
	public Integer getWeatherTypeForRealIncrement(){
		return this.weatherTypeForRealIncrement;
	}

	/**
	 * 设置 增加 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForRealIncrement
	 */
	public void setWeatherTypeForRealIncrement(Integer weatherTypeForRealIncrement){
		this.weatherTypeForRealIncrement = weatherTypeForRealIncrement;
	}

	/**
	 * 获取 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @return weatherTypeForRealList
	 */
	public List<Integer> getWeatherTypeForRealList(){
		return this.weatherTypeForRealList;
	}

	/**
	 * 设置 天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨
	 * @param weatherTypeForRealList
	 */
	public void setWeatherTypeForRealList(List<Integer> weatherTypeForRealList){
		this.weatherTypeForRealList = weatherTypeForRealList;
	}

	/**
	 * 获取 天气(白天)，晴、多云、雨
	 * @return weatherList
	 */
	public List<String> getWeatherList(){
		return this.weatherList;
	}

	/**
	 * 设置 天气(白天)，晴、多云、雨
	 * @param weatherList
	 */
	public void setWeatherList(List<String> weatherList){
		this.weatherList = weatherList;
	}

	/**
	 * 获取 天气(夜间)，晴、多云、雨
	 * @return weatherForNightList
	 */
	public List<String> getWeatherForNightList(){
		return this.weatherForNightList;
	}

	/**
	 * 设置 天气(夜间)，晴、多云、雨
	 * @param weatherForNightList
	 */
	public void setWeatherForNightList(List<String> weatherForNightList){
		this.weatherForNightList = weatherForNightList;
	}

	/**
	 * 获取 天气(实时)，晴、多云、雨
	 * @return weatherForRealList
	 */
	public List<String> getWeatherForRealList(){
		return this.weatherForRealList;
	}

	/**
	 * 设置 天气(实时)，晴、多云、雨
	 * @param weatherForRealList
	 */
	public void setWeatherForRealList(List<String> weatherForRealList){
		this.weatherForRealList = weatherForRealList;
	}

	/**
	 * 获取 开始 空气温度(白天)，单位：摄氏度
	 * @return airTemperatureStart
	 */
	public BigDecimal getAirTemperatureStart(){
		return this.airTemperatureStart;
	}

	/**
	 * 设置 开始 空气温度(白天)，单位：摄氏度
	 * @param airTemperatureStart
	 */
	public void setAirTemperatureStart(BigDecimal airTemperatureStart){
		this.airTemperatureStart = airTemperatureStart;
	}

	/**
	 * 获取 结束 空气温度(白天)，单位：摄氏度
	 * @return airTemperatureEnd
	 */
	public BigDecimal getAirTemperatureEnd(){
		return this.airTemperatureEnd;
	}

	/**
	 * 设置 结束 空气温度(白天)，单位：摄氏度
	 * @param airTemperatureEnd
	 */
	public void setAirTemperatureEnd(BigDecimal airTemperatureEnd){
		this.airTemperatureEnd = airTemperatureEnd;
	}

	/**
	 * 获取 增加 空气温度(白天)，单位：摄氏度
	 * @return airTemperatureIncrement
	 */
	public BigDecimal getAirTemperatureIncrement(){
		return this.airTemperatureIncrement;
	}

	/**
	 * 设置 增加 空气温度(白天)，单位：摄氏度
	 * @param airTemperatureIncrement
	 */
	public void setAirTemperatureIncrement(BigDecimal airTemperatureIncrement){
		this.airTemperatureIncrement = airTemperatureIncrement;
	}

	/**
	 * 获取 空气温度(白天)，单位：摄氏度
	 * @return airTemperatureList
	 */
	public List<BigDecimal> getAirTemperatureList(){
		return this.airTemperatureList;
	}

	/**
	 * 设置 空气温度(白天)，单位：摄氏度
	 * @param airTemperatureList
	 */
	public void setAirTemperatureList(List<BigDecimal> airTemperatureList){
		this.airTemperatureList = airTemperatureList;
	}

	/**
	 * 获取 开始 空气温度(夜间)，单位：摄氏度
	 * @return airTemperatureForNightStart
	 */
	public BigDecimal getAirTemperatureForNightStart(){
		return this.airTemperatureForNightStart;
	}

	/**
	 * 设置 开始 空气温度(夜间)，单位：摄氏度
	 * @param airTemperatureForNightStart
	 */
	public void setAirTemperatureForNightStart(BigDecimal airTemperatureForNightStart){
		this.airTemperatureForNightStart = airTemperatureForNightStart;
	}

	/**
	 * 获取 结束 空气温度(夜间)，单位：摄氏度
	 * @return airTemperatureForNightEnd
	 */
	public BigDecimal getAirTemperatureForNightEnd(){
		return this.airTemperatureForNightEnd;
	}

	/**
	 * 设置 结束 空气温度(夜间)，单位：摄氏度
	 * @param airTemperatureForNightEnd
	 */
	public void setAirTemperatureForNightEnd(BigDecimal airTemperatureForNightEnd){
		this.airTemperatureForNightEnd = airTemperatureForNightEnd;
	}

	/**
	 * 获取 增加 空气温度(夜间)，单位：摄氏度
	 * @return airTemperatureForNightIncrement
	 */
	public BigDecimal getAirTemperatureForNightIncrement(){
		return this.airTemperatureForNightIncrement;
	}

	/**
	 * 设置 增加 空气温度(夜间)，单位：摄氏度
	 * @param airTemperatureForNightIncrement
	 */
	public void setAirTemperatureForNightIncrement(BigDecimal airTemperatureForNightIncrement){
		this.airTemperatureForNightIncrement = airTemperatureForNightIncrement;
	}

	/**
	 * 获取 空气温度(夜间)，单位：摄氏度
	 * @return airTemperatureForNightList
	 */
	public List<BigDecimal> getAirTemperatureForNightList(){
		return this.airTemperatureForNightList;
	}

	/**
	 * 设置 空气温度(夜间)，单位：摄氏度
	 * @param airTemperatureForNightList
	 */
	public void setAirTemperatureForNightList(List<BigDecimal> airTemperatureForNightList){
		this.airTemperatureForNightList = airTemperatureForNightList;
	}

	/**
	 * 获取 开始 空气温度(实时)，单位：摄氏度
	 * @return airTemperatureForRealStart
	 */
	public BigDecimal getAirTemperatureForRealStart(){
		return this.airTemperatureForRealStart;
	}

	/**
	 * 设置 开始 空气温度(实时)，单位：摄氏度
	 * @param airTemperatureForRealStart
	 */
	public void setAirTemperatureForRealStart(BigDecimal airTemperatureForRealStart){
		this.airTemperatureForRealStart = airTemperatureForRealStart;
	}

	/**
	 * 获取 结束 空气温度(实时)，单位：摄氏度
	 * @return airTemperatureForRealEnd
	 */
	public BigDecimal getAirTemperatureForRealEnd(){
		return this.airTemperatureForRealEnd;
	}

	/**
	 * 设置 结束 空气温度(实时)，单位：摄氏度
	 * @param airTemperatureForRealEnd
	 */
	public void setAirTemperatureForRealEnd(BigDecimal airTemperatureForRealEnd){
		this.airTemperatureForRealEnd = airTemperatureForRealEnd;
	}

	/**
	 * 获取 增加 空气温度(实时)，单位：摄氏度
	 * @return airTemperatureForRealIncrement
	 */
	public BigDecimal getAirTemperatureForRealIncrement(){
		return this.airTemperatureForRealIncrement;
	}

	/**
	 * 设置 增加 空气温度(实时)，单位：摄氏度
	 * @param airTemperatureForRealIncrement
	 */
	public void setAirTemperatureForRealIncrement(BigDecimal airTemperatureForRealIncrement){
		this.airTemperatureForRealIncrement = airTemperatureForRealIncrement;
	}

	/**
	 * 获取 空气温度(实时)，单位：摄氏度
	 * @return airTemperatureForRealList
	 */
	public List<BigDecimal> getAirTemperatureForRealList(){
		return this.airTemperatureForRealList;
	}

	/**
	 * 设置 空气温度(实时)，单位：摄氏度
	 * @param airTemperatureForRealList
	 */
	public void setAirTemperatureForRealList(List<BigDecimal> airTemperatureForRealList){
		this.airTemperatureForRealList = airTemperatureForRealList;
	}

	/**
	 * 获取 开始 空气湿度(白天)，单位：百分比
	 * @return airHumidityStart
	 */
	public BigDecimal getAirHumidityStart(){
		return this.airHumidityStart;
	}

	/**
	 * 设置 开始 空气湿度(白天)，单位：百分比
	 * @param airHumidityStart
	 */
	public void setAirHumidityStart(BigDecimal airHumidityStart){
		this.airHumidityStart = airHumidityStart;
	}

	/**
	 * 获取 结束 空气湿度(白天)，单位：百分比
	 * @return airHumidityEnd
	 */
	public BigDecimal getAirHumidityEnd(){
		return this.airHumidityEnd;
	}

	/**
	 * 设置 结束 空气湿度(白天)，单位：百分比
	 * @param airHumidityEnd
	 */
	public void setAirHumidityEnd(BigDecimal airHumidityEnd){
		this.airHumidityEnd = airHumidityEnd;
	}

	/**
	 * 获取 增加 空气湿度(白天)，单位：百分比
	 * @return airHumidityIncrement
	 */
	public BigDecimal getAirHumidityIncrement(){
		return this.airHumidityIncrement;
	}

	/**
	 * 设置 增加 空气湿度(白天)，单位：百分比
	 * @param airHumidityIncrement
	 */
	public void setAirHumidityIncrement(BigDecimal airHumidityIncrement){
		this.airHumidityIncrement = airHumidityIncrement;
	}

	/**
	 * 获取 空气湿度(白天)，单位：百分比
	 * @return airHumidityList
	 */
	public List<BigDecimal> getAirHumidityList(){
		return this.airHumidityList;
	}

	/**
	 * 设置 空气湿度(白天)，单位：百分比
	 * @param airHumidityList
	 */
	public void setAirHumidityList(List<BigDecimal> airHumidityList){
		this.airHumidityList = airHumidityList;
	}

	/**
	 * 获取 开始 空气湿度(实时)，单位：百分比
	 * @return airHumidityForRealStart
	 */
	public BigDecimal getAirHumidityForRealStart(){
		return this.airHumidityForRealStart;
	}

	/**
	 * 设置 开始 空气湿度(实时)，单位：百分比
	 * @param airHumidityForRealStart
	 */
	public void setAirHumidityForRealStart(BigDecimal airHumidityForRealStart){
		this.airHumidityForRealStart = airHumidityForRealStart;
	}

	/**
	 * 获取 结束 空气湿度(实时)，单位：百分比
	 * @return airHumidityForRealEnd
	 */
	public BigDecimal getAirHumidityForRealEnd(){
		return this.airHumidityForRealEnd;
	}

	/**
	 * 设置 结束 空气湿度(实时)，单位：百分比
	 * @param airHumidityForRealEnd
	 */
	public void setAirHumidityForRealEnd(BigDecimal airHumidityForRealEnd){
		this.airHumidityForRealEnd = airHumidityForRealEnd;
	}

	/**
	 * 获取 增加 空气湿度(实时)，单位：百分比
	 * @return airHumidityForRealIncrement
	 */
	public BigDecimal getAirHumidityForRealIncrement(){
		return this.airHumidityForRealIncrement;
	}

	/**
	 * 设置 增加 空气湿度(实时)，单位：百分比
	 * @param airHumidityForRealIncrement
	 */
	public void setAirHumidityForRealIncrement(BigDecimal airHumidityForRealIncrement){
		this.airHumidityForRealIncrement = airHumidityForRealIncrement;
	}

	/**
	 * 获取 空气湿度(实时)，单位：百分比
	 * @return airHumidityForRealList
	 */
	public List<BigDecimal> getAirHumidityForRealList(){
		return this.airHumidityForRealList;
	}

	/**
	 * 设置 空气湿度(实时)，单位：百分比
	 * @param airHumidityForRealList
	 */
	public void setAirHumidityForRealList(List<BigDecimal> airHumidityForRealList){
		this.airHumidityForRealList = airHumidityForRealList;
	}

	/**
	 * 获取 空气质量，优、良、差
	 * @return airQualityList
	 */
	public List<String> getAirQualityList(){
		return this.airQualityList;
	}

	/**
	 * 设置 空气质量，优、良、差
	 * @param airQualityList
	 */
	public void setAirQualityList(List<String> airQualityList){
		this.airQualityList = airQualityList;
	}

	/**
	 * 获取 开始 空气指数
	 * @return airQIStart
	 */
	public BigDecimal getAirQIStart(){
		return this.airQIStart;
	}

	/**
	 * 设置 开始 空气指数
	 * @param airQIStart
	 */
	public void setAirQIStart(BigDecimal airQIStart){
		this.airQIStart = airQIStart;
	}

	/**
	 * 获取 结束 空气指数
	 * @return airQIEnd
	 */
	public BigDecimal getAirQIEnd(){
		return this.airQIEnd;
	}

	/**
	 * 设置 结束 空气指数
	 * @param airQIEnd
	 */
	public void setAirQIEnd(BigDecimal airQIEnd){
		this.airQIEnd = airQIEnd;
	}

	/**
	 * 获取 增加 空气指数
	 * @return airQIIncrement
	 */
	public BigDecimal getAirQIIncrement(){
		return this.airQIIncrement;
	}

	/**
	 * 设置 增加 空气指数
	 * @param airQIIncrement
	 */
	public void setAirQIIncrement(BigDecimal airQIIncrement){
		this.airQIIncrement = airQIIncrement;
	}

	/**
	 * 获取 空气指数
	 * @return airQIList
	 */
	public List<BigDecimal> getAirQIList(){
		return this.airQIList;
	}

	/**
	 * 设置 空气指数
	 * @param airQIList
	 */
	public void setAirQIList(List<BigDecimal> airQIList){
		this.airQIList = airQIList;
	}

	/**
	 * 获取 开始 最高温(白天)，白天温度，单位：摄氏度
	 * @return airMaxTemperatureStart
	 */
	public BigDecimal getAirMaxTemperatureStart(){
		return this.airMaxTemperatureStart;
	}

	/**
	 * 设置 开始 最高温(白天)，白天温度，单位：摄氏度
	 * @param airMaxTemperatureStart
	 */
	public void setAirMaxTemperatureStart(BigDecimal airMaxTemperatureStart){
		this.airMaxTemperatureStart = airMaxTemperatureStart;
	}

	/**
	 * 获取 结束 最高温(白天)，白天温度，单位：摄氏度
	 * @return airMaxTemperatureEnd
	 */
	public BigDecimal getAirMaxTemperatureEnd(){
		return this.airMaxTemperatureEnd;
	}

	/**
	 * 设置 结束 最高温(白天)，白天温度，单位：摄氏度
	 * @param airMaxTemperatureEnd
	 */
	public void setAirMaxTemperatureEnd(BigDecimal airMaxTemperatureEnd){
		this.airMaxTemperatureEnd = airMaxTemperatureEnd;
	}

	/**
	 * 获取 增加 最高温(白天)，白天温度，单位：摄氏度
	 * @return airMaxTemperatureIncrement
	 */
	public BigDecimal getAirMaxTemperatureIncrement(){
		return this.airMaxTemperatureIncrement;
	}

	/**
	 * 设置 增加 最高温(白天)，白天温度，单位：摄氏度
	 * @param airMaxTemperatureIncrement
	 */
	public void setAirMaxTemperatureIncrement(BigDecimal airMaxTemperatureIncrement){
		this.airMaxTemperatureIncrement = airMaxTemperatureIncrement;
	}

	/**
	 * 获取 最高温(白天)，白天温度，单位：摄氏度
	 * @return airMaxTemperatureList
	 */
	public List<BigDecimal> getAirMaxTemperatureList(){
		return this.airMaxTemperatureList;
	}

	/**
	 * 设置 最高温(白天)，白天温度，单位：摄氏度
	 * @param airMaxTemperatureList
	 */
	public void setAirMaxTemperatureList(List<BigDecimal> airMaxTemperatureList){
		this.airMaxTemperatureList = airMaxTemperatureList;
	}

	/**
	 * 获取 开始 最低温(夜间)，夜间温度，单位：摄氏度
	 * @return airMinTemperatureStart
	 */
	public BigDecimal getAirMinTemperatureStart(){
		return this.airMinTemperatureStart;
	}

	/**
	 * 设置 开始 最低温(夜间)，夜间温度，单位：摄氏度
	 * @param airMinTemperatureStart
	 */
	public void setAirMinTemperatureStart(BigDecimal airMinTemperatureStart){
		this.airMinTemperatureStart = airMinTemperatureStart;
	}

	/**
	 * 获取 结束 最低温(夜间)，夜间温度，单位：摄氏度
	 * @return airMinTemperatureEnd
	 */
	public BigDecimal getAirMinTemperatureEnd(){
		return this.airMinTemperatureEnd;
	}

	/**
	 * 设置 结束 最低温(夜间)，夜间温度，单位：摄氏度
	 * @param airMinTemperatureEnd
	 */
	public void setAirMinTemperatureEnd(BigDecimal airMinTemperatureEnd){
		this.airMinTemperatureEnd = airMinTemperatureEnd;
	}

	/**
	 * 获取 增加 最低温(夜间)，夜间温度，单位：摄氏度
	 * @return airMinTemperatureIncrement
	 */
	public BigDecimal getAirMinTemperatureIncrement(){
		return this.airMinTemperatureIncrement;
	}

	/**
	 * 设置 增加 最低温(夜间)，夜间温度，单位：摄氏度
	 * @param airMinTemperatureIncrement
	 */
	public void setAirMinTemperatureIncrement(BigDecimal airMinTemperatureIncrement){
		this.airMinTemperatureIncrement = airMinTemperatureIncrement;
	}

	/**
	 * 获取 最低温(夜间)，夜间温度，单位：摄氏度
	 * @return airMinTemperatureList
	 */
	public List<BigDecimal> getAirMinTemperatureList(){
		return this.airMinTemperatureList;
	}

	/**
	 * 设置 最低温(夜间)，夜间温度，单位：摄氏度
	 * @param airMinTemperatureList
	 */
	public void setAirMinTemperatureList(List<BigDecimal> airMinTemperatureList){
		this.airMinTemperatureList = airMinTemperatureList;
	}

	/**
	 * 获取 开始 PM2.5，单位：ug/m3
	 * @return airPM25ValueStart
	 */
	public BigDecimal getAirPM25ValueStart(){
		return this.airPM25ValueStart;
	}

	/**
	 * 设置 开始 PM2.5，单位：ug/m3
	 * @param airPM25ValueStart
	 */
	public void setAirPM25ValueStart(BigDecimal airPM25ValueStart){
		this.airPM25ValueStart = airPM25ValueStart;
	}

	/**
	 * 获取 结束 PM2.5，单位：ug/m3
	 * @return airPM25ValueEnd
	 */
	public BigDecimal getAirPM25ValueEnd(){
		return this.airPM25ValueEnd;
	}

	/**
	 * 设置 结束 PM2.5，单位：ug/m3
	 * @param airPM25ValueEnd
	 */
	public void setAirPM25ValueEnd(BigDecimal airPM25ValueEnd){
		this.airPM25ValueEnd = airPM25ValueEnd;
	}

	/**
	 * 获取 增加 PM2.5，单位：ug/m3
	 * @return airPM25ValueIncrement
	 */
	public BigDecimal getAirPM25ValueIncrement(){
		return this.airPM25ValueIncrement;
	}

	/**
	 * 设置 增加 PM2.5，单位：ug/m3
	 * @param airPM25ValueIncrement
	 */
	public void setAirPM25ValueIncrement(BigDecimal airPM25ValueIncrement){
		this.airPM25ValueIncrement = airPM25ValueIncrement;
	}

	/**
	 * 获取 PM2.5，单位：ug/m3
	 * @return airPM25ValueList
	 */
	public List<BigDecimal> getAirPM25ValueList(){
		return this.airPM25ValueList;
	}

	/**
	 * 设置 PM2.5，单位：ug/m3
	 * @param airPM25ValueList
	 */
	public void setAirPM25ValueList(List<BigDecimal> airPM25ValueList){
		this.airPM25ValueList = airPM25ValueList;
	}

	/**
	 * 获取 开始 PM10，单位：ug/m3
	 * @return airPM10ValueStart
	 */
	public BigDecimal getAirPM10ValueStart(){
		return this.airPM10ValueStart;
	}

	/**
	 * 设置 开始 PM10，单位：ug/m3
	 * @param airPM10ValueStart
	 */
	public void setAirPM10ValueStart(BigDecimal airPM10ValueStart){
		this.airPM10ValueStart = airPM10ValueStart;
	}

	/**
	 * 获取 结束 PM10，单位：ug/m3
	 * @return airPM10ValueEnd
	 */
	public BigDecimal getAirPM10ValueEnd(){
		return this.airPM10ValueEnd;
	}

	/**
	 * 设置 结束 PM10，单位：ug/m3
	 * @param airPM10ValueEnd
	 */
	public void setAirPM10ValueEnd(BigDecimal airPM10ValueEnd){
		this.airPM10ValueEnd = airPM10ValueEnd;
	}

	/**
	 * 获取 增加 PM10，单位：ug/m3
	 * @return airPM10ValueIncrement
	 */
	public BigDecimal getAirPM10ValueIncrement(){
		return this.airPM10ValueIncrement;
	}

	/**
	 * 设置 增加 PM10，单位：ug/m3
	 * @param airPM10ValueIncrement
	 */
	public void setAirPM10ValueIncrement(BigDecimal airPM10ValueIncrement){
		this.airPM10ValueIncrement = airPM10ValueIncrement;
	}

	/**
	 * 获取 PM10，单位：ug/m3
	 * @return airPM10ValueList
	 */
	public List<BigDecimal> getAirPM10ValueList(){
		return this.airPM10ValueList;
	}

	/**
	 * 设置 PM10，单位：ug/m3
	 * @param airPM10ValueList
	 */
	public void setAirPM10ValueList(List<BigDecimal> airPM10ValueList){
		this.airPM10ValueList = airPM10ValueList;
	}

	/**
	 * 获取 风向(白天)，东风、西风
	 * @return windDirectionList
	 */
	public List<String> getWindDirectionList(){
		return this.windDirectionList;
	}

	/**
	 * 设置 风向(白天)，东风、西风
	 * @param windDirectionList
	 */
	public void setWindDirectionList(List<String> windDirectionList){
		this.windDirectionList = windDirectionList;
	}

	/**
	 * 获取 风向(夜间)，东风、西风
	 * @return windDirectionForNightList
	 */
	public List<String> getWindDirectionForNightList(){
		return this.windDirectionForNightList;
	}

	/**
	 * 设置 风向(夜间)，东风、西风
	 * @param windDirectionForNightList
	 */
	public void setWindDirectionForNightList(List<String> windDirectionForNightList){
		this.windDirectionForNightList = windDirectionForNightList;
	}

	/**
	 * 获取 风向(实时)，东风、西风
	 * @return windDirectionForRealList
	 */
	public List<String> getWindDirectionForRealList(){
		return this.windDirectionForRealList;
	}

	/**
	 * 设置 风向(实时)，东风、西风
	 * @param windDirectionForRealList
	 */
	public void setWindDirectionForRealList(List<String> windDirectionForRealList){
		this.windDirectionForRealList = windDirectionForRealList;
	}

	/**
	 * 获取 风力(白天)
	 * @return windPowerList
	 */
	public List<String> getWindPowerList(){
		return this.windPowerList;
	}

	/**
	 * 设置 风力(白天)
	 * @param windPowerList
	 */
	public void setWindPowerList(List<String> windPowerList){
		this.windPowerList = windPowerList;
	}

	/**
	 * 获取 风力(夜间)
	 * @return windPowerForNightList
	 */
	public List<String> getWindPowerForNightList(){
		return this.windPowerForNightList;
	}

	/**
	 * 设置 风力(夜间)
	 * @param windPowerForNightList
	 */
	public void setWindPowerForNightList(List<String> windPowerForNightList){
		this.windPowerForNightList = windPowerForNightList;
	}

	/**
	 * 获取 风力(实时)
	 * @return windPowerForRealList
	 */
	public List<String> getWindPowerForRealList(){
		return this.windPowerForRealList;
	}

	/**
	 * 设置 风力(实时)
	 * @param windPowerForRealList
	 */
	public void setWindPowerForRealList(List<String> windPowerForRealList){
		this.windPowerForRealList = windPowerForRealList;
	}

	/**
	 * 获取 开始 大气压力，单位：hPa
	 * @return barometricStart
	 */
	public BigDecimal getBarometricStart(){
		return this.barometricStart;
	}

	/**
	 * 设置 开始 大气压力，单位：hPa
	 * @param barometricStart
	 */
	public void setBarometricStart(BigDecimal barometricStart){
		this.barometricStart = barometricStart;
	}

	/**
	 * 获取 结束 大气压力，单位：hPa
	 * @return barometricEnd
	 */
	public BigDecimal getBarometricEnd(){
		return this.barometricEnd;
	}

	/**
	 * 设置 结束 大气压力，单位：hPa
	 * @param barometricEnd
	 */
	public void setBarometricEnd(BigDecimal barometricEnd){
		this.barometricEnd = barometricEnd;
	}

	/**
	 * 获取 增加 大气压力，单位：hPa
	 * @return barometricIncrement
	 */
	public BigDecimal getBarometricIncrement(){
		return this.barometricIncrement;
	}

	/**
	 * 设置 增加 大气压力，单位：hPa
	 * @param barometricIncrement
	 */
	public void setBarometricIncrement(BigDecimal barometricIncrement){
		this.barometricIncrement = barometricIncrement;
	}

	/**
	 * 获取 大气压力，单位：hPa
	 * @return barometricList
	 */
	public List<BigDecimal> getBarometricList(){
		return this.barometricList;
	}

	/**
	 * 设置 大气压力，单位：hPa
	 * @param barometricList
	 */
	public void setBarometricList(List<BigDecimal> barometricList){
		this.barometricList = barometricList;
	}

	/**
	 * 获取 开始 紫外线，单位：W/㎡
	 * @return ultravioletRaysStart
	 */
	public BigDecimal getUltravioletRaysStart(){
		return this.ultravioletRaysStart;
	}

	/**
	 * 设置 开始 紫外线，单位：W/㎡
	 * @param ultravioletRaysStart
	 */
	public void setUltravioletRaysStart(BigDecimal ultravioletRaysStart){
		this.ultravioletRaysStart = ultravioletRaysStart;
	}

	/**
	 * 获取 结束 紫外线，单位：W/㎡
	 * @return ultravioletRaysEnd
	 */
	public BigDecimal getUltravioletRaysEnd(){
		return this.ultravioletRaysEnd;
	}

	/**
	 * 设置 结束 紫外线，单位：W/㎡
	 * @param ultravioletRaysEnd
	 */
	public void setUltravioletRaysEnd(BigDecimal ultravioletRaysEnd){
		this.ultravioletRaysEnd = ultravioletRaysEnd;
	}

	/**
	 * 获取 增加 紫外线，单位：W/㎡
	 * @return ultravioletRaysIncrement
	 */
	public BigDecimal getUltravioletRaysIncrement(){
		return this.ultravioletRaysIncrement;
	}

	/**
	 * 设置 增加 紫外线，单位：W/㎡
	 * @param ultravioletRaysIncrement
	 */
	public void setUltravioletRaysIncrement(BigDecimal ultravioletRaysIncrement){
		this.ultravioletRaysIncrement = ultravioletRaysIncrement;
	}

	/**
	 * 获取 紫外线，单位：W/㎡
	 * @return ultravioletRaysList
	 */
	public List<BigDecimal> getUltravioletRaysList(){
		return this.ultravioletRaysList;
	}

	/**
	 * 设置 紫外线，单位：W/㎡
	 * @param ultravioletRaysList
	 */
	public void setUltravioletRaysList(List<BigDecimal> ultravioletRaysList){
		this.ultravioletRaysList = ultravioletRaysList;
	}

	/**
	 * 获取 开始 紫外线指数，单位：W/㎡
	 * @return ultraVioletIndexStart
	 */
	public BigDecimal getUltraVioletIndexStart(){
		return this.ultraVioletIndexStart;
	}

	/**
	 * 设置 开始 紫外线指数，单位：W/㎡
	 * @param ultraVioletIndexStart
	 */
	public void setUltraVioletIndexStart(BigDecimal ultraVioletIndexStart){
		this.ultraVioletIndexStart = ultraVioletIndexStart;
	}

	/**
	 * 获取 结束 紫外线指数，单位：W/㎡
	 * @return ultraVioletIndexEnd
	 */
	public BigDecimal getUltraVioletIndexEnd(){
		return this.ultraVioletIndexEnd;
	}

	/**
	 * 设置 结束 紫外线指数，单位：W/㎡
	 * @param ultraVioletIndexEnd
	 */
	public void setUltraVioletIndexEnd(BigDecimal ultraVioletIndexEnd){
		this.ultraVioletIndexEnd = ultraVioletIndexEnd;
	}

	/**
	 * 获取 增加 紫外线指数，单位：W/㎡
	 * @return ultraVioletIndexIncrement
	 */
	public BigDecimal getUltraVioletIndexIncrement(){
		return this.ultraVioletIndexIncrement;
	}

	/**
	 * 设置 增加 紫外线指数，单位：W/㎡
	 * @param ultraVioletIndexIncrement
	 */
	public void setUltraVioletIndexIncrement(BigDecimal ultraVioletIndexIncrement){
		this.ultraVioletIndexIncrement = ultraVioletIndexIncrement;
	}

	/**
	 * 获取 紫外线指数，单位：W/㎡
	 * @return ultraVioletIndexList
	 */
	public List<BigDecimal> getUltraVioletIndexList(){
		return this.ultraVioletIndexList;
	}

	/**
	 * 设置 紫外线指数，单位：W/㎡
	 * @param ultraVioletIndexList
	 */
	public void setUltraVioletIndexList(List<BigDecimal> ultraVioletIndexList){
		this.ultraVioletIndexList = ultraVioletIndexList;
	}

	/**
	 * 获取 天气描述
	 * @return remarkList
	 */
	public List<String> getRemarkList(){
		return this.remarkList;
	}

	/**
	 * 设置 天气描述
	 * @param remarkList
	 */
	public void setRemarkList(List<String> remarkList){
		this.remarkList = remarkList;
	}

	/**
	 * 获取 天气扩展
	 * @return extList
	 */
	public List<String> getExtList(){
		return this.extList;
	}

	/**
	 * 设置 天气扩展
	 * @param extList
	 */
	public void setExtList(List<String> extList){
		this.extList = extList;
	}

	/**
	 * 获取 开始 最后同步时间
	 * @return lastSyncTimeStart
	 */
	public String getLastSyncTimeStart(){
		return this.lastSyncTimeStart;
	}

	/**
	 * 设置 开始 最后同步时间
	 * @param lastSyncTimeStart
	 */
	public void setLastSyncTimeStart(String lastSyncTimeStart){
		this.lastSyncTimeStart = lastSyncTimeStart;
	}

	/**
	 * 获取 结束 最后同步时间
	 * @return lastSyncTimeEnd
	 */
	public String getLastSyncTimeEnd(){
		return this.lastSyncTimeEnd;
	}

	/**
	 * 设置 结束 最后同步时间
	 * @param lastSyncTimeEnd
	 */
	public void setLastSyncTimeEnd(String lastSyncTimeEnd){
		this.lastSyncTimeEnd = lastSyncTimeEnd;
	}

	/**
	 * 获取 OR条件集合，列表项之间是OR，项内容之间是AND，如：(list[0].1 and list[0].2) or (list[1].3 and list[1].4)
	 * @return orConditionList
	 */
	public List<WeatherQuery> getOrConditionList(){
		return this.orConditionList;
	}

	/**
	 * 设置 OR条件集合，列表项之间是OR，项内容之间是AND，如：(list[0].1 and list[0].2) or (list[1].3 and list[1].4)
	 * @param orConditionList
	 */
	public void setOrConditionList(List<WeatherQuery> orConditionList){
		this.orConditionList = orConditionList;
	}

	/**
	 * 获取 AND条件集合，列表项之间是AND，项内容之间是OR，如：(list[0].1 or list[0].2) and (list[1].3 or list[1].4)
	 * @return andConditionList
	 */
	public List<WeatherQuery> getAndConditionList(){
		return this.andConditionList;
	}

	/**
	 * 设置 AND条件集合，列表项之间是AND，项内容之间是OR，如：(list[0].1 or list[0].2) and (list[1].3 or list[1].4)
	 * @param andConditionList
	 */
	public void setAndConditionList(List<WeatherQuery> andConditionList){
		this.andConditionList = andConditionList;
	}


}