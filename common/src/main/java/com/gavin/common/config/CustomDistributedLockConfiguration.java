package com.gavin.common.config;

import com.gavin.common.lock.DistributedLockTemplate;
import com.gavin.common.lock.redis.RedisLockTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class CustomDistributedLockConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public DistributedLockTemplate distributedLockTemplate() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(-1);
        config.setTestOnBorrow(true);

        JedisPool jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort());
        return new RedisLockTemplate(jedisPool);
    }

}
