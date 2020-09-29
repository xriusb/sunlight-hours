package com.xriusb.sunlighthours.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class NeighborhoodTest {

    public static final String BUILDING_A_NAME = "BUILDING_A_NAME";
    public static final String BUILDING_B_NAME = "BUILDING_B_NAME";
    public static final String BUILDING_C_NAME = "BUILDING_C_NAME";
    private Neighborhood testee;
    private Building buildingA;
    private Building buildingB;
    private Building buildingC;

    @BeforeEach
    public void setup() {
        testee = new Neighborhood(null, null, null);
        buildingA = Building.builder().name(BUILDING_A_NAME).distance(2f).build();
        buildingB = Building.builder().name(BUILDING_B_NAME).distance(4.5f).build();
        buildingC = Building.builder().name(BUILDING_C_NAME).distance(50f).build();

        testee.setBuildings(Arrays.asList(buildingA, buildingB, buildingC));
    }

    @Test
    public void getSurfaceOnlyAssumesDistances() {
        assertThat(testee.getSurface()).isEqualTo(9.5f);
    }

    @Test
    public void givenAnExistingBuildingName_thenReturnIt() {
        assertThat(testee.getBuilding(BUILDING_A_NAME).get()).isEqualTo(buildingA);
    }
}