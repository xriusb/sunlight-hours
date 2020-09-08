package com.xriusb.sunlighthours.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Neighborhood {
    @JsonProperty("neighborhood")
    private String name;
    @JsonProperty("apartments_height")
    private Integer apartmentsHeight;
    private List<Building> buildings;
}
