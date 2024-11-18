package com.gm.javaeaseframe.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An optionally named list of example properties.
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomExample {
	/**
     * The examples properties.
     *
     * @return the actual extension properties
     * @see ExampleProperty
     */
	CustomExampleProperty[] value();
}
