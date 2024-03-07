package com.vivek.onlinecodeexecutionsystem.controller;

import com.vivek.onlinecodeexecutionsystem.dto.SubmissionDTO;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import com.vivek.onlinecodeexecutionsystem.utils.SubmissionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class SubmissionController {
    private final Logger logger = LoggerFactory.getLogger(SubmissionController.class);
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }


    private final String submitRequestDoc = """
            Creates a submission and adds it Db and redis stream which is picked by workers \n
            Mandatory field: sourceCode and languageId rest of the fields can be left empty and will be replaced by defaults \n
            Fields: \n
            sourceCode*: string \n
            langaugeId*: int (Can be retrieved using languages API) \n
            expectedOutput: string (Code can be tested against data) \n
            stdIn: string (standard input to code) \n
            cpuTimeLimit: float (Limit run time of the program to time seconds) Default: 30 \n
            cpuExtraTimeLimit: float (When the time limit is exceeded, do not kill the program immediately, but wait until extra time seconds elapse since the start of the program) Default: 20 \n
            wallTimeLimit: float (Limit wall-clock time to time seconds. This clock measures the time from the start of the program to its exit) Default: 30 \n
            memoryLimit: int (Limit address space of the program to size kilobytes) Default: 512000 \n
            stackLimit: int (Limit process stack to size kilobytes) Default: 128000 \n
            maxProcessesAndOrThreadsLimit: int (Permit the program to create up to max processes and/or threads) Default: 120 \n
            maxFileSizeLimit: int (Limit size of each file created (or modified) by the program to size kilobytes) Default: 4096       
            """;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = submitRequestDoc, summary = "Creates submission and adds it to DB")
    public ResponseEntity<Long> submit(@RequestBody @Parameter(description = "") SubmissionDTO submissionDTO) {
        SubmissionUtil.validateAndConfigureSubmission(submissionDTO);
        long id = submissionService.submit(submissionDTO);
        return ResponseEntity.ok(id);
    }

    private final String submitResponseDoc = """
            Gives the status of submission \n
            Fields: \n
            sourceCode*: string (Submitted code)\n
            langaugeId*: int (Submitted language ID) \n
            expectedOutput: string (Code tested against data) \n
            stdIn: string (standard input to code) \n
            cpuTimeLimit: float (Limit run time of the program to time seconds) Default: 30 \n
            cpuExtraTimeLimit: float (When the time limit is exceeded, do not kill the program immediately, but wait until extra time seconds elapse since the start of the program) Default: 20 \n
            wallTimeLimit: float (Limit wall-clock time to time seconds. This clock measures the time from the start of the program to its exit) Default: 30 \n
            memoryLimit: int (Limit address space of the program to size kilobytes) Default: 512000 \n
            stackLimit: int (Limit process stack to size kilobytes) Default: 128000 \n
            maxProcessesAndOrThreadsLimit: int (Permit the program to create up to max processes and/or threads) Default: 120 \n
            maxFileSizeLimit: int (Limit size of each file created (or modified) by the program to size kilobytes) Default: 4096 \n
            exitCode: int (The program has exited normally with this exit code) \n
            exitSignal: int (The program has exited after receiving this fatal signal) \n
            message: string (Status message E.g., "Time limit exceeded.") \n
            wallTime: float (Wall clock time of the program in fractional seconds) \n
            time: float (Run time of the program in fractional seconds) \n
            executionHost: string (Machine on which code is executed)
            memory: int (This is the total memory use) \n
            """;

    @GetMapping(value = "{submissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = submitResponseDoc, summary = "Status of execution")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable Long submissionId) {
        Submission submission = submissionService.getSubmission(submissionId);
        SubmissionDTO submissionDTO = submissionService.convertSubmissionEntityToDTO(submission);
        logger.info("Request:{}", submissionDTO);
        return ResponseEntity.ok(submissionDTO);
    }
}
