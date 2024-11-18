package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;

import static java.util.Optional.ofNullable;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import java.util.function.Predicate;

import org.springframework.core.Ordered;

import com.gm.javaeaseframe.common.annotation.CustomApiModel;

import springfox.documentation.schema.DefaultTypeNameProvider;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

//@Component
//@Primary
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomModelTypeNameProvider extends DefaultTypeNameProvider implements Ordered {
  @Override
  public String nameFor(Class<?> type) {
	  CustomApiModel annotation = findAnnotation(type, CustomApiModel.class);
	    String defaultTypeName = super.nameFor(type);
	    if (annotation != null) {
	      return ofNullable(annotation.value())
	          .filter(((Predicate<String>) String::isEmpty).negate())
	          .orElse(defaultTypeName);
	    }
	    return defaultTypeName;
  }
 
  @Override
  public boolean supports(DocumentationType delimiter) {
    return SwaggerPluginSupport.pluginDoesApply(delimiter);
  }

@Override
public int getOrder() {
	return Ordered.HIGHEST_PRECEDENCE;
}
  
}
