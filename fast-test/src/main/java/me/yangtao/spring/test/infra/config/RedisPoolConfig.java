package me.yangtao.spring.test.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "redis")
@Data
public class RedisPoolConfig {
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int maxWaitMillis;
    private long commandTimeout;
    private String redisAuth;
    private List<RedisClusterNode> clusterNodes;
}
