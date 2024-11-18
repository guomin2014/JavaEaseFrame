package com.gm.javaeaseframe.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an authorization scheme to be used on a resource or an operation.
 * <p>
 * The authorization scheme used needs to be declared at the Swagger root level first.
 * <p>
 * This annotation is not used directly and will not be parsed by Swagger. It should be used
 * within either {@link Api} or {@link ApiOperation}.
 *
 * @see ApiOperation
 * @see Api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAuthorization {
	/**
     * The name of the authorization scheme to be used on this resource/operation.
     * <p>
     * The name must be defined in the Resource Listing's authorization section,
     */
    String value();

    /**
     * The scopes to be used if the authorization scheme is OAuth2.
     *
     * @see AuthorizationScope
     */
    CustomAuthorizationScope[] scopes() default @CustomAuthorizationScope(scope = "", description = "");
}
