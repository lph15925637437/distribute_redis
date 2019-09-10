package com.lph.dr.distribute_redis.constant;


/**
 * @version V1.0
 * @author: lph
 * @date: 2019/6/11 16:53
 */
public class RedisConstant {

    // @formatter:off 带该注释的注解不进行格式化
    public static final String KEY_PRE =          "lock:%s"; // 分布式锁前缀

    // @formatter:on 带该注释的注解进行格式化
    public static final String EX_RET_STATUS = "OK"; // 加锁后返回状态码

}
