package com.lph.dr.distribute_redis.component;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class JedisClientTemplate {
    public static final Logger logger = LoggerFactory.getLogger(JedisClientTemplate.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
            return true;
        } catch (Exception e) {
            logger.error("error:{}", e.getMessage());
        }
        return false;

    }

    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("error:{}", e.getMessage());
        }
        return null;

    }
}
