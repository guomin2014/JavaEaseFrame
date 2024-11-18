package com.gm.javaeaseframe.core.config.web.converter;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 转换成Json字符串
 * @author	GM
 * @date	2020年11月4日
 */
public class CustomJsonStringDeserializingConverter implements CustomDeserializingConverter<String> {

    @Override
    public String convert(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSON) {
            return ((JSON)obj).toJSONString();
        } else if (obj instanceof Map) {
            return new JSONObject((Map)obj).toJSONString();
        } else if (obj instanceof String) {
            return obj.toString();
        }
        return null;
    }

}
