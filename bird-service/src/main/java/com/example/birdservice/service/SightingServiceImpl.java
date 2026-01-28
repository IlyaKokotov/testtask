package com.example.birdservice.service;

import com.example.birdservice.dto.SightingDto;
import com.example.birdservice.dto.SightingRequestDto;
import com.example.birdservice.exception.ResourceNotFoundException;
import com.example.birdservice.model.Bird;
import com.example.birdservice.model.Sighting;
import com.example.birdservice.repository.BirdRepository;
import com.example.birdservice.repository.SightingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing bird sightings.
 * Provides thread-safe operations for creating and searching sightings.
 */
@Service
public class SightingServiceImpl implements SightingService {

    @Autowired
    private SightingRepository sightingRepository;

    @Autowired
    private BirdRepository birdRepository;

    /**
     * Search for sightings by bird name, location, and/or time interval.
     * All parameters are optional and can be combined.
     * 
     * @param birdName partial bird name (case-insensitive), can be null
     * @param location partial location (case-insensitive), can be null
     * @param start start of time interval (inclusive), can be null
     * @param end end of time interval (inclusive), can be null
     * @return list of matching sightings
     */
    @Override
    public List<SightingDto> search(String birdName, String location, LocalDateTime start, LocalDateTime end) {
        // Use the combined query method that supports all filters simultaneously
        List<Sighting> sightings = sightingRepository.findByFilters(birdName, location, start, end);
        return sightings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Create a new sighting record.
     * 
     * @param request sighting data including bird ID, location, and date-time
     * @return created sighting DTO
     * @throws ResourceNotFoundException if bird with given ID does not exist
     */
    @Override
    @Transactional
    public SightingDto create(SightingRequestDto request) {
        Bird bird = birdRepository.findById(request.getBirdId())
                .orElseThrow(() -> new ResourceNotFoundException("Bird not found with id: " + request.getBirdId()));
        
        Sighting sighting = new Sighting();
        sighting.setBird(bird);
        sighting.setLocation(request.getLocation());
        sighting.setDateTime(request.getDateTime());
        
        return convertToDto(sightingRepository.save(sighting));
    }

    /**
     * Convert a Sighting entity to a SightingDto.
     * 
     * @param sighting the entity to convert
     * @return the corresponding DTO
     */
    private SightingDto convertToDto(Sighting sighting) {
        SightingDto dto = new SightingDto();
        dto.setId(sighting.getId());
        dto.setBirdId(sighting.getBird().getId());
        dto.setBirdName(sighting.getBird().getName());
        dto.setLocation(sighting.getLocation());
        dto.setDateTime(sighting.getDateTime());
        return dto;
    }
}