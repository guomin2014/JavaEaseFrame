package com.gm.javaeaseframe.demo.weather.web.dto;

import com.gm.javaeaseframe.common.annotation.CustomApiModel;
import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;
import com.gm.javaeaseframe.core.context.web.dto.BaseRequestPageDto;

@CustomApiModel("天气信息(分页)")
public class WeatherRequestPageDto extends BaseRequestPageDto {

	/** ID */
	@CustomApiModelProperty("ID")
	private Long id;
	/** 区域ID */
	@CustomApiModelProperty("区域ID")
	private Long areaId;
	/** 城市名称 */
	@CustomApiModelProperty("城市名称")
	private String city;
	/** 城市编号，用于同步天气信息 */
	@CustomApiModelProperty("城市编号")
	private String cityCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
}
