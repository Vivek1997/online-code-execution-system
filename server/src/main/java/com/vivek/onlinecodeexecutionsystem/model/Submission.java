package com.vivek.onlinecodeexecutionsystem.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "timestamp", insertable = false, updatable = false)
    private Timestamp timeStamp;
    @Column(name = "source_code")
    private String sourceCode;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "input")
    private String input;
    @Column(name = "expected_output")
    private String expectedOutput;
    @Column(name = "output")
    private String output;
    @Column(name = "status")
    private Status status;


    public Submission() {
    }

    public Submission(String sourceCode, Language language, String input, String expectedOutput) {
        this.sourceCode = sourceCode;
        this.language = language;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", sourceCode='" + sourceCode + '\'' +
                ", language=" + language +
                ", input='" + input + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                ", output='" + output + '\'' +
                ", status=" + status +
                '}';
    }
}
