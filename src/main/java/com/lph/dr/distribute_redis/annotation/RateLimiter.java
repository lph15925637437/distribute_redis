package com.lph.dr.distribute_redis.annotation;

import java.lang.annotation.*;


/**
 * 限流注解类
 * @author: lph
 * @date:  2019/6/11 13:55
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 5;
    int timeout() default 1000;
}