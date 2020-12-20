package com.vagivagi.connector.toggl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vagivagi.connector.common.ThirdResponseBody;

import java.time.OffsetDateTime;

public class TogglResponseBody implements ThirdResponseBody {

    @JsonProperty("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    static class Data {
        private int id;
        private int wid;
        private int pid;
        private boolean billable;
        private OffsetDateTime start;
        private int duration;
        private String description;
        private OffsetDateTime at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public boolean isBillable() {
            return billable;
        }

        public void setBillable(boolean billable) {
            this.billable = billable;
        }

        public OffsetDateTime getStart() {
            return start;
        }

        public void setStart(OffsetDateTime start) {
            this.start = start;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public OffsetDateTime getAt() {
            return at;
        }

        public void setAt(OffsetDateTime at) {
            this.at = at;
        }
    }
}
