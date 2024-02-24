package com.vivek.onlinecodeexecutionsystem.configuration;

import com.vivek.onlinecodeexecutionsystem.listener.SubmissionStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.data.redis.stream.Subscription;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class RedisSubmissionStreamConfig {

    private final Logger logger = LoggerFactory.getLogger(RedisSubmissionStreamConfig.class);

    @Value("${stream.key}")
    private String streamKey;

    @Value("${stream.key.group-name}")
    private String streamGroupName;

    @Bean
    public Subscription subscription(RedisConnectionFactory redisConnectionFactory, StringRedisTemplate stringRedisTemplate) throws UnknownHostException {
        createConsumerGroupIfNotExists(stringRedisTemplate, streamKey, streamGroupName);
        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options
                = StreamMessageListenerContainerOptions.builder().pollTimeout(Duration.ofMillis(100)).build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container
                = StreamMessageListenerContainer.create(redisConnectionFactory, options);


        StreamOffset<String> streamOffset = StreamOffset.create(streamKey, ReadOffset.lastConsumed());

        Subscription subscription = container.receive(
                Consumer.from(streamGroupName, InetAddress.getLocalHost().getHostName()),
                streamOffset, new SubmissionStreamListener(stringRedisTemplate, streamKey, streamGroupName));
        container.start();
        return subscription;
    }

    private void createConsumerGroupIfNotExists(StringRedisTemplate stringRedisTemplate, String streamKey, String groupName) {
        try {
            stringRedisTemplate.opsForStream().createGroup(streamKey, ReadOffset.from("0-0"), groupName);
        } catch (Exception e) {
            logger.error("Error in creating consumer group:{}", e.getCause().getMessage());
        }
    }
}
