package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.SubmissionDao;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionDao submissionDao;

    @Autowired
    public SubmissionServiceImpl(SubmissionDao submissionDao) {
        this.submissionDao = submissionDao;
    }

    @Override
    public Submission getSubmission(Long submissionId) {

        return submissionDao.findById(submissionId).orElse(null);

    }

    @Override
    @Transactional
    public long submit(Submission submission) {
        submission = submissionDao.save(submission);
        return submission.getId();
    }
}
