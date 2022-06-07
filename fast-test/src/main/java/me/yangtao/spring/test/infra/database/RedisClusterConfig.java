package me.yangtao.spring.test.infra.database;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.yangtao.spring.test.infra.config.RedisClusterNode;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Slf4j
public class RedisClusterConfig {

    private final int maxRedirects = 5;

    /**
     * 获取连接工厂方法
     * @param redisPoolConfig
     * @return
     */
    protected LettuceConnectionFactory getLettuceConnectionFactory(RedisPoolConfig redisPoolConfig) {

        val poolConfig = getPoolConfig(redisPoolConfig.getMaxTotal(), redisPoolConfig.getMaxIdle(), redisPoolConfig.getMinIdle(),
                redisPoolConfig.getMaxWaitMillis());

        val clientConfig = LettucePoolingClientConfiguration.builder()
                        .commandTimeout(Duration.ofMillis(redisPoolConfig.getCommandTimeout()))
                        .clientOptions(getClientOptions(redisPoolConfig))
                        .clientResources(DefaultClientResources.create())
                        .poolConfig(poolConfig).build();

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        clusterConfig.setMaxRedirects(maxRedirects);
        List<RedisClusterNode> clusterNodes = redisPoolConfig.getClusterNodes();
        for (val clusterNode : clusterNodes) {
        	clusterConfig.addClusterNode(new RedisNode(clusterNode.getHost(), clusterNode.getPort()));
        }
        if (StringUtils.isNotBlank(redisPoolConfig.getRedisAuth())) {
        	clusterConfig.setPassword(redisPoolConfig.getRedisAuth());
        }

        final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfig, clientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    /**
     * 配置拓扑刷新
     * @return
     */
    protected ClientOptions getClientOptions(RedisPoolConfig redisPoolConfig) {
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
        		.enablePeriodicRefresh(true)
        		.refreshPeriod(Duration.ofSeconds(60))
        		.enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT,
                        ClusterTopologyRefreshOptions.RefreshTrigger.ASK_REDIRECT,
                        ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS,
                        ClusterTopologyRefreshOptions.RefreshTrigger.UNKNOWN_NODE,
                        ClusterTopologyRefreshOptions.RefreshTrigger.UNCOVERED_SLOT)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30L))
                .refreshTriggersReconnectAttempts(5)
                .build();
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(redisPoolConfig.getMaxWaitMillis()))
                .keepAlive(true)
                .tcpNoDelay(true)
                .build();
        return ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .socketOptions(socketOptions)
                .timeoutOptions(TimeoutOptions.enabled(Duration.ofMillis(redisPoolConfig.getCommandTimeout())))
                .validateClusterNodeMembership(false)
                .build();
    }

    /**
     * 获取redisTemplate连接
     * @param connectionFactory
     * @param serializer
     * @return
     */
    protected RedisTemplate getRedisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer serializer) {
        val stringSerializer = new StringRedisSerializer();
        val redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 获取redis连接池配置
     * @param maxWaitMillis
     * @return
     */
    protected GenericObjectPoolConfig getPoolConfig(int maxTotal, int maxIdle, int minIdle, int maxWaitMillis) {

        val poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        // 空闲资源的检测周期(单位为毫秒)
        poolConfig.setTimeBetweenEvictionRunsMillis(30 * 1000);
        // 每波最多检查200个连接
//        poolConfig.setNumTestsPerEvictionRun(200);
        // 资源池中资源最小空闲时间
        poolConfig.setMinEvictableIdleTimeMillis(60 * 1000);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTestOnCreate(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

}
