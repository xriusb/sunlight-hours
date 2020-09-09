package com.xriusb.sunlighthours.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Neighborhood {
    public static final Float APARTMENT_WIDTH = 1f;
    public static final Float APARTMENT_HEIGHT = 1f;

    @JsonProperty("neighborhood")
    private String name;
    @JsonProperty("apartments_height")
    private Integer apartmentsHeight;
    private List<Building> buildings;

    public Float getSurface() {
        float surface = 0f;
        for (int i = 0; i < buildings.size() - 1; i++) {
            surface += buildings.get(i).getDistance() + APARTMENT_WIDTH;
        }
        surface += APARTMENT_WIDTH;
        return surface;
    }

    public void setBuildingsShapes() {
        float buildingX = getSurface() / 2 - getSurface();
        for (Building building : buildings) {
            building.setShape(new Rectangle2D.Float(
                    buildingX,
                    0,
                    Neighborhood.APARTMENT_WIDTH,
                    building.getApartmentsCount()));

            buildingX += building.getDistance() + Neighborhood.APARTMENT_WIDTH;
        }
    }

    public List<Building> getBuildingsAfter(double xCoordinate) {
        List<Building> result = new ArrayList<>();
        buildings.forEach(building -> {
            if (building.getShape().getX() > xCoordinate ) {
                result.add(building);
            }
        });
        return result;
    }

    public List<Building> getBuildingsBefore(double xCoordinate) {
        List<Building> result = new ArrayList<>();
        buildings.forEach(building -> {
            if (building.getShape().getX() < xCoordinate ) {
                result.add(building);
            }
        });
        return result;
    }


}
