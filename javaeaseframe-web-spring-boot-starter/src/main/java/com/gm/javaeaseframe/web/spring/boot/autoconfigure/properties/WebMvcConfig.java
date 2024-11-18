package com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.gm.javaeaseframe.core.config.web.BaseWebMvcConfigurer;
import com.gm.javaeaseframe.core.config.web.resolver.ArgumentResolverModeEnum;

/**
 * 相当于 spring-web.xml
 * @author	GM
 * @date	2023年9月19日
 */
//@Configuration
@EnableWebMvc
//配置扫描包的策略，当前注解为只扫描加@Controller的组件
//注意useDefaultFilters要设置为fasle，不然为默认的Filters，@Service/@Repository都扫描
@ComponentScan(basePackages= {"com.gm.javaeaseframe"},includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class) },useDefaultFilters = false)
public class WebMvcConfig extends BaseWebMvcConfigurer
{
	private WebProperties webProperties;
	public WebMvcConfig(WebProperties webProperties) {
        super(false, ArgumentResolverModeEnum.WHITE);
        this.webProperties = webProperties;
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Map<String, String> map = webProperties.getResourceHandlers();
        if (map != null) {
        	for (Map.Entry<String, String> entry : map.entrySet()) {
        		registry.addResourceHandler(entry.getKey()).addResourceLocations(entry.getValue());
        	}
        }
    }
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		MessageConverterProperties messageConverterProperties = webProperties.getMessageConverter();
		if (messageConverterProperties == null) {
			messageConverterProperties = new MessageConverterProperties();
		}
		super.extendMessageConverters(converters);
		boolean hasDefault = false;
		// 解决中文乱码，响应内容为JSON串时，中文内容乱码的缺陷（返回默认使用了ISO-8859-1字符集）
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
				hasDefault = true;
			}
		}
		if (!hasDefault) {
			// 字符串转换器
			List<MediaType> listString = new ArrayList<MediaType>();
			// 字符串的消息类型为text/plain
			listString.add(MediaType.TEXT_PLAIN);
			StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
			stringHttpMessageConverter.setSupportedMediaTypes(listString);
			// converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
			converters.add(stringHttpMessageConverter);
		}
		MessageConverterHandler handler = messageConverterProperties.getFastJson();
		if (handler != null && handler.isEnable()) {
			FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
			List<String> supportedMediaTypes = handler.getSupportedMediaTypes();
			List<MediaType> list = new ArrayList<MediaType>();
			if (supportedMediaTypes == null || supportedMediaTypes.isEmpty()) {
				list.add(MediaType.valueOf("application/json;charset=UTF-8"));
			} else {
				for (String type : supportedMediaTypes) {
					try {
						list.add(MediaType.valueOf(type));
					} catch (Exception e) {
						log.warn("unsupport media type[" + type + "]");
					}
				}
			}
			fastJsonHttpMessageConverter.setSupportedMediaTypes(list);
			SerializeConfig serializeConfig = SerializeConfig.globalInstance;
			serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
			serializeConfig.put(BigDecimal.class, ToStringSerializer.instance);
			serializeConfig.put(Long.class, ToStringSerializer.instance);
			serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
			fastJsonHttpMessageConverter.getFastJsonConfig().setSerializeConfig(serializeConfig);
			if (handler.isSortEnable()) {
				// 确保本JSON转换器在所有支持json转换器的最前面
				int index = 0;
				for (index = 0; index < converters.size(); index++) {
					HttpMessageConverter<?> converter = converters.get(index);
					if (converter.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON)) {
						break;
					}
				}
				if (index >= converters.size()) {
					index = converters.size() - 1;
				}
				if (index < 0) {
					index = 0;
				}
				converters.add(index, fastJsonHttpMessageConverter);
			} else {
				converters.add(fastJsonHttpMessageConverter);
			}
		}

	}
}