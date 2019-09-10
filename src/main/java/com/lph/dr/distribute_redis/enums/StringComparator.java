package com.lph.dr.distribute_redis.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用来标记要被排序的类中字段
 * @author: lph
 * @date:  2019/5/24 8:58
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringComparator {
}
