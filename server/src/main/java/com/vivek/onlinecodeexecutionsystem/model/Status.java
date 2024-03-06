package com.vivek.onlinecodeexecutionsystem.model;

public enum Status {
    QUEUED(1, "In Queue"), PROCESS(2, "Processing"), AC(3, "Accepted"), WA(4, "Wrong answer"),
    TLE(5, "Time limit exceeded"), CE(6, "Compilation error"), SIGSEGV(7, "Runtime Error (SIGSEGV)"),
    SIGXFSZ(8, "Runtime Error (SIGXFSZ)"), SIGFPE(9, "Runtime Error (SIGFPE)"),
    SIGABRT(10, "Runtime Error (SIGABRT)"), NZEC(11, "Runtime Error (NZEC)"), OTHER(12, "Runtime Error (Other)"),
    BOXERR(13, "Internal Error"), EXEERR(14, "Exec Format Error");
    private final int code;

    private final String name;

    Status(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Status{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public static Status findRuntimeErrorByStatusCode(Integer exitSignal) {
        if (exitSignal == null)
            return Status.OTHER;
        Status status = switch (exitSignal) {
            case 11 -> Status.SIGSEGV;
            case 25 -> Status.SIGXFSZ;
            case 8 -> Status.SIGFPE;
            case 6 -> Status.SIGABRT;
            default -> Status.OTHER;
        };
        return status;
    }
}
