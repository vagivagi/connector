package com.vagivagi.connector.toggl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = TogglWrapperEntryRequestBuilder.class)
public class TogglWrapperEntryRequest {
    private String description;
    private int wid;
    private int pid;
    private String key;

    public TogglWrapperEntryRequest(String description, int wid, int pid, String key) {
        this.description = description;
        this.wid = wid;
        this.pid = pid;
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
