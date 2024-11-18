package com.gm.javaeaseframe.core.config.web.converter;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -7356118143040019932L;
    
    public CustomObjectMapper() {
//        System.out.println("**********使用自定义ObjectMapper*************");
//        SimpleModule module = new SimpleModule();
////        // 添加一个自定义Deserializer
////        module.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
////            @Override
////            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
////                System.out.println("*************CustomObjectMapper--string--->" + p);
////                return p.getValueAsString() == null ? null : p.getValueAsString().trim(); // 去掉头尾空格
////            }
////        });
////        module.addDeserializer(BaseForm.class, new JsonDeserializer<BaseForm>() {
////            @Override
////            public BaseForm deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
////                System.out.println("*************BaseForm--->" + p);
////                return null;
////            }});
//        module.setDeserializerModifier(new MyBeanDeserializerModifier());
//        // 注册
//        this.registerModule(module);
    }
    
    public static ObjectMapper build() {
//        ExampleModule module = new ExampleModule();
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {

            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (deserializer instanceof BeanDeserializer) {//替换默认的BeanDeserializer
                    return new CustomBeanDeserializer((BeanDeserializer) deserializer);
                }
                return deserializer;
            }
            
        });
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().modules(module).featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).build();
        return mapper;
    }

}
