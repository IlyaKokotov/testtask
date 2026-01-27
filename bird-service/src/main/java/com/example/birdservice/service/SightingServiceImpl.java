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

@Service
public class SightingServiceImpl implements SightingService {

    @Autowired
    private SightingRepository sightingRepository;

    @Autowired
    private BirdRepository birdRepository;

    @Override
    public List<SightingDto> search(String birdName, String location, LocalDateTime start, LocalDateTime end) {
        List<Sighting> sightings;
        if (birdName != null) sightings = sightingRepository.findByBird_NameContainingIgnoreCase(birdName);
        else if (location != null) sightings = sightingRepository.findByLocationContainingIgnoreCase(location);
        else if (start != null && end != null) sightings = sightingRepository.findByDateTimeBetween(start, end);
        else sightings = sightingRepository.findAll();

        return sightings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

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