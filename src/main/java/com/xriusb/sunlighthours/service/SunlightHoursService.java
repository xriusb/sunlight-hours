package com.xriusb.sunlighthours.service;

import com.xriusb.sunlighthours.application.Sun;
import com.xriusb.sunlighthours.model.Apartment;
import com.xriusb.sunlighthours.model.Building;
import com.xriusb.sunlighthours.model.Neighborhood;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Getter
@Setter
@Service
public class SunlightHoursService {

    private final List<Neighborhood> neighborhoods = new ArrayList<>();

    @Value("${app.earthRadius}")
    private BigDecimal earthRadius;
    @Value("${app.sunriseTime}")
    private String sunriseTime;
    @Value("${app.sunsetTime}")
    private String sunsetTime;

    @Autowired
    private Sun geometricUtils;

    public List<Neighborhood> save(List<Neighborhood> neighborhoods) {
        this.neighborhoods.addAll(neighborhoods);
        this.neighborhoods.forEach(Neighborhood::setBuildingsShapes);
        return this.neighborhoods;
    }

    public String getSunlightHours(String neighborhoodName, String building, Integer apartmentFloor) {
        LocalTime sunrise = LocalTime.parse(this.sunriseTime);
        LocalTime sunset = LocalTime.parse(this.sunsetTime);
        LocalTime currentTime = null;
        String startTimeApartmentSunlight;
        String endTimeApartmentSunlight;

        Neighborhood neighborhood = neighborhoods.stream().filter(n -> neighborhoodName.equals(n.getName()))
                .findFirst().orElse(null);

        Apartment apartment = getApartment(neighborhoodName, building, (float) apartmentFloor);
        List<Building> eastSideApartmentBuildings = neighborhood.getBuildingsAfter(apartment.getLocation().getX());
        List<Building> westSideApartmentBuildings = neighborhood.getBuildingsBefore(apartment.getLocation().getX());

        currentTime = sunrise;
        startTimeApartmentSunlight = getStartSunlightTime(sunrise, sunset, apartment, eastSideApartmentBuildings, currentTime);

        currentTime = getTimeFromSunOnTopOfTheApartment(sunrise, sunset, apartment);
        endTimeApartmentSunlight = getEndSunlightTime(sunrise, sunset, apartment, westSideApartmentBuildings, currentTime);

        return endTimeApartmentSunlight + " - " + startTimeApartmentSunlight;
    }

    private String getStartSunlightTime(LocalTime sunrise, LocalTime sunset, Apartment apartment, List<Building> eastSideApartmentBuildings, LocalTime currentTime) {
        String result = null;
        while (currentTime.toSecondOfDay() <= sunset.toSecondOfDay()) {
            BigDecimal sunAngle = geometricUtils.getAngle(sunrise, sunset, currentTime);
            Point2D.Double sunPosition = geometricUtils.getPosition(earthRadius, sunAngle);

            Line2D.Float sunRayToApartmentFloor = new Line2D.Float(apartment.getEastFloorLocation(), sunPosition);
            Line2D.Float sunRayToApartmentRoof = new Line2D.Float(apartment.getEastRoofLocation(), sunPosition);
            if (!isApartmentNotShadowed(sunRayToApartmentFloor, sunRayToApartmentRoof, eastSideApartmentBuildings)) {
                result = currentTime.toString();
                break;
            }
            currentTime = currentTime.plusSeconds(1);
        }
        return result;
    }

    private String getEndSunlightTime(LocalTime sunrise, LocalTime sunset, Apartment apartment, List<Building> westSideApartmentBuildings, LocalTime currentTime) {
        String result = null;
        while (currentTime.toSecondOfDay() <= sunset.toSecondOfDay()) {
            BigDecimal sunAngle = geometricUtils.getAngle(sunrise, sunset, currentTime);
            Point2D.Double sunPosition = geometricUtils.getPosition(earthRadius, sunAngle);

            Line2D.Float sunRayToApartmentFloor = new Line2D.Float(apartment.getWestFloorLocation(), sunPosition);
            Line2D.Float sunRayToApartmentRoof = new Line2D.Float(apartment.getWestRoofLocation(), sunPosition);
            if (isApartmentNotShadowed(sunRayToApartmentFloor, sunRayToApartmentRoof, westSideApartmentBuildings)) {
                result = currentTime.toString();
                break;
            }
            currentTime = currentTime.plusSeconds(1);
        }
        if (result == null) {
            result = currentTime.toString();
        }
        return result;
    }

    private LocalTime getTimeFromSunOnTopOfTheApartment(LocalTime sunrise, LocalTime sunset, Apartment apartment) {
        LocalTime currentTime;
        BigDecimal startingWestSunAngle = geometricUtils.getAngle(apartment.getLocation().getX(), BigDecimal.valueOf(100));
        currentTime = geometricUtils.getTimeFromPosition(sunrise, sunset, startingWestSunAngle);
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
        Neighborhood neighborhood = neighborhoods.stream().filter(n -> neighborhoodName.equals(n.getName()))
                .findFirst().orElse(null);

        if (nonNull(neighborhood)) {
            Building building = neighborhood.getBuildings().stream()
                    .filter(building1 -> buildingName.equals(building1.getName())).findFirst().orElse(null);

            if (nonNull(building)) {
                boolean isTopFloor = false;
                if (building.getApartmentsCount() - 1 == apartmentNumber) {
                    isTopFloor = true;
                }
                return new Apartment(new Point2D.Float((float) building.getShape().getX(), apartmentNumber),
                        new Rectangle2D.Float((float) building.getShape().getX(), apartmentNumber, Neighborhood.APARTMENT_WIDTH, Neighborhood.APARTMENT_HEIGHT),
                        isTopFloor);
            }
        }
        return null;
    }
}
