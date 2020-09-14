package com.xriusb.sunlighthours.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalTime;

@Component
@Getter
public class Sun {

    @Value("${app.earthRadius}")
    private BigDecimal earthRadius;
    @Value("#{ T(java.time.LocalTime).parse('${app.sunriseTime}')}")
    private LocalTime sunriseTime;
    @Value("#{ T(java.time.LocalTime).parse('${app.sunsetTime}')}")
    private LocalTime sunsetTime;

    public Point2D.Double getPosition(BigDecimal sunAngle) {
        double angleInRadians = Math.toRadians(sunAngle.doubleValue());

        Point2D.Double point = new Point2D.Double();
        Double x = earthRadius.doubleValue() * Math.cos(angleInRadians);
        Double y = earthRadius.doubleValue() * Math.sin(angleInRadians);

        point.setLocation(x, y);
        return point;
    }

    public BigDecimal getAngle(LocalTime currentTime) {
        MathContext mc = new MathContext(10);
        return new BigDecimal(180)
                .multiply(new BigDecimal(TimeUtils.getSunlightSeconds(sunriseTime, currentTime)))
                .divide(new BigDecimal(TimeUtils.getSunlightSeconds(sunriseTime, sunsetTime)), mc);
    }

    public BigDecimal getAngle(double xCoordinate) {
        BigDecimal x = new BigDecimal(xCoordinate);
        Double angle = Math.toDegrees(Math.acos(x.divide(earthRadius).doubleValue())) ;

        return BigDecimal.valueOf(angle);
    }

    public LocalTime getTimeFromPosition(BigDecimal sunAngle) {
        MathContext mc = new MathContext(10);
        BigDecimal seconds = sunAngle.multiply(new BigDecimal(TimeUtils.getSunlightSeconds(sunriseTime, sunsetTime)))
                .divide(BigDecimal.valueOf(180), mc);

        return LocalTime.ofSecondOfDay(seconds.longValue()).plusSeconds(sunriseTime.toSecondOfDay());
    }
}
