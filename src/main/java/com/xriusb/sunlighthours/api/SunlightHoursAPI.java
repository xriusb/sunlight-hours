package com.xriusb.sunlighthours.api;

import com.xriusb.sunlighthours.model.Neighborhood;
import com.xriusb.sunlighthours.service.SunlightHoursService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity getSunlightHours(@PathVariable("neighbourhood") String neighbourhood,
                                           @PathVariable("building") String building,
                                           @PathVariable("apartment") Integer apartment) {
        return ResponseEntity.ok(sunlightHoursService.getSunlightHours(neighbourhood, building, apartment));
    }

}
