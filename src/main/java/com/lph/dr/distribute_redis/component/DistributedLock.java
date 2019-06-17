package com.lph.dr.distribute_redis.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


/**
 * 分布式锁操作(单机,集群)
 *
 * @version V1.0
 * @author: lph
 * @date: 2019/6/17 10:25
 */
@Component
public class DistributedLock {

    public static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁，超时后必须手动调用unlock方法进行解锁
     *
     * @param key         锁唯一标志
     * @param timeout     超时时间,单位为秒
     * @param currentTime 当前时间戳
     * @return
     */
    public boolean lock(String key, long timeout, long currentTime) {

        String value = String.valueOf(timeout * 1000 + currentTime);

        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            logger.info("key不存在时插入锁操作");
            return true;
        }

        //判断锁超时,防止死锁
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间value
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);

            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                //校验是不是上个对应的商品时间戳,也是防止并发,解锁操作
                unlock(key);
                return redisTemplate.opsForValue().setIfAbsent(key, value);
            }
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     */
    public void unlock(String key) {
        try {
            logger.info("解鎖");
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue)) {
                redisTemplate.opsForValue().getOperations().delete(key);//删除key
            }
        } catch (Exception e) {
            logger.error("[Redis分布式锁] 解锁出现异常了，{}", e);
        }
    }

    /**
     *  加锁,超时后自动解锁
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public boolean lock(String key, String value, long timeout, TimeUnit unit) {
        String ret = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(ret) || !ret.equals(value)) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        }

        return false;
    }
}
