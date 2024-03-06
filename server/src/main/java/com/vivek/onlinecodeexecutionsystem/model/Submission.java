package com.vivek.onlinecodeexecutionsystem.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "finished_at", insertable = false, updatable = false)
    private Timestamp finishedAt;
    @Column(name = "source_code")
    private String sourceCode;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "expected_output")
    private String expectedOutput;
    @Column(name = "std_out")
    private String stdOut;
    @Column(name = "status")
    private Status status;

    @Column(name = "compile_output")
    private String compileOutput;

    @Column(name = "std_in")
    private String stdIn;

    @Column(name = "std_err")
    private String stdErr;

    @Column(name = "cpu_time_limit")
    private Float cpuTimeLimit;

    @Column(name = "cpu_extra_time_limit")
    private Float cpuExtraTimeLimit;

    @Column(name = "wall_time_limit")
    private Float wallTimeLimit;

    @Column(name = "memory_limit")
    private Integer memoryLimit;

    @Column(name = "stack_limit")
    private Integer stackLimit;

    @Column(name = "max_process_and_or_thread_limit")
    private Integer maxProcessesAndOrThreadsLimit;

    @Column(name = "max_file_size_limit")
    private Integer maxFileSizeLimit;

    @Column(name = "exit_code")
    private Integer exitCode;

    @Column(name = "exit_signal")
    private Integer exitSignal;

    @Column(name = "message")
    private String message;

    @Column(name = "wall_time")
    private Float wallTime;

    @Column(name = "time")
    private Float time;

    @Column(name = "memory")
    private Integer memory;

    @Column(name = "execution_host")
    private String executionHost;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
