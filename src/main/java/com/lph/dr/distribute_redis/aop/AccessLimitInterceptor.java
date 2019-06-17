package com.lph.dr.distribute_redis.aop;

import com.lph.dr.distribute_redis.annotation.RateLimiter;
import com.lph.dr.distribute_redis.exception.DistributedErrorEnum;
import com.lph.dr.distribute_redis.exception.DistributedException;
import com.lph.dr.distribute_redis.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 可以用于单机或者集群环境下进行流量控制类
 *
 * @version V1.0
 * @author: lph
 * @date: 2019/6/17 17:26
 */
@Component
public class AccessLimitInterceptor extends WebMvcConfigurerAdapter {

    public static final Logger logger = LoggerFactory.getLogger(AccessLimitInterceptor.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                try {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    Method method = handlerMethod.getMethod();
                    // 是否有RateLimiter注解
                    if (!method.isAnnotationPresent(RateLimiter.class)) {
                        return true;
                    }
                    // 获取注解内信息
                    RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
                    if (rateLimiter == null) {
                        return true;
                    }
                    int limit = rateLimiter.limit();
                    int timeout = rateLimiter.timeout();
                    // (1)根据IP + API限流该方式主要限制统一ip下恶意请求操作, (2)可以通过随机数做redis的key这里我选择API方式进行限流,该方式主要是限制api某个事件端访问量的控制
                    String key = IpUtils.getIpAddr(request) + request.getRequestURI();
                    // String key = request.getRequestURI();
                    ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
                    String times = opsForValue.get(key);
                    if (times == null) {
                        opsForValue.set(key, "1", timeout, TimeUnit.SECONDS);
                    } else if (Integer.valueOf(times) < limit) {
                        opsForValue.set(key, String.valueOf(Integer.valueOf(times) + 1), timeout, TimeUnit.SECONDS);
                    } else {
                        logger.info("进行限流后的处理:{}", key);
                        response.sendError(Integer.valueOf(DistributedErrorEnum.API_REQUEST_TOO_MUCH.getErrorCode()), DistributedErrorEnum.API_REQUEST_TOO_MUCH.getMsg());
                        return false;
                    }
                } catch (NumberFormatException e) {
                    throw new DistributedException(e.getMessage());
                } catch (Exception e) {
                    throw new DistributedException(DistributedErrorEnum.BAD_REQUEST.getErrorCode(), DistributedErrorEnum.BAD_REQUEST.getMsg());
                }
                return true;
            }
        }).addPathPatterns("/*");
    }
}