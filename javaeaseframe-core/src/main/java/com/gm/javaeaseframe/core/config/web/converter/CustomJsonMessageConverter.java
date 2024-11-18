package com.gm.javaeaseframe.core.config.web.converter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json消息转换，支持Form表单形式提交的Json数据，比如：pageInfo.currPage=5
 * @author	GM
 * @date	2023年9月18日
 */
public class CustomJsonMessageConverter extends AbstractJackson2HttpMessageConverter{
    
    @Nullable
    private String jsonPrefix;
    
    public CustomJsonMessageConverter() {
        this(CustomObjectMapper.build());
    }

    public CustomJsonMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, new MediaType("application", "json+form"));//以Json格式提交的Form表单，如：{pageInfo.currPage:5}
    }
    
    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = (prefixJson ? ")]}', " : null);
    }


    @Override
    @SuppressWarnings("deprecation")
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
//        String jsonpFunction =
//                (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
//        if (jsonpFunction != null) {
//            generator.writeRaw("/**/");
//            generator.writeRaw(jsonpFunction + "(");
//        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
//        String jsonpFunction =
//                (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
//        if (jsonpFunction != null) {
//            generator.writeRaw(");");
//        }
    }
    
}
