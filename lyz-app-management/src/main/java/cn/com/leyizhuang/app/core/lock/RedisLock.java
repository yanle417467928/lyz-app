package cn.com.leyizhuang.app.core.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import javax.annotation.Resource;

/**
 * @Description: ${todo}
 * @Author Richard
 * @Date 2018/5/214:35
 */
@Component
public class RedisLock implements DistributedLock {

    public RedisLock() {
    }

    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);
    private static final int DEFAULT_EXPIRE_TIME = 60;
    private static final String LOCK_KEY_PREFIX = "REDIS_LOCK";

    @Resource
    private JedisPool jedisPool;

    @Override
    public boolean lock(String module, String bizKey, int expireTime) {
        boolean flag;
        if (module != null && !"".equals(module) && bizKey != null && !"".equals(bizKey)) {
            if (this.jedisPool != null) {
                Jedis jedis = null;
                String lockKey = this.getLockKey(module, bizKey);
                try {
                    jedis = jedisPool.getResource();//获取到数据源
                    long e = System.currentTimeMillis();
                    long result = jedis.setnx(SafeEncoder.encode(lockKey), SafeEncoder.encode(e + ""));
                    if (result == 1L) {
                        if (expireTime > 0) {
                            jedis.expire(SafeEncoder.encode(lockKey), expireTime);
                            if (logger.isDebugEnabled()) {
                                logger.debug("key：" + lockKey + " locked and expire time:" + expireTime + "s");
                            }
                        } else {
                            jedis.expire(SafeEncoder.encode(lockKey), 60);
                            if (logger.isDebugEnabled()) {
                                logger.debug("key：" + lockKey + " locked and expire time:" + 60 + "s");
                            }
                        }
                        flag = true;
                    } else {
                        logger.info("key：" + lockKey + " has already bean locked");
                        flag = false;
                    }
                    return flag;
                } catch (Exception e) {
                    logger.error("lock error", e);
                    flag = false;
                    return flag;
                } finally {
                    this.jedisPool.returnResource(jedis);
                }
            } else {
                logger.error("Jedis Pool is null");
                flag = false;
                return flag;
            }
        } else {
            logger.error("parameters is null");
            flag = false;
            return flag;
        }
    }

    @Override
    public void unlock(String module, String bizKey) {
        if (module == null || "".equals(module) || bizKey == null || "".equals(bizKey)) {
            logger.error("parameters is null");
        } else {
            if (this.jedisPool != null) {
                Jedis jedis = null;
                String lockKey = this.getLockKey(module, bizKey);
                try {
                    jedis = this.jedisPool.getResource();
                    jedis.del(SafeEncoder.encode(lockKey));
                    logger.info("unlock success! ");
                } catch (Exception e) {
                    logger.error("unlock error", e);
                } finally {
                    this.jedisPool.returnResource(jedis);
                }
            } else {
                logger.error("jedis pool is null");
            }
        }
    }


    //组装key值
    private String getLockKey(String module, String bizKey) {
        StringBuffer sb = new StringBuffer();
        sb.append("REDIS_LOCK").append(":").append("LYZ_APP_MANAGEMENT").append(":").append(module).append(":").append(bizKey);
        return sb.toString();
    }
}
