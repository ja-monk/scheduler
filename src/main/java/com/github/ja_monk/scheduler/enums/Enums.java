package com.github.ja_monk.scheduler.enums;

public class Enums {
    private Enums() {}  // private constructor to prevent instantiation of container class

    public enum JobStatus {
        WAITING,
        RUNNING,
        COMPLETE,
        FAILED
    }

    public enum Repeat {
        H,
        D,
        W,
        M,
        Y,
        N
    }

}

