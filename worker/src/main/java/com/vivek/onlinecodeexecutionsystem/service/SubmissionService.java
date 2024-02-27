package com.vivek.onlinecodeexecutionsystem.service;

import org.springframework.data.redis.connection.stream.StringRecord;

import java.io.IOException;

public interface SubmissionService {
    void executeSubmission(StringRecord record);
}
