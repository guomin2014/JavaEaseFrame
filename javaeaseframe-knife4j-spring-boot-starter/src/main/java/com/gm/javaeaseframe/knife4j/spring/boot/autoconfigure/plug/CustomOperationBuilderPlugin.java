package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;

import com.gm.javaeaseframe.common.annotation.CustomApi;
import com.gm.javaeaseframe.common.annotation.CustomApiOperation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

//@Component
//@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
public class CustomOperationBuilderPlugin implements OperationBuilderPlugin, Ordered{

	private final DescriptionResolver descriptions;
    @Autowired
    public CustomOperationBuilderPlugin(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
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
	public void apply(OperationContext context) {
		Optional<CustomApiOperation> annotation = context.findAnnotation(CustomApiOperation.class);
		Optional<ApiOperation> oldAnnotation = context.findAnnotation(ApiOperation.class);
		Optional<Api> apiAnnotation = context.findControllerAnnotation(Api.class);
		Optional<CustomApi> customApiAnnotation = context.findControllerAnnotation(CustomApi.class);
		if (annotation.isPresent()) {
			String[] tags = annotation.get().tags();
			Set<String> tagSet = tagsFromArrays(tags);
			if (tagSet.isEmpty()) {
				if (customApiAnnotation.isPresent()) {
					tagSet = tagsFromArrays(customApiAnnotation.get().tags());
				} else if (apiAnnotation.isPresent()) {
					tagSet = tagsFromArrays(apiAnnotation.get().tags());
				}
			}
			context.operationBuilder()
			.notes(annotation.map(CustomApiOperation::notes).orElse(null))
			.tags(tagSet)
			.method(annotation.map(toMethodForCustom()).orElse(null))
			.hidden(annotation.map(CustomApiOperation::hidden).orElse(false))
			.summary(annotation.map(CustomApiOperation::value).orElse(null))
			.position(annotation.map(CustomApiOperation::position).orElse(1))
			.produces(annotation.map(toProducesForCustom()).orElse(null))
			.consumes(annotation.map(toConsumesForCustom()).orElse(null))
			.protocols(annotation.map(toProtocolsForCustom()).orElse(null))
			;
		} else if (oldAnnotation.isPresent()) {
			String[] tags = oldAnnotation.get().tags();
			Set<String> tagSet = tagsFromArrays(tags);
			if (tagSet.isEmpty()) {
				if (customApiAnnotation.isPresent()) {
					tagSet = tagsFromArrays(customApiAnnotation.get().tags());
				} else if (apiAnnotation.isPresent()) {
					tagSet = tagsFromArrays(apiAnnotation.get().tags());
				}
			}
			context.operationBuilder()
			.notes(oldAnnotation.map(ApiOperation::notes).orElse(null))
			.tags(tagSet)
			.method(oldAnnotation.map(toMethod()).orElse(null))
			.hidden(oldAnnotation.map(ApiOperation::hidden).orElse(false))
			.summary(oldAnnotation.map(ApiOperation::value).orElse(null))
			.position(oldAnnotation.map(ApiOperation::position).orElse(1))
			.produces(oldAnnotation.map(toProduces()).orElse(null))
			.consumes(oldAnnotation.map(toConsumes()).orElse(null))
			.protocols(oldAnnotation.map(toProtocols()).orElse(null))
			;
		} else {
			Set<String> tagSet = new HashSet<>();
			if (customApiAnnotation.isPresent()) {
				tagSet = tagsFromArrays(customApiAnnotation.get().tags());
			} else if (apiAnnotation.isPresent()) {
				tagSet = tagsFromArrays(apiAnnotation.get().tags());
			}
			if (!tagSet.isEmpty()) {
				context.operationBuilder()
				.tags(tagSet);
			}
		}
	}
	
	static Function<CustomApiOperation, Set<String>> toTags() {
	    return annotation -> tagsFromArrays(annotation.tags());
	}
	public static Set<String> tagsFromArrays(String[] tags) {
		if (tags != null && tags.length > 0) {
			Set<String> set = new HashSet<>();
			for (String tag : tags) {
				if (StringUtils.isNotBlank(tag)) {
					set.add(tag);
				}
			}
			return set;
		}
		return null;
	}
	public static Set<String> setFromArrays(String value) {
		Set<String> set = new HashSet<>();
		if (StringUtils.isNoneBlank(value)) {
			set.addAll(Arrays.asList(value.split(",")));
		}
		return set;
	}
	static Function<ApiOperation, HttpMethod> toMethod() {
	    return annotation -> {
	      return HttpMethod.resolve(annotation.httpMethod());
	    };
	  }
	static Function<ApiOperation, Set<String>> toProduces() {
	    return annotation -> setFromArrays(annotation.produces());
	}
	static Function<ApiOperation, Set<String>> toConsumes() {
		return annotation -> setFromArrays(annotation.consumes());
	}
	static Function<ApiOperation, Set<String>> toProtocols() {
		return annotation -> setFromArrays(annotation.protocols());
	}
	static Function<CustomApiOperation, HttpMethod> toMethodForCustom() {
		return annotation -> {
			return HttpMethod.resolve(annotation.httpMethod());
		};
	}
	static Function<CustomApiOperation, Set<String>> toProducesForCustom() {
		return annotation -> setFromArrays(annotation.produces());
	}
	static Function<CustomApiOperation, Set<String>> toConsumesForCustom() {
		return annotation -> setFromArrays(annotation.consumes());
	}
	static Function<CustomApiOperation, Set<String>> toProtocolsForCustom() {
		return annotation -> setFromArrays(annotation.protocols());
	}

}
