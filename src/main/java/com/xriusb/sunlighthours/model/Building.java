package com.xriusb.sunlighthours.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.awt.geom.Rectangle2D;

@Data
@Builder
public class Building {
    private String name;
    @JsonProperty("apartments_count")
    private Integer apartmentsCount;
    private Float distance;
    private Rectangle2D.Float shape;
}
