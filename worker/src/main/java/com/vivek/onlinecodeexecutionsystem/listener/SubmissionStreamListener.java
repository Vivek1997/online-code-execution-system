package com.vivek.onlinecodeexecutionsystem.listener;

import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;


public class SubmissionStreamListener implements StreamListener<String, MapRecord<String, String, String>> {
    private final Logger logger = LoggerFactory.getLogger(SubmissionStreamListener.class);

    private final StringRedisTemplate stringRedisTemplate;

    private final String streamKey;
    private final String groupName;

    private final ApplicationContext applicationContext;

    private final SubmissionService submissionService;


    public SubmissionStreamListener(ApplicationContext applicationContext, StringRedisTemplate stringRedisTemplate, String streamKey, String groupName) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.streamKey = streamKey;
        this.groupName = groupName;
        this.applicationContext = applicationContext;
        this.submissionService = applicationContext.getBean(SubmissionService.class);
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message)     {
        StringRecord stringRecord = StringRecord.of(message);
        RecordId recordId = stringRecord.getId();
        logger.info("Received message with redis id:{}, key:{}, value:{}", message.getId(), message.getStream(), message.getValue());
        submissionService.executeSubmission(stringRecord);
    }

}
