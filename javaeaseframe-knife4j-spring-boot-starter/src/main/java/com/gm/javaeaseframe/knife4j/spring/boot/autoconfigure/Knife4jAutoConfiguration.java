package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.classmate.TypeResolver;
import com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug.CustomApiListingBuilderPlugin;
import com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug.CustomModelBuilderPlugin;
import com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug.CustomModelPropertyPlugin;
import com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug.CustomModelTypeNameProvider;
import com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug.CustomOperationBuilderPlugin;
import com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug.CustomParameterBuilderPlugin;

import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.DescriptionResolver;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
public class Knife4jAutoConfiguration {

	@ComponentScan(basePackages = {
            "com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug"
    })
    public class Knife4jEnhanceAutoConfiguration {
		
		@Bean
		@ConditionalOnMissingBean
		public CustomApiListingBuilderPlugin customApiListingBuilderPlugin() {
			return new CustomApiListingBuilderPlugin();
		}
		@Bean
		@ConditionalOnMissingBean
		public CustomModelBuilderPlugin customModelBuilderPlugin(TypeResolver typeResolver) {
			return new CustomModelBuilderPlugin(typeResolver);
		}
		@Bean
		@ConditionalOnMissingBean
		public CustomModelPropertyPlugin customModelPropertyPlugin(DescriptionResolver descriptions) {
			return new CustomModelPropertyPlugin(descriptions);
		}
		@Bean
		@ConditionalOnMissingBean
		@Primary
		public CustomModelTypeNameProvider customModelTypeNameProvider() {
			return new CustomModelTypeNameProvider();
		}
		@Bean
		@ConditionalOnMissingBean
		public CustomOperationBuilderPlugin customOperationBuilderPlugin(DescriptionResolver descriptions) {
			return new CustomOperationBuilderPlugin(descriptions);
		}
		@Bean
		@ConditionalOnMissingBean
		public CustomParameterBuilderPlugin customParameterBuilderPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
			return new CustomParameterBuilderPlugin(descriptions, enumTypeDeterminer);
		}
	}
	
	
}
