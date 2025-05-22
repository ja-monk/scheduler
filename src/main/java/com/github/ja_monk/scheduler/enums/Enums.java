package com.github.ja_monk.scheduler.enums;

public class Enums {
    private Enums() {}  // private constructor to prevent instantiation of container class

    public enum JobStatus {
        WAITING,
        RUNNING,
        COMPLETE,
        FAILED,
        CANCELLED,
        HELD
    }

    // Hour, Day, Week, Month, Year, Time (supply time period to repeat in mins e.g. 15)
    public enum Repeat {
        H,
        D,
        W,
        M,
        Y,
        T,
        N
    }

}

