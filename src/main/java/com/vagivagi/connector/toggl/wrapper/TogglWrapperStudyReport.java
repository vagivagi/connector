package com.vagivagi.connector.toggl.wrapper;

public class TogglWrapperStudyReport {
    private final String today;
    private final String yesterday;
    private final String monthly;

    public TogglWrapperStudyReport(String today, String yesterday, String monthly) {
        this.today = today;
        this.yesterday = yesterday;
        this.monthly = monthly;
    }

    public String getToday() {
        return today;
    }

    public String getYesterday() {
        return yesterday;
    }

    public String getMonthly() {
        return monthly;
    }
}
