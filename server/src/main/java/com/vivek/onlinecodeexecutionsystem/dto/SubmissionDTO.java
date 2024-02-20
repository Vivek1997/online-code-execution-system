package com.vivek.onlinecodeexecutionsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "id", allowGetters = true)
public class SubmissionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String sourceCode;
    private int languageId;
    private String input;
    private String expectedOutput;

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

    @Override
    public String toString() {
        return "SubmissionDTO{" +
                "id=" + id +
                ", sourceCode='" + sourceCode + '\'' +
                ", languageId=" + languageId +
                ", input='" + input + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                '}';
    }
}
