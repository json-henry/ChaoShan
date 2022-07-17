package com.chaoshan.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @DATE: 2022/05/05 20:13
 * @Author: 小爽帅到拖网速
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotationSwagger {
    String value() default "";
}
