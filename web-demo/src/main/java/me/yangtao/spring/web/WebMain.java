package me.yangtao.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"me.yangtao.spring.web"},
        exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Slf4j
public class WebMain {

    public static void main(String... args) {
        try {
            SpringApplication.run(WebMain.class, args);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            throw throwable;
        }
    }

}
