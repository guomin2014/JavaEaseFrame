package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.plug;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.gm.javaeaseframe.common.annotation.CustomApiModelProperty;

import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spring.web.DescriptionResolver;

public class CustomApiModelProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomApiModelProperties.class);
	  private static final Pattern RANGE_PATTERN = Pattern.compile("range([\\[(])(.*),(.*)([])])$");

	  private CustomApiModelProperties() {
	    throw new UnsupportedOperationException();
	  }

	  static Function<CustomApiModelProperty, AllowableValues> toAllowableValues() {
	    return annotation -> allowableValueFromString(annotation.allowableValues());
	  }

	  public static AllowableValues allowableValueFromString(String allowableValueString) {
	    AllowableValues allowableValues = new AllowableListValues(new ArrayList<String>(), "LIST");
	    String trimmed = allowableValueString.trim();
	    Matcher matcher = RANGE_PATTERN.matcher(trimmed.replaceAll(" ", ""));
	    if (matcher.matches()) {
	      if (matcher.groupCount() != 4) {
	        LOGGER.warn("Unable to parse range specified {} correctly", trimmed);
	      } else {
	        allowableValues = new AllowableRangeValues(
	            matcher.group(2).contains("infinity") ? null : matcher.group(2),
	            matcher.group(1).equals("("),
	            matcher.group(3).contains("infinity") ? null : matcher.group(3),
	            matcher.group(4).equals(")"));
	      }
	    } else if (trimmed.contains(",")) {
	      List<String> split =
	          Stream.of(trimmed.split(",")).map(String::trim).filter(item -> !item.isEmpty()).collect(toList());
	      allowableValues = new AllowableListValues(split, "LIST");
	    } else if (hasText(trimmed)) {
	      List<String> singleVal = singletonList(trimmed);
	      allowableValues = new AllowableListValues(singleVal, "LIST");
	    }
	    return allowableValues;
	  }

	  static Function<CustomApiModelProperty, String> toDescription(
	      final DescriptionResolver descriptions) {

	    return annotation -> {
	      String description = "";
	      if (!isEmpty(annotation.value())) {
	        description = annotation.value();
	      } else if (!isEmpty(annotation.notes())) {
	        description = annotation.notes();
	      }
	      return descriptions.resolve(description);
	    };
	  }

	  static Function<CustomApiModelProperty, ResolvedType> toType(final TypeResolver resolver) {
	    return annotation -> {
	      try {
	        return resolver.resolve(Class.forName(annotation.dataType()));
	      } catch (ClassNotFoundException e) {
	        return resolver.resolve(Object.class);
	      }
	    };
	  }

	  public static Optional<CustomApiModelProperty> findApiModePropertyAnnotation(AnnotatedElement annotated) {
	    Optional<CustomApiModelProperty> annotation = empty();

	    if (annotated instanceof Method) {
	      // If the annotated element is a method we can use this information to check superclasses as well
	      annotation = ofNullable(AnnotationUtils.findAnnotation(((Method) annotated), CustomApiModelProperty.class));
	    }

	    return annotation.map(Optional::of).orElse(ofNullable(AnnotationUtils.getAnnotation(annotated,
	    		CustomApiModelProperty.class)));
	  }

	  static Function<CustomApiModelProperty, String> toExample() {
	    return annotation -> {
	      String example = "";
	      if (!isEmpty(annotation.example())) {
	        example = annotation.example();
	      }
	      return example;
	    };
	  }
}
