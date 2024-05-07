package com.lezhin.clone.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getData(String schema, String key){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(schema + ":" + key);
    }

    public void setData(String schema, String key, String value){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(schema + ":" + key,value);
    }

    public void setDataList(String schema, String key, List<String> value){
        ListOperations<String,String> listOperations = stringRedisTemplate.opsForList();
        for(int i = 0; i < value.size(); i++) {
            listOperations.rightPush(schema + ":" + key, value.get(i));
        }
    }

    public List<String> getDataList(String schema, String key){
        ListOperations<String,String> listOperations = stringRedisTemplate.opsForList();
        return listOperations.range(schema + ":" + key, 0l, listOperations.size(schema + ":" + key));
    }

    public void setDataExpire(String schema, String key, String value, long duration){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(schema + ":" + key, value, expireDuration);
    }

    public void deleteData(String schema, String key){
        stringRedisTemplate.delete(schema + ":" + key);
    }

}