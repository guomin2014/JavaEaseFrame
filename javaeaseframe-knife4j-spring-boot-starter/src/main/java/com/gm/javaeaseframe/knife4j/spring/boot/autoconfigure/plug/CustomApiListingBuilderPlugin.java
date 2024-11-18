package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static springfox.documentation.service.Tags.emptyTags;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.core.Ordered;

import com.gm.javaeaseframe.common.annotation.CustomApi;
import com.gm.javaeaseframe.common.util.ReflectionUtil;

import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;

public class CustomApiListingBuilderPlugin implements ApiListingBuilderPlugin, Ordered {

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 2001;
	}
	
	@Override
	public boolean supports(DocumentationType delimiter) {
		return pluginDoesApply(delimiter);
	}

	@Override
	public void apply(ApiListingContext apiListingContext) {
		Optional<? extends Class<?>> controller = apiListingContext.getResourceGroup().getControllerClass();
	    if (controller.isPresent()) {
	      Optional<CustomApi> apiAnnotation = ofNullable(findAnnotation(controller.get(), CustomApi.class));
	      if (apiAnnotation.isPresent()) {
		      String description =
		          apiAnnotation.map(CustomApi::description).filter(((Predicate<String>) String::isEmpty).negate())
		              .orElse(null);
	
		      Set<String> tagSet = apiAnnotation.map(tags())
		          .orElse(new TreeSet<>());
		      if (tagSet.isEmpty()) {
		        tagSet.add(apiListingContext.getResourceGroup().getGroupName());
		      }
		      ApiListingBuilder builder = apiListingContext.apiListingBuilder();
		      try {
		    	  //清除已经存在的tag
			      Object tagNames = ReflectionUtil.getFieldValue(builder, "tagNames");
			      if (tagNames != null) {
			    	  ((Set<?>)tagNames).clear();
			      }
			      Object tags = ReflectionUtil.getFieldValue(builder, "tags");
			      if (tags != null) {
			    	  ((Set<?>)tags).clear();
			      }
		      } catch (Exception e) {}
		      builder
		          .description(description)
		          .tagNames(tagSet);
	      }
	    }
	}
	private Function<CustomApi, Set<String>> tags() {
	    return input -> Stream.of(input.tags())
	        .filter(emptyTags())
	        .collect(toCollection(TreeSet::new));
	  }

}
