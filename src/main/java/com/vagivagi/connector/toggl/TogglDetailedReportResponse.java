package com.vagivagi.connector.toggl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vagivagi.connector.common.ThirdResponseBody;

public class TogglDetailedReportResponse implements ThirdResponseBody {
    @JsonProperty("total_grand")
    private long totalGrand;
    @JsonProperty("total_count")
    private long totalCount;

    public long getTotalGrand() {
        return totalGrand;
    }

    public void setTotalGrand(int totalGrand) {
        this.totalGrand = totalGrand;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
