package com.lph.dr.distribute_redis.component;

import com.lph.dr.distribute_redis.constant.RedisConstant;
import com.lph.dr.distribute_redis.exception.DistributedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;

/**
 * 通过redis实现分布式锁工具类
 * @author: lph
 * @date:  2019/6/11 15:55
 * @version V1.0
 */
@Component
public class DistributedLockComponent {

    public static final Logger logger = LoggerFactory.getLogger(DistributedLockComponent.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     *  加锁
     * @param lockName 锁的key
     * @param acquireTimeout 获取超时时间
     * @param timeout 锁的超时时间
     * @return
     */
    public String lockWithTimeout(String lockName, long acquireTimeout, long timeout){
        Jedis conn = null;
        String retIdentifier = null;
        try {
            // 获取连接
            conn = jedisPool.getResource();
            // 随机生成一个value
            String identifier = UUID.randomUUID().toString();
            // 锁名，即key值
            String lockKey = RedisConstant.KEY_PRE + lockName;
            // 超时时间，上锁后超过此时间则自动释放锁
            int lockExpire = (int)(timeout / 1000);

            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                if (RedisConstant.EX_RET_STATUS.equals(conn.setex(lockKey, lockExpire, identifier))) {
                    conn.expire(lockKey, lockExpire);
                    // 返回value值，用于释放锁时间确认
                    retIdentifier = identifier;
                    return retIdentifier;
                }
                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (conn.ttl(lockKey) == -1) {
                    conn.expire(lockKey, lockExpire);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e) {
            throw new DistributedException(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return retIdentifier;

    }

    /**
     * 释放锁
     * @param lockName 锁的key
     * @param identifier    释放锁的标识(加锁后返回的标识)
     * @return
     */
    public boolean releaseLock(String lockName, String identifier) {
        Jedis conn = null;
        String lockKey = RedisConstant.KEY_PRE + lockName;
        boolean retFlag = false;
        try {
            conn = jedisPool.getResource();
            while (true) {
                // 监视lock，准备开始事务
                conn.watch(lockKey);
                // 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
                if (!StringUtils.isEmpty(identifier) && identifier.equals(conn.get(lockKey))) {
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> results = transaction.exec();
                    if (results == null) {
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                break;
            }
        } catch (JedisException e) {
            throw new DistributedException(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return retFlag;
    }
}
