package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;


import java.util.Optional;

import org.springframework.core.Ordered;

import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;

import springfox.documentation.schema.Annotations;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

//@Component
public class CustomModelPropertyPlugin implements ModelPropertyBuilderPlugin, Ordered {
	private final DescriptionResolver descriptions;

//	@Autowired
	public CustomModelPropertyPlugin(DescriptionResolver descriptions) {
		this.descriptions = descriptions;
	}
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void apply(ModelPropertyContext context) {
		Optional<CustomApiModelProperty> annotation = Optional.empty();
		if (context.getAnnotatedElement().isPresent()) {
			annotation = annotation.map(Optional::of)
					.orElse(CustomApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
		}
		if (context.getBeanPropertyDefinition().isPresent()) {
			annotation = annotation.map(Optional::of).orElse(
					Annotations.findPropertyAnnotation(context.getBeanPropertyDefinition().get(), CustomApiModelProperty.class));
		}
		if (annotation.isPresent()) {
			context.getBuilder().allowableValues(annotation.map(CustomApiModelProperties.toAllowableValues()).orElse(null))
					.required(annotation.map(CustomApiModelProperty::required).orElse(false))
					.readOnly(annotation.map(CustomApiModelProperty::readOnly).orElse(false))
					.description(annotation.map(CustomApiModelProperties.toDescription(descriptions)).orElse(null))
					.isHidden(annotation.map(CustomApiModelProperty::hidden).orElse(false))
					.type(annotation.map(CustomApiModelProperties.toType(context.getResolver())).orElse(null))
					.position(annotation.map(CustomApiModelProperty::position).orElse(1))
					.example(annotation.map(CustomApiModelProperties.toExample()).orElse(null));
		}
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}
}
