package com.gm.javaeaseframe.demo.weather.web.dto;

import com.gm.javaeaseframe.core.context.web.dto.BaseResponseDto;

public class WeatherResponseDto extends BaseResponseDto {
	/** ID */
	private Long id;
	/** 区域ID */
	private Long areaId;

	/** 城市名称 */
	private String city;

	/** 城市编号，用于同步天气信息 */
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
