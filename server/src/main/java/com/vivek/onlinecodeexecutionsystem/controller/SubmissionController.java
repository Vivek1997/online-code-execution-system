package com.vivek.onlinecodeexecutionsystem.controller;

import com.vivek.onlinecodeexecutionsystem.dto.SubmissionDTO;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class SubmissionController {

    private final SubmissionService submissionService;


    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/")
    public ResponseEntity<Long> submit(@RequestBody SubmissionDTO submissionDTO) {
        long id = submissionService.submit(submissionDTO);
        return ResponseEntity.ok(id);
    }

    @GetMapping("{submissionId}")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable Long submissionId) {
        Submission submission = submissionService.getSubmission(submissionId);
        SubmissionDTO submissionDTO = submissionService.convertSubmissionEntityToDTO(submission);
        return ResponseEntity.ok(submissionDTO);
    }
}
