package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import com.fasterxml.classmate.TypeResolver;
import com.gm.javaeaseframe.common.annotation.CustomApiModel;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;

//@Component
public class CustomModelBuilderPlugin implements ModelBuilderPlugin, Ordered {
  private final TypeResolver typeResolver;
 
//  @Autowired
  public CustomModelBuilderPlugin(TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }
  
	@Override
	public int getOrder() {
		return 0;
	}
 
  @Override
  public void apply(ModelContext context) {
	CustomApiModel annotation = AnnotationUtils.findAnnotation(forClass(context), CustomApiModel.class);
    if (annotation != null) {
      context.getBuilder().description(annotation.description());
    }
  }
 
  private Class<?> forClass(ModelContext context) {
    return typeResolver.resolve(context.getType()).getErasedType();
  }
 
  @Override
  public boolean supports(DocumentationType delimiter) {
    return true;
  }
}
