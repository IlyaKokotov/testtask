package com.example.birdservice.controller;

import com.example.birdservice.dto.BirdDto;
import com.example.birdservice.service.BirdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/birds")
@Tag(name = "Bird Management", description = "Operations for managing bird species records")
public class BirdController {

    @Autowired
    private BirdService birdService;

    @GetMapping
    @Operation(summary = "Get all birds with optional filters")
    public List<BirdDto> getAllBirds(@RequestParam(required = false) String name, 
                                     @RequestParam(required = false) String color) {
        return birdService.findAll(name, color);
    }

    @PostMapping
    @Operation(summary = "Register a new bird species")
    public BirdDto createBird(@Valid @RequestBody BirdDto birdDto) {
        return birdService.create(birdDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bird details by ID")
    public ResponseEntity<BirdDto> getBird(@PathVariable Long id) {
        return ResponseEntity.ok(birdService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing bird record")
    public ResponseEntity<BirdDto> updateBird(@PathVariable Long id, @Valid @RequestBody BirdDto birdDto) {
        return ResponseEntity.ok(birdService.update(id, birdDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bird record")
    public ResponseEntity<Void> deleteBird(@PathVariable Long id) {
        birdService.delete(id);
        return ResponseEntity.ok().build();
    }
}