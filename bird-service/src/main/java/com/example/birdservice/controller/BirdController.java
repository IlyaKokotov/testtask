package com.example.birdservice.controller;

import com.example.birdservice.model.Bird;
import com.example.birdservice.repository.BirdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * REST Controller for managing Bird records.
 * Provides endpoints for CRUD operations and filtered searches.
 */
@RestController
@RequestMapping("/api/birds")
public class BirdController {

    @Autowired
    private BirdRepository birdRepository;

    @GetMapping
    public List<Bird> getAllBirds(@RequestParam(required = false) String name, 
                                  @RequestParam(required = false) String color) {
        if (name != null && color != null) {
            return birdRepository.findByNameContainingIgnoreCaseAndColorIgnoreCase(name, color);
        } else if (name != null) {
            return birdRepository.findByNameContainingIgnoreCase(name);
        } else if (color != null) {
            return birdRepository.findByColorIgnoreCase(color);
        }
        return birdRepository.findAll();
    }

    @PostMapping
    public Bird createBird(@RequestBody Bird bird) {
        return birdRepository.save(bird);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bird> getBird(@PathVariable Long id) {
        return birdRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bird> updateBird(@PathVariable Long id, @RequestBody Bird birdDetails) {
        return birdRepository.findById(id).map(bird -> {
            bird.setName(birdDetails.getName());
            bird.setColor(birdDetails.getColor());
            bird.setWeight(birdDetails.getWeight());
            bird.setHeight(birdDetails.getHeight());
            return ResponseEntity.ok(birdRepository.save(bird));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBird(@PathVariable Long id) {
        if (birdRepository.existsById(id)) {
            birdRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}