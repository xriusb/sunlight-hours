package com.xriusb.sunlighthours.service;

import com.xriusb.sunlighthours.application.Sun;
import com.xriusb.sunlighthours.model.Apartment;
import com.xriusb.sunlighthours.model.Building;
import com.xriusb.sunlighthours.model.Neighborhood;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SunlightHoursServiceTest {

    private static final String NEIGHBORHOOD_NAME = "PobleNou";
    private static final String BUILDING_1_NAME = "Block_A";
    private static final String BUILDING_2_NAME = "Block_B";
    private static final String BUILDING_3_NAME = "Block_C";
    private static final Integer BUILDING_1_HEIGHT = 4;
    private static final Integer BUILDING_2_HEIGHT = 2;
    private static final Integer BUILDING_3_HEIGHT = 6;

    private Neighborhood neighborhood;
    private Building building_1;

    private SunlightHoursService testee;

    @BeforeEach
    public void setup() {
        testee = new SunlightHoursService();

        building_1 = Building.builder().name(BUILDING_1_NAME).apartmentsCount(4).distance(2f).build();

        List<Building> neighborhoodBuildings = Arrays.asList(building_1,
                Building.builder().name(BUILDING_2_NAME).apartmentsCount(2).distance(3f).build(),
                Building.builder().name(BUILDING_3_NAME).apartmentsCount(6).distance(2f).build());

        neighborhood = Neighborhood.builder()
                .name(NEIGHBORHOOD_NAME)
                .apartmentsHeight(6)
                .buildings(neighborhoodBuildings)
                .build();

        List<Neighborhood> neighborhoods = Arrays.asList(neighborhood);

        testee.save(neighborhoods);

        Sun sun = new Sun();
        ReflectionTestUtils.setField(sun, "sunriseTime", LocalTime.parse("08:14:00"));
        ReflectionTestUtils.setField(sun, "sunsetTime", LocalTime.parse("17:25:00"));
        ReflectionTestUtils.setField(sun, "earthRadius", BigDecimal.valueOf(100));

        testee.setSun(sun);
    }

    @Test
    void givenNameOfNeighborhoodAndApartmentThenReturnsSunlightHours() {
        assertThat(testee.getSunlightHours(neighborhood, building_1, 1 ))
                .isEqualTo("17:25:00 - 10:20:34");
    }

    @Test
    public void givenANeighborhoodWhenCallingSaveThenMakesBuildingShapes() {
        Iterator<Neighborhood> neighborhoodIterator = testee.getNeighborhoods().iterator();
        Iterator<Building> buildingIterator = neighborhoodIterator.next().getBuildings().iterator();

        assertThat(buildingIterator.next().getShape())
                .isEqualTo(new Rectangle2D.Float(-4, 0, Neighborhood.APARTMENT_WIDTH, BUILDING_1_HEIGHT));
        assertThat(buildingIterator.next().getShape())
                .isEqualTo(new Rectangle2D.Float(-1, 0, Neighborhood.APARTMENT_WIDTH, BUILDING_2_HEIGHT));
        assertThat(buildingIterator.next().getShape())
                .isEqualTo(new Rectangle2D.Float(3, 0, Neighborhood.APARTMENT_WIDTH, BUILDING_3_HEIGHT));
    }

    @Test
    public void getApartmentPointAndRectangle() {
        Apartment result = testee.getApartment(building_1, 2f );

        assertThat(result.getLocation()).isEqualTo(new Point2D.Float(-4, 2));
    }
}