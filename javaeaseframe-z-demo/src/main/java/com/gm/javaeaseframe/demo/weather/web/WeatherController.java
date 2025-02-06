/**
* 文件：WeatherController.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/
package com.gm.javaeaseframe.demo.weather.web;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gm.javaeaseframe.common.annotation.CustomApi;
import com.gm.javaeaseframe.common.annotation.CustomApiOperation;
import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.util.DataUtil;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.OrderCol;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.core.context.web.BaseCRUDJsonMappingController;
import com.gm.javaeaseframe.core.context.web.dto.BasePageDto;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.demo.common.code.WeatherTypeEnum;
import com.gm.javaeaseframe.demo.feign.AppNameFeign;
import com.gm.javaeaseframe.demo.weather.model.WeatherEntity;
import com.gm.javaeaseframe.demo.weather.service.WeatherService;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherRequestPageDto;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherResponseDto;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.annotation.GlobalException;

/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherController  </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */
@RestController
@RequestMapping("weather")
@CustomApi(tags = "天气模块")
@GlobalException
public class WeatherController extends BaseCRUDJsonMappingController<WeatherService,WeatherForm,WeatherEntity,Long>{

	@Autowired
	private AppNameFeign appNameFeign;
	
	public WeatherController(){
		super.setFormClass(WeatherForm.class);
		super.setModuleDesc("城市天气信息");
	}
	@PostMapping("feign/call")
	@CustomApiOperation(value = "Feign调用测试")
	public CommonResult<BasePageDto<WeatherResponseDto>> feignCallTest() {
		WeatherRequestPageDto data = new WeatherRequestPageDto();
		data.setCityCode("5101");
		data.setPageSize(10);
		CommonResult<BasePageDto<WeatherResponseDto>> result = appNameFeign.list(data);
		return result;
	}
	@PostMapping("feign/call/exception")
	@CustomApiOperation(value = "Feign调用测试(异常测试)")
	public CommonResult<BasePageDto<WeatherResponseDto>> feignCallTest2() {
		WeatherRequestPageDto data = new WeatherRequestPageDto();
		data.setCityCode("5101");
		data.setPageSize(10);
		CommonResult<BasePageDto<WeatherResponseDto>> result = appNameFeign.exceptionTest(data);
		return result;
	}
	
	@Override
    protected void init(HttpServletRequest request, HttpServletResponse response, WeatherForm form, Map<String, Object> model, Context context) {
        super.addDict(model, "weatherType", WeatherTypeEnum.getEnumMap());
    }
	

    @Override
    protected void doListBefore(HttpServletRequest request, HttpServletResponse response, WeatherForm form, Map<String, Object> model, Context context) throws BusinessException {
        List<OrderCol> orderColList = new ArrayList<>();
        orderColList.add(new OrderCol("date", OrderCol.DESCENDING));
        form.getQuery().setOrderColList(orderColList);
    }

    /**
	 * 获取天气预报
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@PostMapping("forecasts")
	@CustomApiOperation(value = "获取天气预报")
    public String doForecasts(HttpServletRequest request, HttpServletResponse response, WeatherForm form) {
	    JSONObject ret = new JSONObject();
	    Map<String, Object> model = new HashMap<>();
	    Context context = getContext();
        String busiDesc = "查询" + getModuleDesc();
        int code = VALUE_RESULT_SUCCESS;
	    try {
	        super.recordSysLog(request, busiDesc);
    	    IUser user = context.getUser();
    	    List<WeatherEntity> weatherList = this.getService().findForecastsByArea(1L);
    	    JSONArray weatherArr = new JSONArray();
    	    Calendar currCal = Calendar.getInstance();
    	    int currDay = currCal.get(Calendar.DAY_OF_YEAR);
    	    String [] workChinese = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    	    String city = "";
    	    for (WeatherEntity entity : weatherList) {
    	        if (StringUtils.isEmpty(city)) {
    	            city = entity.getCity();
    	        }
    	        JSONObject weather = new JSONObject();
    	        Calendar weatherCal = Calendar.getInstance();
    	        weatherCal.setTime(DateUtils.parseDate(entity.getDate().toString(), "yyyyMMdd"));
    	        int weatherDay = weatherCal.get(Calendar.DAY_OF_YEAR);
    	        String work = "";
    	        int diffDay = weatherDay - currDay;
    	        switch(diffDay) {
    	            case 0:
    	                work = "今天";
    	                break;
    	            case 1:
    	                work = "明天";
    	                break;
    	            case 2:
    	                work = "后天";
    	                break;
	                default:
	                    work = workChinese[weatherCal.get(Calendar.DAY_OF_WEEK) - 1];
	                    break;
    	        }
    	        weather.put("date", DateFormatUtils.format(weatherCal.getTime(), "MM月dd日"));
    	        weather.put("work", work);
    	        weather.put("weatherType", entity.getWeatherType());
    	        weather.put("weather", entity.getWeather());
    	        weather.put("minTemperature", DataUtil.conver2Int(entity.getAirMinTemperature()));
    	        weather.put("maxTemperature", DataUtil.conver2Int(entity.getAirMaxTemperature()));
//    	        weather.put("airQuality", "优");
    	        weather.put("windDirection", entity.getWindDirection());
    	        weather.put("windPower", entity.getWindPower());
    	        weatherArr.add(weather);
    	    }
    	    model.put("city", city);
    	    model.put("weatherList", weatherArr);
    	    ret.put(KEY_RESULT_CODE, code);
            ret.put(KEY_RESULT_DATA, model);
	    } catch (Exception e) {
	        log.error("查询天气预报异常", e);
	        ret.put(KEY_RESULT_CODE, code);
	        ret.put(KEY_RESULT_MSG, super.convertException(e));
	    }
	    return ret.toJSONString();
	}
}