package com.vivek.onlinecodeexecutionsystem.utils;

import com.vivek.onlinecodeexecutionsystem.dto.SubmissionDTO;
import com.vivek.onlinecodeexecutionsystem.exceptions.InvalidSubmissionException;
import com.vivek.onlinecodeexecutionsystem.exceptions.NoSuchLanguageException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vivek.onlinecodeexecutionsystem.constants.ExecutionConfig.*;

public class SubmissionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionUtil.class);

    public static void validateAndConfigureSubmission(SubmissionDTO submissionDTO) throws InvalidSubmissionException, NoSuchLanguageException {
        if (StringUtils.isBlank(submissionDTO.getSourceCode()))
            throw new InvalidSubmissionException("Source code cannot be null");

        if (submissionDTO.getLanguageId() == null)
            throw new NoSuchLanguageException("No language id provided");

        Float cpuTimeLimit = submissionDTO.getCpuTimeLimit();
        if (cpuTimeLimit == null) {
            LOGGER.info("Setting CPU time limit to max:{}", MAX_CPU_TIME_LIMIT);
            cpuTimeLimit = MAX_CPU_TIME_LIMIT;
            submissionDTO.setCpuTimeLimit(MAX_CPU_TIME_LIMIT);
        } else if (cpuTimeLimit > MAX_CPU_TIME_LIMIT || cpuTimeLimit <= 0) {
            throw new InvalidSubmissionException(
                    String.format("CPU time limit %f is more than allowed time limit of %f or less than equal to 0", cpuTimeLimit, MAX_CPU_TIME_LIMIT));
        }

        Float cpuExtraTimeLimit = submissionDTO.getCpuExtraTimeLimit();
        if (cpuExtraTimeLimit == null) {
            LOGGER.info("Setting CPU extra time limit to max:{}", MAX_CPU_EXTRA_TIME_LIMIT);
            cpuExtraTimeLimit = MAX_CPU_EXTRA_TIME_LIMIT;
            submissionDTO.setCpuExtraTimeLimit(MAX_CPU_EXTRA_TIME_LIMIT);
        } else if (cpuExtraTimeLimit > MAX_CPU_EXTRA_TIME_LIMIT) {
            throw new InvalidSubmissionException(
                    String.format("CPU extra time limit %f is more than allowed time limit of %f", cpuExtraTimeLimit, MAX_CPU_EXTRA_TIME_LIMIT));
        }
        Float wallTimeLimit = submissionDTO.getWallTimeLimit();
        if (wallTimeLimit == null) {
            LOGGER.info("Setting wall time limit to max:{}", MAX_WALL_TIME_LIMIT);
            submissionDTO.setWallTimeLimit(MAX_WALL_TIME_LIMIT);
        } else if (wallTimeLimit > MAX_WALL_TIME_LIMIT ||
                (cpuTimeLimit + cpuExtraTimeLimit) > wallTimeLimit ||
                (cpuTimeLimit + cpuExtraTimeLimit) > MAX_WALL_TIME_LIMIT)
            throw new InvalidSubmissionException(
                    String.format("Wall time limit %f is more than allowed wall time limit of %f " +
                            "or sum of cpu time and cpu extra time limit is more than allowed max wall time limit", wallTimeLimit, MAX_WALL_TIME_LIMIT));

        Integer memoryLimit = submissionDTO.getMemoryLimit();
        if (memoryLimit == null) {
            LOGGER.info("Setting memory limit to max:{}", MAX_MEMORY_LIMIT);
            submissionDTO.setMemoryLimit(MAX_MEMORY_LIMIT);
        } else if (memoryLimit > MAX_MEMORY_LIMIT)
            throw new InvalidSubmissionException(String.format("Memory limit %d is more than allowed value of %d", memoryLimit, MAX_MEMORY_LIMIT));

        Integer stackLimit = submissionDTO.getStackLimit();
        if (stackLimit == null) {
            LOGGER.info("Setting stack limit to max:{}", MAX_STACK_LIMIT);
            submissionDTO.setStackLimit(MAX_STACK_LIMIT);
        } else if (stackLimit > MAX_STACK_LIMIT)
            throw new InvalidSubmissionException(String.format("Stack limit %d is more than allowed value of %d", stackLimit, MAX_STACK_LIMIT));

        Integer maxProcessesAndOrThreadsLimit = submissionDTO.getMaxProcessesAndOrThreadsLimit();
        if (maxProcessesAndOrThreadsLimit == null) {
            LOGGER.info("Setting max processes and threads limit to max:{}", MAX_MAX_PROCESSES_AND_OR_THREADS);
            submissionDTO.setMaxProcessesAndOrThreadsLimit(MAX_MAX_PROCESSES_AND_OR_THREADS);
        } else if (maxProcessesAndOrThreadsLimit > MAX_MAX_PROCESSES_AND_OR_THREADS)
            throw new InvalidSubmissionException(
                    String.format("Max processes or threads limit is set %d more than allowed value of %d",
                            maxProcessesAndOrThreadsLimit, MAX_MAX_PROCESSES_AND_OR_THREADS));
        Integer maxFileSizeLimit = submissionDTO.getMaxFileSizeLimit();
        if (maxFileSizeLimit == null) {
            LOGGER.info("Setting max file size limit to max:{}", MAX_FILE_SIZE);
            submissionDTO.setMaxFileSizeLimit(MAX_FILE_SIZE);
        } else if (maxFileSizeLimit > MAX_FILE_SIZE)
            throw new InvalidSubmissionException(String.format("File size limit is set %d more than allowed value of %d", maxFileSizeLimit, MAX_FILE_SIZE));

    }
}
