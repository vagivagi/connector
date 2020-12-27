package com.vagivagi.connector.toggl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vagivagi.connector.common.ThirdResponseBody;

public class TogglTimeEntryResponseBody implements ThirdResponseBody {

    @JsonProperty("data")
    private TogglTimeEntry data;

    public TogglTimeEntry getData() {
        return data;
    }

    public void setData(TogglTimeEntry data) {
        this.data = data;
    }

}
