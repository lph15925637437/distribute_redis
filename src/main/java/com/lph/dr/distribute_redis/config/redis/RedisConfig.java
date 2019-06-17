package com.lph.dr.distribute_redis.config.redis;

import com.lph.dr.distribute_redis.config.jedis.JedisPoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter(JedisPoolConfiguration.class)
public class RedisConfig {

    public static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String ipHostPair;
    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.max-redirects}")
    private Integer redirects;


    @Bean
    public RedisClusterConfiguration getClusterConfiguration() {
        if (ipHostPair.split(",").length > 1) {
            //如果是host是集群模式的才进行以下操作
            Map<String, Object> source = new HashMap<String, Object>();

            source.put("spring.redis.cluster.nodes", ipHostPair);

            source.put("spring.redis.cluster.timeout", timeout);

            source.put("spring.redis.cluster.max-redirects", redirects);
            //在源码的注释中可以看到是这样配置，以为这样写就不用在Connection中不用在认证，后来确定太天真了

            source.put("spring.redis.cluster.password", password);

            return new RedisClusterConfiguration(new
                    MapPropertySource("RedisClusterConfiguration", source));
        } else {
            return null;
        }
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        if (ipHostPair.split(",").length == 1) {
            logger.info("进入redis单机中");
            JedisConnectionFactory factory = new JedisConnectionFactory(jedisPoolConfig);
            factory.setHostName(ipHostPair.split(":")[0]);
            factory.setPort(Integer.valueOf(ipHostPair.split(":")[1]));
            factory.setPassword(password);
            factory.setTimeout(timeout);
            return factory;
        } else {
            JedisConnectionFactory jcf = new JedisConnectionFactory(getClusterConfiguration());
            jcf.setPassword(password); //集群的密码认证
            return jcf;
        }

    }

    @Bean
    public RedisConnection getRedisConnection(JedisConnectionFactory jedisConnectionFactory) {
        RedisConnection redisConnection = null;
        if (ipHostPair.split(",").length == 1) {
            redisConnection = jedisConnectionFactory.getConnection();
        } else {
            redisConnection = jedisConnectionFactory.getClusterConnection();
        }

        return redisConnection;
    }

    /**
     * 实例化 RedisTemplate 对象
     *
     * @return
     */
    @Bean
    public StringRedisTemplate functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }


    /**
     * 设置数据存入 redis 的序列化方式,并开启事务
     *
     * @param redisTemplate
     * @param factory
     */
    private void initDomainRedisTemplate(StringRedisTemplate redisTemplate, RedisConnectionFactory factory) {
        // 如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
    }

}
