package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.isEmpty;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;
import static springfox.documentation.swagger.readers.parameter.Examples.examples;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;
import com.gm.javaeaseframe.common.annotation.CustomApiParam;
import com.gm.javaeaseframe.common.annotation.CustomExample;
import com.gm.javaeaseframe.common.annotation.CustomExampleProperty;

import io.swagger.annotations.ApiParam;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.schema.ApiModelProperties;

//@Component
//@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
//@Conditional(SpringIntegrationPluginNotPresentInClassPathCondition.class)
public class CustomParameterBuilderPlugin implements ExpandedParameterBuilderPlugin, Ordered {

	private final DescriptionResolver descriptions;
	private final EnumTypeDeterminer enumTypeDeterminer;

	@Autowired
	public CustomParameterBuilderPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
		this.descriptions = descriptions;
		this.enumTypeDeterminer = enumTypeDeterminer;
	}

	@Override
	public int getOrder() {
		return SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1;
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}

	@Override
	public void apply(ParameterExpansionContext context) {
		Optional<CustomApiModelProperty> apiModelPropertyOptional = context.findAnnotation(CustomApiModelProperty.class);
		apiModelPropertyOptional.ifPresent(apiModelProperty -> fromApiModelProperty(context, apiModelProperty));
		Optional<CustomApiParam> customApiParamOptional = context.findAnnotation(CustomApiParam.class);
		customApiParamOptional.ifPresent(apiParam -> fromCustomApiParam(context, apiParam));
		Optional<ApiParam> apiParamOptional = context.findAnnotation(ApiParam.class);
		apiParamOptional.ifPresent(apiParam -> fromApiParam(context, apiParam));
	}

	private void fromApiModelProperty(ParameterExpansionContext context, CustomApiModelProperty apiModelProperty) {
		String allowableProperty = ofNullable(apiModelProperty.allowableValues())
				.filter(((Predicate<String>) String::isEmpty).negate()).orElse(null);
		AllowableValues allowable = allowableValues(ofNullable(allowableProperty),
				context.getFieldType().getErasedType());

		maybeSetParameterName(context, apiModelProperty.name())
				.description(descriptions.resolve(apiModelProperty.value())).required(apiModelProperty.required())
				.allowableValues(allowable).parameterAccess(apiModelProperty.access()).hidden(apiModelProperty.hidden())
				.scalarExample(apiModelProperty.example()).order(SWAGGER_PLUGIN_ORDER).build();
	}

	private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
		String allowableProperty = ofNullable(apiParam.allowableValues())
				.filter(((Predicate<String>) String::isEmpty).negate()).orElse(null);
		AllowableValues allowable = allowableValues(ofNullable(allowableProperty),
				context.getFieldType().getErasedType());

		maybeSetParameterName(context, apiParam.name()).description(descriptions.resolve(apiParam.value()))
				.defaultValue(apiParam.defaultValue()).required(apiParam.required())
				.allowMultiple(apiParam.allowMultiple()).allowableValues(allowable).parameterAccess(apiParam.access())
				.hidden(apiParam.hidden()).scalarExample(apiParam.example())
				.complexExamples(examples(apiParam.examples())).order(SWAGGER_PLUGIN_ORDER).build();
	}
	private void fromCustomApiParam(ParameterExpansionContext context, CustomApiParam apiParam) {
		String allowableProperty = ofNullable(apiParam.allowableValues())
				.filter(((Predicate<String>) String::isEmpty).negate()).orElse(null);
		AllowableValues allowable = allowableValues(ofNullable(allowableProperty),
				context.getFieldType().getErasedType());
		
		maybeSetParameterName(context, apiParam.name()).description(descriptions.resolve(apiParam.value()))
		.defaultValue(apiParam.defaultValue()).required(apiParam.required())
		.allowMultiple(apiParam.allowMultiple()).allowableValues(allowable).parameterAccess(apiParam.access())
		.hidden(apiParam.hidden()).scalarExample(apiParam.example())
		.complexExamples(customExamples(apiParam.examples())).order(SWAGGER_PLUGIN_ORDER).build();
	}

	private ParameterBuilder maybeSetParameterName(ParameterExpansionContext context, String parameterName) {
		if (!isEmpty(parameterName)) {
			context.getParameterBuilder().name(parameterName);
		}
		return context.getParameterBuilder();
	}

	private AllowableValues allowableValues(final Optional<String> optionalAllowable, Class<?> fieldType) {

		AllowableValues allowable = null;
		if (enumTypeDeterminer.isEnum(fieldType)) {
			allowable = new AllowableListValues(getEnumValues(fieldType), "LIST");
		} else if (optionalAllowable.isPresent()) {
			allowable = ApiModelProperties.allowableValueFromString(optionalAllowable.get());
		}
		return allowable;
	}

	private List<String> getEnumValues(final Class<?> subject) {
		return Stream.of(subject.getEnumConstants()).map((Function<Object, String>) Object::toString).collect(toList());
	}
	
	public static Map<String, List<Example>> customExamples(CustomExample example) {
		Map<String, List<Example>> examples = new HashMap<>();
		for (CustomExampleProperty each : example.value()) {
			if (!isEmpty(each.value())) {
				examples.putIfAbsent(each.mediaType(), new LinkedList<>());
				examples.get(each.mediaType()).add(new Example(ofNullable(each.mediaType())
						.filter(((Predicate<String>) String::isEmpty).negate()).orElse(null), each.value()));
			}
		}
		return examples;
	}

}
