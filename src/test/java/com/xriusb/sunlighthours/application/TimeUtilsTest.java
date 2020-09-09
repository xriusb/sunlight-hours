package com.xriusb.sunlighthours.application;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUtilsTest {

    @Test
    public void givenSunriseAndSunset_when() {
        LocalTime sunrise = LocalTime.of(8,14,00);
        LocalTime sunset = LocalTime.parse("17:25:00");

        assertThat(TimeUtils.getSunlightSeconds(sunrise,sunset)).isEqualTo(33060);

    }

}