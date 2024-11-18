package com.gm.javaeaseframe.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes an OAuth2 authorization scope.
 * <p>
 * Used to define an authorization scope that is used by an operation for
 * a defined authorization scheme.
 * <p>
 * This annotation is not used directly and will not be parsed by Swagger. It should be used
 * within the {@link Authorization}.
 *
 * @see Authorization
 * @see ApiOperation
 * @see Api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAuthorizationScope {
	/**
     * The scope of the OAuth2 Authorization scheme to be used.
     * <p>
     * The scope should be previously declared in the Swagger Object's securityDefinition section.
     */
    String scope();

    /**
     * Not used in 1.5.X, kept for legacy support.
     */
    String description();
}
