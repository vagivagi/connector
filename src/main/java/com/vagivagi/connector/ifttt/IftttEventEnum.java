package com.vagivagi.connector.ifttt;

public enum IftttEventEnum {
    LIGHT_0N("light_on", "light on by switchbot"),
    STUDY_REPORT("study_report", "English Study Report Notification");

    IftttEventEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private final String name;
    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
