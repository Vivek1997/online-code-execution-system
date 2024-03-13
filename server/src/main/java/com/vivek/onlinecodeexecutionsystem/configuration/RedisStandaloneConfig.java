package com.vivek.onlinecodeexecutionsystem.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisStandaloneConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisStandaloneConfig.class);
    RedisProperties redisProperties;

    @Autowired
    public RedisStandaloneConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        LOGGER.info("Setting up redis connection with host:{} and port:{}", redisProperties.getHost(), redisProperties.getPort());
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort()));
    }


    @Bean
    StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

}
