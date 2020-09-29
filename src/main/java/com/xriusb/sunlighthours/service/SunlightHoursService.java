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
import java.util.Optional;

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

    public String getSunlightHours(Neighborhood neighborhood, Building building, Integer apartmentFloor) {
        Apartment apartment = getApartment(building, (float) apartmentFloor);

        LocalTime currentTime = sun.getSunriseTime();
        String startTimeApartmentSunlight = getStartSunlightTime(apartment,
                neighborhood.getBuildingsInEastDirection(apartment.getLocation().getX()), currentTime);

        currentTime = getTimeFromSunOnTopOfTheApartment(apartment);
        String endTimeApartmentSunlight = getEndSunlightTime(apartment,
                neighborhood.getBuildingsInWestDirection(apartment.getLocation().getX()), currentTime);

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
        return buildings.stream().anyMatch(building -> building.getShape().intersectsLine(sunRayToApartmentFloor)
                || building.getShape().intersectsLine(sunRayToApartmentRoof));
    }

    public Optional<Neighborhood> getNeighborhood(String name) {
        return neighborhoods.stream().filter(n -> name.equals(n.getName())).findFirst();
    }

    public Apartment getApartment(Building building, Float apartmentNumber) {
        return new Apartment(new Point2D.Float((float) building.getShape().getX(), apartmentNumber),
                new Rectangle2D.Float((float) building.getShape().getX(), apartmentNumber, Neighborhood.APARTMENT_WIDTH, Neighborhood.APARTMENT_HEIGHT));
    }
}
