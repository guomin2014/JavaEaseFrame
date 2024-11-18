package com.gm.javaeaseframe.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A mediaType/value property within a Swagger example
 *
 * @see Example
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomExampleProperty {
	/**
     * The name of the property.
     *
     * @return the name of the property
     */
    String mediaType() default "";

    /**
     * The value of the example.
     *
     * @return the value of the example
     */
    String value();
}
