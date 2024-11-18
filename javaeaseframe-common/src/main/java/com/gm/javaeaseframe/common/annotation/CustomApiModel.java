package com.gm.javaeaseframe.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CustomApiModel {

	/**
     * Provide an alternative name for the model.
     * <p>
     * By default, the class name is used.
     */
    String value() default "";

    /**
     * Provide a longer description of the class.
     */
    String description() default "";

    /**
     * Provide a superclass for the model to allow describing inheritance.
     */
    Class<?> parent() default Void.class;

    /**
     * Supports model inheritance and polymorphism.
     * <p>
     * This is the name of the field used as a discriminator. Based on this field,
     * it would be possible to assert which sub type needs to be used.
     */
    String discriminator() default "";

    /**
     * An array of the sub types inheriting from this model.
     */
    Class<?>[] subTypes() default {};

    /**
     * Specifies a reference to the corresponding type definition, overrides any other metadata specified
     */

    String reference() default "";
}
