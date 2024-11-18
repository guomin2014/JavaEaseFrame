package com.gm.javaeaseframe.demo.weather.service.impl;

import org.springframework.stereotype.Service;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.demo.weather.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

	@Override
	public void save() {
		System.out.println("test1");
		throw new BusinessException("error");
	}

	@Override
	public void search() {
		System.out.println("test2");
	}

}
