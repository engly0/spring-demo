package me.yangtao.spring.test.ju;

import lombok.extern.slf4j.Slf4j;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest()
@ActiveProfiles("dev")
public class ConfigTest {
    @Resource
    private RedisPoolConfig redisPoolConfig;

    @Test
    public void testInitConfigFromYaml() {
        System.out.println("redisPoolConfig:" + redisPoolConfig);
    }
}
