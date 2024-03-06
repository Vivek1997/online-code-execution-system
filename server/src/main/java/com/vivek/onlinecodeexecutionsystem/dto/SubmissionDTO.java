package com.vivek.onlinecodeexecutionsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

@JsonIgnoreProperties(
        value = {"id", "createdAt", "finishedAt", "stdOut", "status", "compileOutput", "stdErr", "exitCode", "exitSignal", "message", "wallTime", "time", "executionHost", "memory"},
        allowGetters = true,
        ignoreUnknown = true
)
public class SubmissionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp finishedAt;
    private String sourceCode;
    private Integer languageId;
    private String expectedOutput;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String stdOut;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String compileOutput;
    private String stdIn;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String stdErr;

    private Float cpuTimeLimit;

    private Float cpuExtraTimeLimit;

    private Float wallTimeLimit;

    private Integer memoryLimit;

    private Integer stackLimit;

    private Integer maxProcessesAndOrThreadsLimit;

    private Integer maxFileSizeLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer exitCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer exitSignal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String message;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float wallTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float time;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String executionHost;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer memory;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Timestamp finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getStdOut() {
        return stdOut;
    }

    public void setStdOut(String stdOut) {
        this.stdOut = stdOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompileOutput() {
        return compileOutput;
    }

    public void setCompileOutput(String compileOutput) {
        this.compileOutput = compileOutput;
    }

    public String getStdIn() {
        return stdIn;
    }

    public void setStdIn(String stdIn) {
        this.stdIn = stdIn;
    }

    public String getStdErr() {
        return stdErr;
    }

    public void setStdErr(String stdErr) {
        this.stdErr = stdErr;
    }

    public Float getCpuTimeLimit() {
        return cpuTimeLimit;
    }

    public void setCpuTimeLimit(Float cpuTimeLimit) {
        this.cpuTimeLimit = cpuTimeLimit;
    }

    public Float getCpuExtraTimeLimit() {
        return cpuExtraTimeLimit;
    }

    public void setCpuExtraTimeLimit(Float cpuExtraTimeLimit) {
        this.cpuExtraTimeLimit = cpuExtraTimeLimit;
    }

    public Float getWallTimeLimit() {
        return wallTimeLimit;
    }

    public void setWallTimeLimit(Float wallTimeLimit) {
        this.wallTimeLimit = wallTimeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Integer getStackLimit() {
        return stackLimit;
    }

    public void setStackLimit(Integer stackLimit) {
        this.stackLimit = stackLimit;
    }

    public Integer getMaxProcessesAndOrThreadsLimit() {
        return maxProcessesAndOrThreadsLimit;
    }

    public void setMaxProcessesAndOrThreadsLimit(Integer maxProcessesAndOrThreadsLimit) {
        this.maxProcessesAndOrThreadsLimit = maxProcessesAndOrThreadsLimit;
    }

    public Integer getMaxFileSizeLimit() {
        return maxFileSizeLimit;
    }

    public void setMaxFileSizeLimit(Integer maxFileSizeLimit) {
        this.maxFileSizeLimit = maxFileSizeLimit;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public Integer getExitSignal() {
        return exitSignal;
    }

    public void setExitSignal(Integer exitSignal) {
        this.exitSignal = exitSignal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getWallTime() {
        return wallTime;
    }

    public void setWallTime(Float wallTime) {
        this.wallTime = wallTime;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public String getExecutionHost() {
        return executionHost;
    }

    public void setExecutionHost(String executionHost) {
        this.executionHost = executionHost;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }
}
