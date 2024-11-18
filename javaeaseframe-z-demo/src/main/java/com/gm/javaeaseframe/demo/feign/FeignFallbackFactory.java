package com.gm.javaeaseframe.demo.feign;

import org.springframework.cloud.openfeign.FallbackFactory;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.core.context.web.dto.BasePageDto;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherRequestPageDto;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherResponseDto;

//@Component
public class FeignFallbackFactory implements FallbackFactory<AppNameFeign> {

	@Override
	public AppNameFeign create(Throwable cause) {
		return new AppNameFeign() {

			@Override
			public CommonResult<BasePageDto<WeatherResponseDto>> list(WeatherRequestPageDto data) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CommonResult<BasePageDto<WeatherResponseDto>> exceptionTest(WeatherRequestPageDto data) {
				if (cause instanceof BusinessException) {
					return new CommonResult<>(((BusinessException) cause).getCode(), cause.getMessage());
				} else {
					return new CommonResult<>(ExceptionCodeEnum.SYSTEM_BUSY.getCode(), ExceptionCodeEnum.SYSTEM_BUSY.getDesc());
				}
			}
			
		};
	}

}
