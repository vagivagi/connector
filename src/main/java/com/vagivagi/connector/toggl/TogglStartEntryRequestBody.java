package com.vagivagi.connector.toggl;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TogglStartEntryRequestBody {

    public TogglStartEntryRequestBody(TimeEntry timeEntry) {
        this.timeEntry = timeEntry;
    }

    @JsonProperty("time_entry")
    private TimeEntry timeEntry;

    public static TogglStartEntryRequestBodyBuilder builder() {
        return new TogglStartEntryRequestBodyBuilder();
    }

    public TimeEntry getTimeEntry() {
        return timeEntry;
    }

    static class TimeEntry {
        public TimeEntry(String description, List<String> tags, int pid, String createdWith) {
            this.description = description;
            this.tags = tags;
            this.pid = pid;
            this.createdWith = createdWith;
        }

        private String description;
        private List<String> tags;
        private int pid;
        @JsonProperty("created_with")
        private String createdWith;

        public String getDescription() {
            return description;
        }

        public List<String> getTags() {
            return tags;
        }

        public int getPid() {
            return pid;
        }

        public String getCreatedWith() {
            return createdWith;
        }
    }

    public static class TogglStartEntryRequestBodyBuilder {
        private String description;
        private List<String> tags;
        private int pid;
        private String createdWith;

        public TogglStartEntryRequestBody build() {
            return new TogglStartEntryRequestBody(new TimeEntry(this.description, this.tags, this.pid, this.createdWith));
        }

        public TogglStartEntryRequestBodyBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TogglStartEntryRequestBodyBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public TogglStartEntryRequestBodyBuilder pid(int pid) {
            this.pid = pid;
            return this;
        }

        public TogglStartEntryRequestBodyBuilder createdWith(String createdWith) {
            this.createdWith = createdWith;
            return this;
        }
    }
}
