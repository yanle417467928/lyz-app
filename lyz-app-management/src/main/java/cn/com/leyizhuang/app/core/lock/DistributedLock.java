package cn.com.leyizhuang.app.core.lock;

/**
 * @Description: Redis
 * @Author Richard
 * @Date 2018/5/214:34
 */
public interface DistributedLock {

    /**
     * 加锁
     *
     * @param module     业务模块
     * @param bizKey     业务键
     * @param expireTime 过期时间，单位秒
     * @return 加锁结果
     */
    boolean lock(String module, String bizKey, int expireTime);

    /**
     * 释放锁
     *
     * @param module 模块
     * @param bizKey 业务键
     */
    void unlock(String module, String bizKey);
}
