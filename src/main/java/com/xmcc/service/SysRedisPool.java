package com.xmcc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

@Service
@Slf4j
public class SysRedisPool {
    @Resource(name="shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    //获取pool链接
    /**
     * 获取连接方法
     */
    public  ShardedJedis getJedis() {
        return shardedJedisPool.getResource();
    }
    /**
     * 关闭Jedis
     */
    public  void close(ShardedJedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
