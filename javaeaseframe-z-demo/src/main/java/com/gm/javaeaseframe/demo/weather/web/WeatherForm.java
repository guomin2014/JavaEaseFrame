/**
* 文件：WeatherForm.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/
package com.gm.javaeaseframe.demo.weather.web;

import com.gm.javaeaseframe.core.context.web.BaseCRUDFormLong;
import com.gm.javaeaseframe.demo.weather.model.WeatherEntity;
import com.gm.javaeaseframe.demo.weather.model.WeatherQuery;

/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherForm  </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */
public class WeatherForm extends BaseCRUDFormLong<WeatherEntity> {
	private WeatherEntity weather = new WeatherEntity();
	private WeatherQuery query = new WeatherQuery(); 
	public WeatherForm(){
		
	}
	
    @Override
	public WeatherEntity getEntity() {
		return weather;
	}
	public WeatherEntity getWeather() {
		return weather;
	}

	public void setWeather(WeatherEntity weather) {
		this.weather = weather;
	}
	
	@Override
	public WeatherQuery getQuery() {
		return query;
	}

	public void setQuery(WeatherQuery query) {
		this.query = query;
	}
}