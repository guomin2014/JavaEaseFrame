package com.gm.javaeaseframe.demo.weather.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gm.javaeaseframe.common.annotation.CustomApi;
import com.gm.javaeaseframe.common.annotation.CustomApiOperation;
import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.util.ExcelExportData;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.web.BaseCRUDDtoMappingController;
import com.gm.javaeaseframe.core.context.web.dto.BasePageDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseResponseDto;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.demo.weather.model.WeatherEntity;
import com.gm.javaeaseframe.demo.weather.service.WeatherService;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherRequestDto;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherRequestPageDto;
import com.gm.javaeaseframe.demo.weather.web.dto.WeatherResponseDto;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.annotation.GlobalException;

@RestController
@RequestMapping("/v2/weather")
@CustomApi(tags="天气模块DTO")
@GlobalException
public class WeatherDtoController extends BaseCRUDDtoMappingController<WeatherService,WeatherRequestDto,WeatherRequestPageDto,WeatherResponseDto,WeatherEntity,Long> {

	
	@PostMapping(value="list/test", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@CustomApiOperation("test_列表测试")
	public CommonResult<BasePageDto<BaseResponseDto>> listTest(HttpServletRequest request, HttpServletResponse response, WeatherRequestPageDto data) {
		throw new BusinessException( 500, "数据不存在");
	}
	@PostMapping("exception/test")
	@CustomApiOperation("test_异常测试")
	public CommonResult<BasePageDto<BaseResponseDto>> exceptionTest() {
		throw new BusinessException( 500, "数据不存在");
	}
	
	@PostMapping("test/readonly/transaction")
	public void testReadOnlyTransaction() {
		System.out.println("****testReadOnlyTransaction*****start**");
		this.service.search();
		System.out.println("****testReadOnlyTransaction*****end**");
	}
	@PostMapping("test/transaction")
	public void testTransaction() {
		System.out.println("****testTransaction*****start**");
		this.service.save();
		System.out.println("****testTransaction*****end**");
	}
	@PostMapping("test/supports/transaction")
	public void testSupportsTransaction() {
		System.out.println("****testSupportsTransaction*****start**");
		this.service.search2();
		System.out.println("****testSupportsTransaction*****end**");
	}
	@PostMapping("test/notsupported/transaction")
	public void testNotSupportedTransaction() {
		System.out.println("****testNotSupportedTransaction*****start**");
		this.service.searchNonTx();
		System.out.println("****testNotSupportedTransaction*****end**");
	}
	
	@Override
	public ExcelExportData doExportConvert(Context context, WeatherRequestDto form, WeatherEntity entity,
			List<WeatherEntity> list) throws BusinessException {
		ExcelExportData data = new ExcelExportData();
		if (list == null) {
			data.setColumnName(new String[] {"列1", "列2"});
		} else {
			List<String[]> dataList = new ArrayList<>();
			for (WeatherEntity we : list) {
				dataList.add(new String[] {we.getCityCode(), we.getCity()});
			}
			data.setDataList(dataList);
		}
		return data;
	}
	@PostMapping("test/export")
	public void export(HttpServletRequest request, HttpServletResponse response, WeatherRequestDto form) {
		super.doExport(request, response, form);
	}

}
