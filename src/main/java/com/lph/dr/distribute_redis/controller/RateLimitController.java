package com.lph.dr.distribute_redis.controller;

import com.lph.dr.distribute_redis.annotation.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {

    @RateLimiter(limit = 2, timeout = 5000)
    @RequestMapping("/ratelimit")
    public String rateLimit(){
        return "项目搭建成功";
    }
}
