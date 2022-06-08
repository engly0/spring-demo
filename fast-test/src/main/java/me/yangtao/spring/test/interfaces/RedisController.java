package me.yangtao.spring.test.interfaces;

import lombok.extern.slf4j.Slf4j;
import me.yangtao.spring.demo.api.request.OperatorRequest;
import me.yangtao.spring.demo.common.Result;
import me.yangtao.spring.test.application.service.RedisLuaTestAppService;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import me.yangtao.spring.test.infra.database.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private RedisLuaTestAppService luaTestAppService;

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

    @GetMapping("/testLuaSlotOnCluster")
    public Result<Long> testLuaSlotOnCluster() {
        Long ret = luaTestAppService.testLuaSlotOnCluster();
        return Result.success("testLuaSlotOnCluster", ret);
    }

    @GetMapping("/splitKeys")
    public Result<Object> splitKeys() {
        Object ret = luaTestAppService.testSplitKeys();
        return Result.success("clusterSlots", ret);
    }

    @GetMapping("/testPipelineLuaOnCluster")
    public Result<Object> testPipelineLuaOnCluster() {
        luaTestAppService.testPipelineLuaOnCluster();
        return Result.success("testPipelineLuaOnCluster", "done");
    }

    @GetMapping("/getStoredValues")
    public Result<Object> getStoredValues() {
        Map<String,String> storedValues = luaTestAppService.getStoredValues();
        return Result.success("getStoredValues", storedValues);
    }

}
