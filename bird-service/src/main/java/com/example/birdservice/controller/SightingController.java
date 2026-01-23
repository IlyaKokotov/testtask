package com.example.birdservice.controller;

import com.example.birdservice.model.Sighting;
import com.example.birdservice.repository.SightingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sightings")
public class SightingController {

    @Autowired
    private SightingRepository sightingRepository;

    @GetMapping
    public List<Sighting> getSightings(
            @RequestParam(required = false) String birdName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        if (birdName != null) return sightingRepository.findByBird_NameContainingIgnoreCase(birdName);
        if (location != null) return sightingRepository.findByLocationContainingIgnoreCase(location);
        if (start != null && end != null) return sightingRepository.findByDateTimeBetween(start, end);
        
        return sightingRepository.findAll();
    }

    @PostMapping
    public Sighting createSighting(@RequestBody Sighting sighting) {
        return sightingRepository.save(sighting);
    }
}