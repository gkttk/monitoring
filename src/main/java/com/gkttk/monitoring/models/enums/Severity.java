package com.gkttk.monitoring.models.enums;

public enum Severity {

    INFO, WARNING, ERROR;


    public static Severity fromString(String text) {
        for (Severity severity : Severity.values()) {
            if (severity.name().equalsIgnoreCase(text)) {
                return severity;
            }
        }
        return null;
    }
}
