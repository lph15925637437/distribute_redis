package com.lph.dr.distribute_redis.config;

import com.lph.dr.distribute_redis.annotation.RateLimiter;
import com.lph.dr.distribute_redis.util.RedisRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 进行@RateLimiter注解方法的拦截并限流(单机)
 * @author: lph
 * @date:  2019/6/11 13:52
 * @version V1.0
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
 
                if (rateLimiter != null){
                    int limit = rateLimiter.limit();
                    int timeout = rateLimiter.timeout();
                    Jedis jedis = jedisPool.getResource();
                    String token = RedisRateLimiter.acquireTokenFromBucket(jedis, limit, timeout, redisTemplate);
                    if (token == null) {
                        logger.info("进行限流后的处理");
                        response.sendError(500, "进行限流处理");
                        return false;
                    }
                    logger.info("token -> {}",token);
                    jedis.close();
                }
                return true;
            }
        }).addPathPatterns("/*");
    }
}