package com.example.birdservice.service;

import com.example.birdservice.dto.BirdDto;
import com.example.birdservice.exception.ConflictException;
import com.example.birdservice.exception.ResourceNotFoundException;
import com.example.birdservice.model.Bird;
import com.example.birdservice.repository.BirdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BirdServiceImpl implements BirdService {

    @Autowired
    private BirdRepository birdRepository;

    @Override
    public List<BirdDto> findAll(String name, String color) {
        List<Bird> birds;
        if (name != null && color != null) {
            birds = birdRepository.findByNameContainingIgnoreCaseAndColorIgnoreCase(name, color);
        } else if (name != null) {
            birds = birdRepository.findByNameContainingIgnoreCase(name);
        } else if (color != null) {
            birds = birdRepository.findByColorIgnoreCase(color);
        } else {
            birds = birdRepository.findAll();
        }
        return birds.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public BirdDto findById(Long id) {
        return birdRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Bird not found with id: " + id));
    }

    @Override
    @Transactional
    public BirdDto create(BirdDto birdDto) {
        if (birdRepository.existsByNameIgnoreCase(birdDto.getName())) {
            throw new ConflictException("Bird with name '" + birdDto.getName() + "' already exists.");
        }
        Bird bird = new Bird();
        mapToEntity(birdDto, bird);
        return convertToDto(birdRepository.save(bird));
    }

    @Override
    @Transactional
    public BirdDto update(Long id, BirdDto birdDto) {
        Bird bird = birdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bird not found with id: " + id));
        
        if (!bird.getName().equalsIgnoreCase(birdDto.getName()) && 
            birdRepository.existsByNameIgnoreCase(birdDto.getName())) {
            throw new ConflictException("Bird with name '" + birdDto.getName() + "' already exists.");
        }

        mapToEntity(birdDto, bird);
        return convertToDto(birdRepository.save(bird));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!birdRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bird not found with id: " + id);
        }
        try {
            birdRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Cannot delete bird because it has existing sightings.");
        }
    }

    private BirdDto convertToDto(Bird bird) {
        return new BirdDto(bird.getId(), bird.getName(), bird.getColor(), bird.getWeight(), bird.getHeight());
    }

    private void mapToEntity(BirdDto dto, Bird entity) {
        entity.setName(dto.getName());
        entity.setColor(dto.getColor());
        entity.setWeight(dto.getWeight());
        entity.setHeight(dto.getHeight());
    }
}