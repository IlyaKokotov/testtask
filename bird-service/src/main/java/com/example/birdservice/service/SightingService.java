package com.example.birdservice.service;

import com.example.birdservice.dto.SightingDto;
import com.example.birdservice.dto.SightingRequestDto;
import java.time.LocalDateTime;
import java.util.List;

public interface SightingService {
    List<SightingDto> search(String birdName, String location, LocalDateTime start, LocalDateTime end);
    SightingDto create(SightingRequestDto request);
}