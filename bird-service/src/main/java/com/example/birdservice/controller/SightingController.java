package com.example.birdservice.controller;

import com.example.birdservice.dto.SightingDto;
import com.example.birdservice.dto.SightingRequestDto;
import com.example.birdservice.service.SightingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sightings")
@Tag(name = "Sighting Management", description = "Operations for recording and searching bird sightings")
public class SightingController {

    @Autowired
    private SightingService sightingService;

    @GetMapping
    @Operation(summary = "Search sightings")
    public List<SightingDto> getSightings(
            @RequestParam(required = false) String birdName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        return sightingService.search(birdName, location, start, end);
    }

    @PostMapping
    @Operation(summary = "Record a new sighting")
    public SightingDto createSighting(@Valid @RequestBody SightingRequestDto request) {
        return sightingService.create(request);
    }
}