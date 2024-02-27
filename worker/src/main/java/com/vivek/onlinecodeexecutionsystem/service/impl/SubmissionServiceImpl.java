package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.SubmissionDao;
import com.vivek.onlinecodeexecutionsystem.model.Status;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionDao submissionDao;
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${stream.key}")
    private String streamKey;

    @Value("${stream.key.group-name}")
    private String streamGroupName;

    @Autowired
    public SubmissionServiceImpl(SubmissionDao submissionDao, StringRedisTemplate stringRedisTemplate) {
        this.submissionDao = submissionDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @Transactional
    public void executeSubmission(StringRecord record) {
        long submissionId = Long.parseLong(record.getValue().get("submissionId"));
        logger.info("Executing submissionId:{}", submissionId);
        Optional<Submission> optionalSubmission = submissionDao.findById(submissionId);
        if (optionalSubmission.isEmpty()) {
            //TODO: Remove from queue and log since it does not exists in database
            return;
        }
        Submission submission = optionalSubmission.get();
        submission.setStatus(Status.PICKED);
        submission = submissionDao.save(submission);
        logger.info("Updating database status for submissionId:{}", submissionId);
        logger.info("Creating Sandbox");
        try {
            createSandbox(submission);
            destroySandbox(submission);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        submission.setStatus(Status.EXECUTING);
        submission = submissionDao.save(submission);
        stringRedisTemplate.opsForStream().acknowledge(streamKey, streamGroupName, record.getId());
        logger.info("Acknowledge redis id:{} and value:{}", record.getId(), record.getValue());
    }

    private void createSandbox(Submission submission) throws IOException {
        long boxId = submission.getId();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("isolate", "--b", String.valueOf(boxId), "--init");
        logger.info("Executing isolate command:{}", processBuilder.command());
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        String output = new String(inputStream.readAllBytes());
        logger.info("output:{}", output);
    }

    private void destroySandbox(Submission submission) throws IOException, InterruptedException {
        long boxId = submission.getId();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("isolate", "--b", String.valueOf(boxId), "--cleanup");
        logger.info("Executing isolate command:{}", processBuilder.command());
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        String output = new String(inputStream.readAllBytes());
        boolean exitSuccess = process.waitFor(30, TimeUnit.SECONDS);
        if (!exitSuccess)
            logger.error("Unable to cleanup sandbox: {}", boxId);
        logger.info("output:{}", output);

    }
}
