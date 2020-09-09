package com.xriusb.sunlighthours.application;

import java.time.LocalTime;

public class TimeUtils {

    public static int getSunlightSeconds(LocalTime sunrise, LocalTime sunset) {
        return sunset.toSecondOfDay() - sunrise.toSecondOfDay();
    }
}
