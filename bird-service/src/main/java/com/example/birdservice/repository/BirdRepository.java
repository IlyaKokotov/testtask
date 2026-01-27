package com.example.birdservice.repository;

import com.example.birdservice.model.Bird;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BirdRepository extends JpaRepository<Bird, Long> {
    List<Bird> findByNameContainingIgnoreCase(String name);
    List<Bird> findByColorIgnoreCase(String color);
    List<Bird> findByNameContainingIgnoreCaseAndColorIgnoreCase(String name, String color);
    boolean existsByNameIgnoreCase(String name);
}