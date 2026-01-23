package com.example.birdservice.repository;

import com.example.birdservice.model.Bird;
import com.example.birdservice.model.Sighting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SightingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SightingRepository sightingRepository;

    @Test
    public void whenFindByLocation_thenReturnSighting() {
        Bird bird = new Bird();
        bird.setName("Owl");
        entityManager.persist(bird);

        Sighting sighting = new Sighting();
        sighting.setBird(bird);
        sighting.setLocation("Central Park");
        sighting.setDateTime(LocalDateTime.now());
        entityManager.persist(sighting);
        entityManager.flush();

        List<Sighting> found = sightingRepository.findByLocationContainingIgnoreCase("Central");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getLocation()).isEqualTo("Central Park");
    }
}