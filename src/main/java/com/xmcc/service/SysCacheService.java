package com.xmcc.service;

import com.xmcc.beans.CachePrefix;
import com.xmcc.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

@Service
@Slf4j
public class SysCacheService {
    @Resource
    private SysRedisPool sysRedisPool;


    //写缓存
    //一般我们有key/value就可以写缓存，但是实际开发中我们要规定好  存的是哪个版块的内容我们一般自定义一个前缀来拼接
    public void saveCache(String toSaveCacheValue, int timeOutSeconds, String key, CachePrefix prefix) {
        if (StringUtils.isBlank(toSaveCacheValue)) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = getCachKey(key, prefix);
            shardedJedis = sysRedisPool.getJedis();
            shardedJedis.setex(cacheKey, timeOutSeconds, toSaveCacheValue);
        } catch (Exception e) {
            log.error("sava Cach ERROR prifix{},key{}", prefix.name(), key);
        } finally {
            shardedJedis.close();
        }
    }
    public String getCachKey(String key, CachePrefix prefix) {
        if (StringUtils.isBlank(key)) {
            throw new ParamException("写入缓存的key值不能为空");
        }
        return prefix + "_" + key;
    }

    //读缓存
    public String getInfoFromCach(String key, CachePrefix prefix) {
        String value = "";
        String cacheKey = getCachKey(key, prefix);
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = sysRedisPool.getJedis();
            value = shardedJedis.get(cacheKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get Cach ERROR prifix{},key{}", prefix.name(), key);
        } finally {
            shardedJedis.close();
        }
        return value;
    }

}
