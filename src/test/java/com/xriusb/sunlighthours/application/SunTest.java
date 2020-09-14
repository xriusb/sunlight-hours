package com.xriusb.sunlighthours.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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

        ReflectionTestUtils.setField(testee, "sunriseTime", LocalTime.parse("08:14:00"));
        ReflectionTestUtils.setField(testee, "sunsetTime", LocalTime.parse("17:25:00"));
        ReflectionTestUtils.setField(testee, "earthRadius", BigDecimal.valueOf(100));
    }

    @Test
    public void givenSunriseAndSunsetThenReturnAngle() {
        LocalTime currentTime = LocalTime.parse("10:00:00");

        MathContext mc = new MathContext(10);

        assertThat(testee.getAngle(currentTime))
                .isEqualTo(new BigDecimal(34.62794918, mc));
    }

    @Test
    public void givenSunriseAndSunsetGivenSunsetTimeThenReturnsHalfCircle() {
        LocalTime currentTime = LocalTime.parse("17:25:00");

        MathContext mc = new MathContext(10);

        assertThat(testee.getAngle(currentTime))
                .isEqualTo(new BigDecimal(180, mc));
    }

    @Test
    public void givenRadiusAndAngleGetSunPosition() {
        BigDecimal angle = new BigDecimal(30);

        MathContext mc = new MathContext(10);

        Point2D.Double result = new Point2D.Double();

        Double x = 86.60254037844388;
        Double y = 49.99999999999999;

        result.setLocation(x, y);

        assertThat(testee.getPosition(angle))
                .isEqualTo(result);
    }

    @Test
    void calculateSunAngleFromXCoordinate() {

        assertThat(testee.getAngle(0))
                .isEqualTo(BigDecimal.valueOf(90.0));
    }

    @Test
    void givenASunAngleReturnTheTimeWhenItsThere() {

        assertThat(testee.getTimeFromPosition(BigDecimal.valueOf(45)))
                .isEqualTo(LocalTime.parse("10:31:45"));
    }

}