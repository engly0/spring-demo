package me.yangtao.spring.test.interfaces;

import lombok.extern.slf4j.Slf4j;
import me.yangtao.spring.demo.api.request.OperatorRequest;
import me.yangtao.spring.demo.common.Result;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import me.yangtao.spring.test.infra.database.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisDao redisDao;

    @Resource
    private RedisPoolConfig redisPoolConfig;

    @RequestMapping("/selectConfig")
    public Result<RedisPoolConfig> selectConfig(OperatorRequest request) {
        log.info("test.selectConfig:" + request);
        Result<RedisPoolConfig> result = Result.success(redisPoolConfig);
        log.debug("test.result:" + result);
        return result;
    }

    @GetMapping("/testSet")
    public Result<String> testSet(@RequestParam String key, @RequestParam String val) {
        redisDao.set(key, val, 3, TimeUnit.DAYS);
        return Result.success("testSet", "done");
    }

    @GetMapping("/testGet")
    public Result<String> testGet(@RequestParam String key) {
        String val = redisDao.get(key);
        return Result.success("testGet", val);
    }


}
