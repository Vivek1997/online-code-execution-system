package com.vivek.onlinecodeexecutionsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

@JsonIgnoreProperties(value = {"id", "output", "status"}, allowGetters = true, ignoreUnknown = true)
public class SubmissionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp timeStamp;
    private String sourceCode;
    private int languageId;
    private String input;
    private String expectedOutput;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String output;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    public SubmissionDTO() {
    }

    public SubmissionDTO(long id, String sourceCode, int languageId, String input, String expectedOutput) {
        this.id = id;
        this.sourceCode = sourceCode;
        this.languageId = languageId;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public SubmissionDTO(String sourceCode, int languageId, String input, String expectedOutput) {
        this.sourceCode = sourceCode;
        this.languageId = languageId;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubmissionDTO{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", sourceCode='" + sourceCode + '\'' +
                ", languageId=" + languageId +
                ", input='" + input + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                ", output='" + output + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
