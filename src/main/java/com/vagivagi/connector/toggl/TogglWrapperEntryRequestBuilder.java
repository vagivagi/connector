package com.vagivagi.connector.toggl;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public class TogglWrapperEntryRequestBuilder {
    private String description;
    private int wid;
    private int pid;
    private String key;

    public TogglWrapperEntryRequestBuilder() {
    }

    public TogglWrapperEntryRequestBuilder(TogglWrapperEntryRequest request) {
        this.description = request.getDescription().replaceAll(" ", "");
        this.wid = request.getWid();
        this.pid = request.getPid();
        this.key = request.getKey();
    }

    public TogglWrapperEntryRequest build() {
        return new TogglWrapperEntryRequest(description, wid, pid, key);
    }

    public TogglWrapperEntryRequestBuilder withDescription(String description) {
        this.description = description.replaceAll(" ", "");
        return this;
    }

    public TogglWrapperEntryRequestBuilder withWid(int wid) {
        this.wid = wid;
        return this;
    }

    public TogglWrapperEntryRequestBuilder withPid(int pid) {
        this.pid = pid;
        return this;
    }

    public TogglWrapperEntryRequestBuilder withKey(String key) {
        this.key = key;
        return this;
    }
}
