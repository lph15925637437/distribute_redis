package com.lph.dr.distribute_redis.controller;

import com.alibaba.fastjson.JSON;
import com.lph.dr.distribute_redis.annotation.RateLimiter;
import com.lph.dr.distribute_redis.component.DistributedLock;
import com.lph.dr.distribute_redis.component.JedisClientTemplate;
import com.lph.dr.distribute_redis.config.redis.User;
import com.lph.dr.distribute_redis.util.CheckConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RateLimitController {

    @Autowired
    private JedisClientTemplate jedisClientTemplate;

    @Autowired
    private RedisConnection getRedisConnection;

    @Autowired
    private DistributedLock distributedLock;

    @RateLimiter(limit = 2, timeout = 5000)
    @RequestMapping("/ratelimit")
    public String rateLimit(){
        return "项目搭建成功";
    }

    @RequestMapping("/cluster")
    public String setCluster(){
        String cluster = jedisClientTemplate.get("user3");
        System.err.println(cluster);
        User user = CheckConvertUtil.getConvertResult(cluster, User.class);
        System.err.println(user);

        return JSON.toJSONString(user);
    }

    @RequestMapping("/set")
    public String set(){
        boolean set = jedisClientTemplate.set("user3", new User("刘德华", 56));
        System.err.println("==============set:" + set);
        if (set) {
            return "成功";
        }
        return "失败";
    }

    @RequestMapping("testlock")
    public String getLock(){
        boolean lock2 = distributedLock.lock("liupeihan:lock2", "liupeihan", 10L, TimeUnit.SECONDS);
        distributedLock.unlock("liupeihan:lock2");
        if (lock2) {
            return "加锁成功";
        }
        return "锁已存在";
    }
}
