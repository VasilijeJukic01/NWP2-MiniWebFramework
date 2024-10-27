package com.nwp.annotations;

import java.lang.annotation.*;

/**
 * Mark a class as a bean that should be managed by the IoC container.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Bean {
    String scope() default "singleton";
}
