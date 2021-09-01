package mySpring.annotation;

import java.lang.annotation.*;

/**
 * 控制类注解，被标记的类将作为框架中的控制类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Controller {
    String value() default "";
}

