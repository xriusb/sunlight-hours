package com.xriusb.sunlighthours.api;

import com.xriusb.sunlighthours.model.Building;
import com.xriusb.sunlighthours.model.Neighborhood;
import com.xriusb.sunlighthours.service.SunlightHoursService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sunlight-hours")
@Slf4j
@RequiredArgsConstructor
public class SunlightHoursAPI {
    private final SunlightHoursService sunlightHoursService;

    @PostMapping
    public ResponseEntity init(@Valid @RequestBody List<Neighborhood> city) {
        sunlightHoursService.save(city);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{neighbourhood}/{building}/{apartment}")
    public ResponseEntity getSunlightHours(@PathVariable("neighbourhood") String neighbourhoodName,
                                           @PathVariable("building") String buildingName,
                                           @PathVariable("apartment") Integer apartmentNumber) {
        Optional<Neighborhood> neighborhood = sunlightHoursService.getNeighborhood(neighbourhoodName);
        if(neighborhood.isEmpty()) {
            log.error("Neighborhood " + neighbourhoodName + " does not exists");
            return ResponseEntity.badRequest().body("Neighborhood " + neighbourhoodName + " does not exists");
        }
        Optional<Building> building = neighborhood.get().getBuilding(buildingName);
        if(building.isEmpty()) {
            log.error("Building " + buildingName + " does not exists");
            return ResponseEntity.badRequest().body("Building " + buildingName + " does not exists");
        }
        return ResponseEntity.ok(sunlightHoursService.getSunlightHours(neighborhood.get(), building.get(), apartmentNumber));
    }
}
