package com.gm.javaeaseframe.openfeign.spring.boot.autoconfigure;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.core.common.ExceptionCodeUtil;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.openfeign.spring.boot.autoconfigure.advice.FeignGlobalExceptionAdvice;
import com.gm.javaeaseframe.openfeign.spring.boot.autoconfigure.interceptor.FeignRequestInterceptor;

import feign.RequestInterceptor;
import feign.Response;
import feign.Util;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignAutoConfiguration extends FeignClientsConfiguration {

	@Bean
	@ConditionalOnMissingBean(FeignRequestInterceptor.class)
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }
	@Bean
    public Encoder feignFormEncoder() {
		ObjectFactory<HttpMessageConverters> messageConverters = new ObjectFactory<HttpMessageConverters>() {
			@Override
			public HttpMessageConverters getObject() throws BeansException {
				FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		        List<MediaType> list = new ArrayList<MediaType>();
		        list.add(MediaType.valueOf("application/json;charset=UTF-8"));
		        fastJsonHttpMessageConverter.setSupportedMediaTypes(list);
		        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
		        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
		        serializeConfig.put(Long.class,ToStringSerializer.instance);
		        serializeConfig.put(Long.TYPE,ToStringSerializer.instance);
		        fastJsonHttpMessageConverter.getFastJsonConfig().setSerializeConfig(serializeConfig);
				return new HttpMessageConverters(fastJsonHttpMessageConverter);
			}};
        return new SpringEncoder(messageConverters);
    }
	@Bean
    public ErrorDecoder errorDecoder() {
        return new RawErrorDecoder();
    }
	/**
	 * 将业务异常传递到Feign调用端
	 * @author Administrator
	 *
	 */
    public class RawErrorDecoder implements ErrorDecoder {
		@Override
        public Exception decode(String methodKey, Response response) {
            String message = null;
			try {
				if (response.status() == 404) {
					return new BusinessException(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.STATUS_REQUEST_METHOD_UNSUPPORTED.getCode()),
							"请求方法不存在");
				} else {
					if (response.body() != null) {

						// 获取原始的返回内容
						message = Util.toString(response.body().asReader(Util.UTF_8));
						// 将返回内容反序列化为Result，这里应根据自身项目作修改
						CommonResult<?> result = JSONObject.parseObject(message, CommonResult.class);
						return new BusinessException(ExceptionCodeUtil.encodeCode(result.getCode()), result.getMsg());
					}
				}
			} catch (Exception ignored) {
			}
            return new BusinessException(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.SYSTEM_ERROR.getCode()), message);
        }
    }
    
    @Bean
    public FeignGlobalExceptionAdvice feignGlobalExceptionAdvice() {
    	return new FeignGlobalExceptionAdvice();
    }
}