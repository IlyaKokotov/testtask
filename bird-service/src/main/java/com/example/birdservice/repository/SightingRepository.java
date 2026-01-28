package com.example.birdservice.repository;

import com.example.birdservice.model.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SightingRepository extends JpaRepository<Sighting, Long> {
    // Single filter methods (kept for backward compatibility)
    List<Sighting> findByBird_NameContainingIgnoreCase(String birdName);
    List<Sighting> findByLocationContainingIgnoreCase(String location);
    List<Sighting> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Combined query that supports filtering by bird name, location, and time interval.
     * Any parameter can be null to skip that filter.
     * Uses JPQL to handle optional parameters efficiently.
     */
    @Query("SELECT s FROM Sighting s WHERE " +
           "(:birdName IS NULL OR LOWER(s.bird.name) LIKE LOWER(CONCAT('%', :birdName, '%'))) AND " +
           "(:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:start IS NULL OR s.dateTime >= :start) AND " +
           "(:end IS NULL OR s.dateTime <= :end)")
    List<Sighting> findByFilters(@Param("birdName") String birdName, 
                                  @Param("location") String location,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);
}