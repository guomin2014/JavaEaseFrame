package com.gm.javaeaseframe.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gm.javaeaseframe.core.context.web.dto.BasePageDto;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherRequestPageDto;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherResponseDto;
import com.gm.javaeaseframe.openfeign.spring.boot.autoconfigure.service.IFeign;

@FeignClient(name="appname-service", fallbackFactory=FeignFallbackFactory.class)
public interface AppNameFeign extends IFeign {

	@RequestMapping(value = "/v2/weather/list", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResult<BasePageDto<WeatherResponseDto>> list(WeatherRequestPageDto data);
	@RequestMapping(value = "/v2/weather/exception/test", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
	public CommonResult<BasePageDto<WeatherResponseDto>> exceptionTest(WeatherRequestPageDto data);
}
