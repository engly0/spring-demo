package me.yangtao.spring.test.application.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.yangtao.spring.test.infra.database.RedisDao;
import me.yangtao.spring.test.infra.database.RedisScriptConstant;
import me.yangtao.spring.test.infra.util.ScriptParamsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisLuaTestAppService {

    @Autowired
    private RedisDao redisDao;

    private ScriptParamsHelper getTestParams(int size) {
        ScriptParamsHelper scriptParamsHelper = new ScriptParamsHelper();
        for (int i = 1; i <= size; i++) {
            String key = "test_slot_" + i;
            String val = String.valueOf(key.hashCode());
            scriptParamsHelper.addKeyVal(key, val);
        }
        return scriptParamsHelper;
    }

    public Long testLuaSlotOnCluster() {
        ScriptParamsHelper helper = getTestParams(100);
        Map<RedisClusterNode, List<String>> splitKeys = redisDao.splitKeys(helper.getAllKeys());
        for(RedisClusterNode node: splitKeys.keySet()){
            List<String> keys = splitKeys.get(node);
            String[] vals = helper.getValuesByKeys(keys);
            log.info("call REDIS_TEST_LUA_SCRIPT,keys size:" + keys.size());
            Long result = redisDao.executeScriptOnSingleNode(node, RedisScriptConstant.REDIS_TEST_LUA_SCRIPT, keys, vals);
            log.info("REDIS_TEST_LUA_SCRIPT:ret={},keys={}", result, keys);
        }
        return 1L;
    }

    public Map<RedisClusterNode, List<String>> testSplitKeys(){
        ScriptParamsHelper helper = getTestParams(100);
        return redisDao.splitKeys(helper.getAllKeys());
    }

    public void testPipelineLuaOnCluster(){
        ScriptParamsHelper helper = getTestParams(20);
        RedisScript<Long> script = RedisScriptConstant.REDIS_TEST_LUA_SCRIPT;
        StringRedisSerializer redisSerializer = redisDao.getStringRedisSerializer();
        redisDao.executePipelined((RedisCallback<List<String>>) connection -> {
            for (String key : helper.getAllKeys()) {
                ReturnType returnType = ReturnType.fromJavaType(script.getResultType());
                byte[] scriptBytes = redisSerializer.serialize(script.getScriptAsString());
                byte[] keyBytes = redisSerializer.serialize(key);
                byte[] valBytes = redisSerializer.serialize(helper.getValue(key));
                connection.eval(scriptBytes, returnType, 1, keyBytes, valBytes);
            }
            return null;
        });
    }

    public Map<String,String> getStoredValues(){
        ScriptParamsHelper helper = getTestParams(20);
        Collection<String> allKeys = helper.getAllKeys();
        List<String> values = redisDao.multiGet(allKeys);
        Map<String,String> kvMap = Maps.newHashMap();
        int idx = 0;
        for(String key: allKeys){
            kvMap.put(key, values.get(idx++));
        }
        return kvMap;
    }
}
