package com.vivek.onlinecodeexecutionsystem.service;

import com.vivek.onlinecodeexecutionsystem.model.Submission;

public interface SubmissionService {
    Submission getSubmission(Long submissionId);

    long submit(Submission submission);
}
