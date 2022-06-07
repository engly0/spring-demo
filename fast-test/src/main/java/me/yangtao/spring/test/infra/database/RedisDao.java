package me.yangtao.spring.test.infra.database;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
public class RedisDao {

    @Resource(name = "clusterStringRedisTemplate")
    private RedisTemplate<String, String> clusterStringRedisTemplate;

    @Resource(name = "clusterNumberRedisTemplate")
    private RedisTemplate<String, Number> clusterNumberRedisTemplate;

    /**
     * 从redis获取单条数据
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return (String) clusterStringRedisTemplate.opsForValue().get(key);
    }

    public void set(String key, String val, long timeout, TimeUnit unit) {
        clusterStringRedisTemplate.opsForValue().set(key, val, timeout, unit);
    }

    /**
     * 从redis获取多条数据
     *
     * @param keyList 键[]
     * @return 值[]
     */

    public List<String> multiGet(List<String> keyList) {
        return clusterStringRedisTemplate.opsForValue().multiGet(keyList);
    }

    /**
     * 假如这个key不存在设置这个key的过期时间，单位是秒
     *
     * @param key        键
     * @param value      值
     * @param expireTime 有效时间
     * @return bool
     */

    public Boolean setIfAbsent(String key, String value, long expireTime, TimeUnit timeUnit) {
        return clusterStringRedisTemplate.opsForValue().setIfAbsent(key, value, expireTime, timeUnit);
    }

    /**
     * 查看redis是否存在这个key
     *
     * @param key 键
     * @return bool
     */

    public Boolean hasKey(String key) {
        return clusterStringRedisTemplate.hasKey(key);
    }

    /**
     * 从redis删除key
     *
     * @param key 键
     */

    public void delete(String key) {
        clusterStringRedisTemplate.delete(key);
    }

    /**
     * hget
     *
     * @param key
     * @param hashKey
     * @return
     */
    public String hget(String key, String hashKey) {
        Object o = clusterStringRedisTemplate.opsForHash().get(key, hashKey);
        return o == null ? null : o.toString();
    }

    /**
     * hmget
     *
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hmgetFromRedis(String key, Collection<Object> fields) {
        return clusterStringRedisTemplate.opsForHash().multiGet(key, fields);
    }

    /**
     * 非公平分布式锁，可配置阻塞重试次数（阻塞间隔7+random.nextInt(10)毫秒，为防止锁在同一瞬间被争抢）
     *
     * @param cacheKey
     * @param timeoutInSecond
     * @param func
     * @param retryTimes
     * @param <R>
     * @return
     * @throws Exception
     */

    public <R> R tryLock(String cacheKey, int timeoutInSecond, Supplier<R> func, int retryTimes) throws Exception {
        return tryLock(cacheKey, timeoutInSecond, func, retryTimes, 7);
    }

    /**
     * 非公平分布式锁，可配置阻塞重试次数和每次阻塞间隔（阻塞间隔+random.nextInt(10)毫秒，为防止锁在同一瞬间被争抢）
     *
     * @param cacheKey
     * @param timeoutInSecond
     * @param func
     * @param retryTimes
     * @param sleepMillis
     * @param <R>
     * @return
     * @throws Exception
     */

    public <R> R tryLock(String cacheKey, int timeoutInSecond, Supplier<R> func, int retryTimes, long sleepMillis) throws Exception {
        R ret = null;
        while (retryTimes > 0) {
            ret = tryLock(cacheKey, timeoutInSecond, func);
            if (ret != null) {
                break;
            }
            try {
                Thread.sleep(sleepMillis + RandomUtils.nextInt(1, 10));
            } catch (Exception e) {
                log.error("sleep Exception:", e);
            }
            retryTimes--;
        }
        return ret;
    }

    /**
     * 分布式锁，遇到已锁定时返回null，表示锁争抢失败
     *
     * @param cacheKey
     * @param timeoutInSecond
     * @param func
     * @param <R>
     * @return
     * @throws Exception
     */

    public <R> R tryLock(String cacheKey, int timeoutInSecond, Supplier<R> func) throws Exception {
        boolean lockEnquired = false;

        long timeoutInMillis = timeoutInSecond * 1000L + 1;
        R o = null;
        try {
            if (lockEnquired = this.setIfAbsent(cacheKey,
                    String.valueOf(System.currentTimeMillis() + timeoutInMillis),
                    timeoutInSecond,
                    TimeUnit.SECONDS)) {
                o = func.get();
            }
        } finally {
            if (lockEnquired) {
                String val = this.get(cacheKey);
                if (val == null) {
                    log.error("timeoutInSecond was too short.cacheKey:{}", cacheKey);
                } else {
                    this.delete(cacheKey);
                }
            }
        }

        return o;
    }
}
