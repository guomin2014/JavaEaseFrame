package com.gm.javaeaseframe.core.config.web.resolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gm.javaeaseframe.core.config.web.converter.CustomDeserializingConverter;

/**
 * 标签：将请求体存储到指定字段
 * @author	GM
 * @date	2023年9月18日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBodyArgument {
    Class<? extends CustomDeserializingConverter<?>> converter();
}
