package com.nwp.annotations;

import java.lang.annotation.*;

/**
 * Mark a class as a controller that should be managed by the IoC container.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Controller { }
