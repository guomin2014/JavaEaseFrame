package com.gm.javaeaseframe.core.config.web.resolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标签：不支持通过Form或Body两种方式解析参数
 * @author	GM
 * @date	2023年9月18日
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormAndJsonArgumentResolverNotSupport {

}
