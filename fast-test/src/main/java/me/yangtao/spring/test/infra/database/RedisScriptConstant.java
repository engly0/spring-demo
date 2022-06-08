package me.yangtao.spring.test.infra.database;

import org.springframework.data.redis.core.script.DefaultRedisScript;

public class RedisScriptConstant {
    public static final DefaultRedisScript<Long> REDIS_TEST_LUA_SCRIPT = new DefaultRedisScript<>(
//            "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end"
            "for i, key in ipairs(KEYS) do " +
                    "redis.call('set', key, ARGV[i], 'EX', 36000) " +
                    "end " +
                    "return 1"
            , Long.class);
}
