package com.gm.javaeaseframe.core.config.web.converter;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 转换成Json对象
 * @author	GM
 * @date	2020年11月4日
 */
public class CustomJsonObjectDeserializingConverter implements CustomDeserializingConverter<JSONObject>{

    @Override
    public JSONObject convert(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONObject) {
            return (JSONObject)obj;
        } else if (obj instanceof Map) {
            return new JSONObject((Map)obj);
        } else if (obj instanceof String) {
            return JSONObject.parseObject(obj.toString());
        }
        return null;
    }

}
