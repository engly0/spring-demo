package me.yangtao.spring.test.infra.config;

import lombok.Data;

@Data
public class RedisClusterNode {
    private String host;
    private int port;
}
