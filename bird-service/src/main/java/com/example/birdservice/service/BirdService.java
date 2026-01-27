package com.example.birdservice.service;

import com.example.birdservice.dto.BirdDto;
import java.util.List;

public interface BirdService {
    List<BirdDto> findAll(String name, String color);
    BirdDto findById(Long id);
    BirdDto create(BirdDto birdDto);
    BirdDto update(Long id, BirdDto birdDto);
    void delete(Long id);
}