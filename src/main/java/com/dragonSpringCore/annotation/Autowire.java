package com.dragonSpringCore.annotation;

import java.lang.annotation.*;


/**
 * 依赖注入注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Autowire {
    String value() default "";
}
