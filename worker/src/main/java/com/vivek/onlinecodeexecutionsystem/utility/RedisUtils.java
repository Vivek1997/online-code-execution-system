package com.vivek.onlinecodeexecutionsystem.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtils.class);

    public static void acknowledge(StringRedisTemplate redisTemplate, String streamKey, String streamGroupName, RecordId recordId) {
        redisTemplate.opsForStream().acknowledge(streamKey, streamGroupName, recordId);
        LOGGER.info("Acknowledged redis for stream key:{}, stream group name:{},record id:{}", streamKey, streamGroupName, recordId);
    }

    public static void deleteFromStream(StringRedisTemplate redisTemplate, String streamKey, RecordId recordId) {
        redisTemplate.opsForStream().delete(streamKey, recordId);
        LOGGER.info("Deleting from redis for stream key:{}, record id:{}", streamKey, recordId);
    }


}
