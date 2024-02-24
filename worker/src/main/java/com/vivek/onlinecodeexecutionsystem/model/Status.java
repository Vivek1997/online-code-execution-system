package com.vivek.onlinecodeexecutionsystem.model;

public enum Status {
    QUEUED(1), PICKED(2), EXECUTING(3), PASSED(4), FAILED(5), RUNTIME_EXCEPTION(6);
    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
