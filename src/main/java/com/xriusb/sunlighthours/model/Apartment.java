package com.xriusb.sunlighthours.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@Data
@Builder
@AllArgsConstructor
public class Apartment {
    private Point2D.Float location;
    private Rectangle2D.Float shape;

    public Point2D.Float getEastFloorLocation() {
        Double x = location.getX() + 1;
        return new Point2D.Float(x.floatValue(), location.y);
    }

    public Point2D.Float getEastRoofLocation() {
        Double x = location.getX() + 1;
        Double y = location.getY() + 1;
        return new Point2D.Float(x.floatValue(), y.floatValue());
    }

    public Point2D.Float getWestFloorLocation() {
        return new Point2D.Float(location.x, location.y);
    }

    public Point2D.Float getWestRoofLocation() {
        Double y = location.getY() + 1;
        return new Point2D.Float(location.x, y.floatValue());
    }
}
