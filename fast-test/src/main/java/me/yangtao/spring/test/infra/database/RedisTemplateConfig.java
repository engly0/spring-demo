package me.yangtao.spring.test.infra.database;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;


/**
 * redisTemplate 配置管理类
 */
@Configuration
@Slf4j
public class RedisTemplateConfig extends RedisClusterConfig {

    @Resource
    private RedisPoolConfig redisPoolConfig;

    @Bean(name = "clusterConnectionFactory")
    public LettuceConnectionFactory clusterConnectionFactory() {
        return getLettuceConnectionFactory(redisPoolConfig);
    }

    @Bean(name = "stringRedisSerializer")
    public StringRedisSerializer getStringRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Bean(name = "clusterStringRedisTemplate")
    public RedisTemplate<String, String> redisClusterTemplate4String(
            @Qualifier("clusterConnectionFactory") LettuceConnectionFactory redisClusterConnectionFactory,
            @Qualifier("stringRedisSerializer") StringRedisSerializer stringRedisSerializer) {
        return getRedisTemplate(redisClusterConnectionFactory, stringRedisSerializer);
    }

    @Bean(name = "clusterNumberRedisTemplate")
    public RedisTemplate<String, Number> redisClusterTemplate4Number(
            @Qualifier("clusterConnectionFactory") LettuceConnectionFactory redisClusterConnectionFactory) {
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        serializer.setObjectMapper(mapper);
        return getRedisTemplate(redisClusterConnectionFactory, serializer);
    }
    
    @Override
    protected RedisTemplate getRedisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer serializer) {
        val stringSerializer = new StringRedisSerializer();
        val redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
