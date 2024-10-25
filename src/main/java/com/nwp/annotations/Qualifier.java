package com.nwp.annotations;

import java.lang.annotation.*;

/**
 * Mark a field as a dependency that should be injected by the IoC container.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Qualifier {
    String value();
}
