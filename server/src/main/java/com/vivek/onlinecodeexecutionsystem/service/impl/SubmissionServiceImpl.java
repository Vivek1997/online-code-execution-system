package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.SubmissionDao;
import com.vivek.onlinecodeexecutionsystem.dto.SubmissionDTO;
import com.vivek.onlinecodeexecutionsystem.exceptions.InvalidSubmissionException;
import com.vivek.onlinecodeexecutionsystem.model.Language;
import com.vivek.onlinecodeexecutionsystem.model.Status;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.LanguageService;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SubmissionServiceImpl implements SubmissionService {

    private final Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionDao submissionDao;
    private final LanguageService languageService;
    private final ModelMapper modelMapper;

    private final StringRedisTemplate stringRedisTemplate;


    @Autowired
    public SubmissionServiceImpl(SubmissionDao submissionDao, LanguageService languageService, ModelMapper modelMapper, StringRedisTemplate stringRedisTemplate) {
        this.submissionDao = submissionDao;
        this.languageService = languageService;
        this.modelMapper = modelMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Submission getSubmission(Long submissionId) throws InvalidSubmissionException {
        return submissionDao.findById(submissionId).orElseThrow(() -> new InvalidSubmissionException("Submission does not exists with id:" + submissionId));
    }

    @Override
    @Transactional
    public long submit(SubmissionDTO submissionDTO) {
        Submission submission = convertToSubmissionEntity(submissionDTO);
        submission.setStatus(Status.QUEUED);
        submission = submissionDao.save(submission);
        Map<String, String> map = new HashMap<>();
        map.put("submissionId", String.valueOf(submission.getId()));
        logger.info("Created submission:{} in db writing to redis", submission.getId());
        StringRecord stringRecord = StreamRecords.newRecord().ofStrings(map).withStreamKey("submission");
        RecordId recordId = stringRedisTemplate.opsForStream().add(stringRecord);
        return submission.getId();
    }

    @Override
    public Submission convertToSubmissionEntity(SubmissionDTO submissionDTO) {
        Submission submission = modelMapper.map(submissionDTO, Submission.class);
        Language language = languageService.getLanguage(submissionDTO.getLanguageId());
        submission.setLanguage(language);
        return submission;
    }

    @Override
    public SubmissionDTO convertSubmissionEntityToDTO(Submission submission) {
        return modelMapper.map(submission, SubmissionDTO.class);
    }

}
