package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties;

import javax.annotation.PostConstruct;

import org.springframework.aop.support.DefaultPointcutAdvisor;

public class CustomAdvisor extends DefaultPointcutAdvisor{

	@PostConstruct
	public void init() {
		System.out.println("***CustomAdvisor init****");
		System.out.println(this.getPointcut());
		System.out.println(this.getAdvice());
	}
}
