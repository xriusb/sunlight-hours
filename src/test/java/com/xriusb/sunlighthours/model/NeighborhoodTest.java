package com.xriusb.sunlighthours.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class NeighborhoodTest {

    private Neighborhood testee;

    @BeforeEach
    public void setup() {
        testee = new Neighborhood(null, null, null);
    }

    @Test
    public void getSurfaceOnlyAssumesDistances() {
        testee.setBuildings(Arrays.asList(
                Building.builder().distance(2f).build(),
                Building.builder().distance(4.5f).build(),
                Building.builder().distance(50f).build()));

        assertThat(testee.getSurface()).isEqualTo(9.5f);
    }
}