package com.xriusb.sunlighthours.service;

import com.xriusb.sunlighthours.application.Sun;
import com.xriusb.sunlighthours.model.Apartment;
import com.xriusb.sunlighthours.model.Building;
import com.xriusb.sunlighthours.model.Neighborhood;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Getter
@Setter
@Service
public class SunlightHoursService {

    private final List<Neighborhood> neighborhoods = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private Sun sun;

    public void save(List<Neighborhood> neighborhoods) {
        this.neighborhoods.addAll(neighborhoods);
        this.neighborhoods.forEach(Neighborhood::setBuildingsShapes);
    }

    public String getSunlightHours(String neighborhoodName, String building, Integer apartmentFloor) {
        LocalTime currentTime = null;
        String startTimeApartmentSunlight;
        String endTimeApartmentSunlight;

        Neighborhood neighborhood = neighborhoods.stream().filter(n -> neighborhoodName.equals(n.getName()))
                .findFirst().orElse(null);

        Apartment apartment = getApartment(neighborhoodName, building, (float) apartmentFloor);
        List<Building> eastSideApartmentBuildings = neighborhood.getBuildingsAfter(apartment.getLocation().getX());
        List<Building> westSideApartmentBuildings = neighborhood.getBuildingsBefore(apartment.getLocation().getX());

        currentTime = sun.getSunriseTime();
        startTimeApartmentSunlight = getStartSunlightTime(apartment, eastSideApartmentBuildings, currentTime);

        currentTime = getTimeFromSunOnTopOfTheApartment(apartment);
        endTimeApartmentSunlight = getEndSunlightTime(apartment, westSideApartmentBuildings, currentTime);

        return endTimeApartmentSunlight + " - " + startTimeApartmentSunlight;
    }

    private String getStartSunlightTime(Apartment apartment, List<Building> eastSideApartmentBuildings, LocalTime currentTime) {
        String result = null;
        while (currentTime.toSecondOfDay() <= sun.getSunsetTime().toSecondOfDay()) {
            BigDecimal sunAngle = sun.getAngle(currentTime);
            Point2D.Double sunPosition = sun.getPosition(sunAngle);

            Line2D.Float sunRayToApartmentFloor = new Line2D.Float(apartment.getEastFloorLocation(), sunPosition);
            Line2D.Float sunRayToApartmentRoof = new Line2D.Float(apartment.getEastRoofLocation(), sunPosition);
            if (!isApartmentNotShadowed(sunRayToApartmentFloor, sunRayToApartmentRoof, eastSideApartmentBuildings)) {
                result = currentTime.format(formatter);
                break;
            }
            currentTime = currentTime.plusSeconds(1);
        }
        return result;
    }

    private String getEndSunlightTime(Apartment apartment, List<Building> westSideApartmentBuildings, LocalTime currentTime) {
        String result = null;
        while (currentTime.toSecondOfDay() <= sun.getSunsetTime().toSecondOfDay()) {
            BigDecimal sunAngle = sun.getAngle(currentTime);
            Point2D.Double sunPosition = sun.getPosition(sunAngle);

            Line2D.Float sunRayToApartmentFloor = new Line2D.Float(apartment.getWestFloorLocation(), sunPosition);
            Line2D.Float sunRayToApartmentRoof = new Line2D.Float(apartment.getWestRoofLocation(), sunPosition);
            if (isApartmentNotShadowed(sunRayToApartmentFloor, sunRayToApartmentRoof, westSideApartmentBuildings)) {
                result = currentTime.format(formatter);
                break;
            }
            currentTime = currentTime.plusSeconds(1);
        }
        if (result == null) {
            result = currentTime.minusSeconds(1).format(formatter);
        }
        return result;
    }

    private LocalTime getTimeFromSunOnTopOfTheApartment(Apartment apartment) {
        LocalTime currentTime;
        BigDecimal startingWestSunAngle = sun.getAngle(apartment.getLocation().getX());
        currentTime = sun.getTimeFromPosition(startingWestSunAngle);
        return currentTime;
    }

    private boolean isApartmentNotShadowed(Line2D.Float sunRayToApartmentFloor,
                                           Line2D.Float sunRayToApartmentRoof,
                                           List<Building> buildings) {
        boolean intersects = false;
        for(Building building : buildings) {
            if (building.getShape().intersectsLine(sunRayToApartmentFloor)
                    || building.getShape().intersectsLine(sunRayToApartmentRoof)) {
                intersects = true;
            }
        }
        return intersects;
    }

    public Apartment getApartment(String neighborhoodName, String buildingName, Float apartmentNumber) {
        Building building = neighborhoods.stream()
                .filter(n -> neighborhoodName.equals(n.getName()))
                .flatMap(n -> n.getBuildings().stream())
                .filter(b -> b.getName().equals(buildingName)).findFirst().orElse(null);

        if (nonNull(building)) {
            return new Apartment(new Point2D.Float((float) building.getShape().getX(), apartmentNumber),
                    new Rectangle2D.Float((float) building.getShape().getX(), apartmentNumber, Neighborhood.APARTMENT_WIDTH, Neighborhood.APARTMENT_HEIGHT));
        }
        return null;
    }
}
