package me.yangtao.spring.test;

import lombok.extern.slf4j.Slf4j;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"me.yangtao.spring.test"},
        exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties(value = {RedisPoolConfig.class})
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Slf4j
public class Main {

    public static void main(String... args) {
        try {
            SpringApplication.run(Main.class, args);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            throw throwable;
        }
    }

}
