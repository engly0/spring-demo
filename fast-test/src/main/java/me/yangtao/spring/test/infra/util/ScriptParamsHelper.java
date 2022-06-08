package me.yangtao.spring.test.infra.util;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public class ScriptParamsHelper {
    private Map<String,String> keyValMap = Maps.newHashMap();

    public void addKeyVal(String key, String val){
        keyValMap.put(key, val);
    }

    public Collection<String> getAllKeys(){
        return keyValMap.keySet();
    }

    public String getValue(String key){
        return keyValMap.get(key);
    }

    public String[] getValuesByKeys(Collection<String> keys){
        if (CollectionUtil.isEmpty(keys)) {
            return new String[0];
        }
        String[] vals = new String[keys.size()];
        int idx = 0;
        for(String key: keys){
            vals[idx++] = keyValMap.get(key);
        }
        return vals;
    }
}
