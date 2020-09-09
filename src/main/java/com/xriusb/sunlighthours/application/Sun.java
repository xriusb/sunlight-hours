package com.xriusb.sunlighthours.application;

import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalTime;

@Component
public class Sun {

    public Point2D.Double getPosition(BigDecimal radius, BigDecimal sunAngle) {
        double angleInRadians = Math.toRadians(sunAngle.doubleValue());

        Point2D.Double point = new Point2D.Double();
        Double x = radius.doubleValue() * Math.cos(angleInRadians);
        Double y = radius.doubleValue() * Math.sin(angleInRadians);

        point.setLocation(x, y);
        return point;
    }

    public BigDecimal getAngle(LocalTime sunrise, LocalTime sunset, LocalTime currentTime) {
        MathContext mc = new MathContext(10);
        return new BigDecimal(180)
                .multiply(new BigDecimal(TimeUtils.getSunlightSeconds(sunrise, currentTime)))
                .divide(new BigDecimal(TimeUtils.getSunlightSeconds(sunrise, sunset)), mc);
    }

    public BigDecimal getAngle(double xCoordinate, BigDecimal radius) {
        BigDecimal x = new BigDecimal(xCoordinate);
        Double angle = Math.toDegrees(Math.acos(x.divide(radius).doubleValue())) ;

        return BigDecimal.valueOf(angle);
    }

    public LocalTime getTimeFromPosition(LocalTime sunrise, LocalTime sunset, BigDecimal sunAngle) {
        MathContext mc = new MathContext(10);
        BigDecimal seconds = sunAngle.multiply(new BigDecimal(TimeUtils.getSunlightSeconds(sunrise, sunset)))
                .divide(BigDecimal.valueOf(180), mc);

        return LocalTime.ofSecondOfDay(seconds.longValue()).plusSeconds(sunrise.toSecondOfDay());
    }
}
