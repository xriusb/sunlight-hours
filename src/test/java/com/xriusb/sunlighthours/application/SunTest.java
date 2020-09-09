package com.xriusb.sunlighthours.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SunTest {

    private Sun testee;

    @BeforeEach
    public void setup() {
        testee = new Sun();
    }

    @Test
    public void givenSunriseAndSunsetThenReturnAngle() {
        LocalTime sunrise = LocalTime.parse("08:14:00");
        LocalTime sunset = LocalTime.parse("17:25:00");
        LocalTime currentTime = LocalTime.parse("10:00:00");

        MathContext mc = new MathContext(10);

        assertThat(testee.getAngle(sunrise, sunset, currentTime))
                .isEqualTo(new BigDecimal(34.62794918, mc));
    }

    @Test
    public void givenSunriseAndSunsetGivenSunsetTimeThenReturnsHalfCircle() {
        LocalTime sunrise = LocalTime.parse("08:14:00");
        LocalTime sunset = LocalTime.parse("17:25:00");
        LocalTime currentTime = LocalTime.parse("17:25:00");

        MathContext mc = new MathContext(10);

        assertThat(testee.getAngle(sunrise, sunset, currentTime))
                .isEqualTo(new BigDecimal(180, mc));
    }

    @Test
    public void givenRadiusAndAngleGetSunPosition() {
        BigDecimal radius = new BigDecimal(50);
        BigDecimal angle = new BigDecimal(30);

        MathContext mc = new MathContext(10);

        Point2D.Double result = new Point2D.Double();

        Double x = 43.30127018922194;
        Double y = 24.999999999999996;

        result.setLocation(x, y);

        assertThat(testee.getPosition(radius, angle))
                .isEqualTo(result);
    }

    @Test
    void calculateSunAngleFromXCoordinate() {
        BigDecimal radius = new BigDecimal(4);

        assertThat(testee.getAngle(0, radius))
                .isEqualTo(BigDecimal.valueOf(90.0));
    }

    @Test
    void givenASunAngleReturnTheTimeWhenItsThere() {
        LocalTime sunrise = LocalTime.parse("08:14:00");
        LocalTime sunset = LocalTime.parse("17:25:00");

        assertThat(testee.getTimeFromPosition(sunrise, sunset, BigDecimal.valueOf(45)))
                .isEqualTo(LocalTime.parse("10:31:45"));
    }

}