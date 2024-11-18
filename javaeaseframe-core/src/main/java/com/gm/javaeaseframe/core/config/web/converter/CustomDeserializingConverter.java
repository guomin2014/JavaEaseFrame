package com.gm.javaeaseframe.core.config.web.converter;

/**
 * 属性转换器
 * @author	GM
 * @date	2020年11月4日
 * @param <T>
 */
public interface CustomDeserializingConverter<T> {
    T convert(Object obj);
}
