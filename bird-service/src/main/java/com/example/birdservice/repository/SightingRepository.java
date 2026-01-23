package com.example.birdservice.repository;

import com.example.birdservice.model.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SightingRepository extends JpaRepository<Sighting, Long> {
    // Uses underscore to explicitly traverse to Bird.name
    List<Sighting> findByBird_NameContainingIgnoreCase(String birdName);
    List<Sighting> findByLocationContainingIgnoreCase(String location);
    List<Sighting> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}