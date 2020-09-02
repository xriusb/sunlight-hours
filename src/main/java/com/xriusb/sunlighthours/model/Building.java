package com.xriusb.sunlighthours.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Building {
    private String name;
    @JsonProperty("apartments_count")
    private Integer apartmentsCount;
    private Integer distance;
}
