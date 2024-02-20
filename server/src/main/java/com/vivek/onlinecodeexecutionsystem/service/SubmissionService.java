package com.vivek.onlinecodeexecutionsystem.service;

import com.vivek.onlinecodeexecutionsystem.dto.SubmissionDTO;
import com.vivek.onlinecodeexecutionsystem.model.Submission;

public interface SubmissionService {
    Submission getSubmission(Long submissionId);

    long submit(SubmissionDTO submissionDTO);

    Submission convertToSubmissionEntity(SubmissionDTO submissionDTO);

    SubmissionDTO convertSubmissionEntityToDTO(Submission submission);
}
