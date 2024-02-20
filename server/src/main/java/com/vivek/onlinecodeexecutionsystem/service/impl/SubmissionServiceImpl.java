package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.SubmissionDao;
import com.vivek.onlinecodeexecutionsystem.dto.SubmissionDTO;
import com.vivek.onlinecodeexecutionsystem.exceptions.InvalidSubmissionException;
import com.vivek.onlinecodeexecutionsystem.model.Language;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.LanguageService;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionDao submissionDao;
    private final LanguageService languageService;
    private final ModelMapper modelMapper;

    @Autowired
    public SubmissionServiceImpl(SubmissionDao submissionDao, LanguageService languageService, ModelMapper modelMapper) {
        this.submissionDao = submissionDao;
        this.languageService = languageService;
        this.modelMapper = modelMapper;
    }


    //TODO: Add throw
    @Override
    public Submission getSubmission(Long submissionId) throws InvalidSubmissionException {
        return submissionDao.findById(submissionId).orElseThrow(() -> new InvalidSubmissionException("Submission does not exists with id:" + submissionId));
    }

    @Override
    @Transactional
    public long submit(SubmissionDTO submissionDTO) {
        Submission submission = convertToSubmissionEntity(submissionDTO);
        submission = submissionDao.save(submission);
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
