package com.gm.javaeaseframe.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomExtensionProperty {
	/**
     * The name of the property.
     *
     * @return the name of the property
     */
    String name();

    /**
     * The value of the property.
     *
     * @return the value of the property
     */
    String value();

    /**
     * If set to true, field `value` will be parsed and serialized as JSON/YAML
     *
     * @return the value of `parseValue` annotation field
     */
    boolean parseValue() default false;
}
