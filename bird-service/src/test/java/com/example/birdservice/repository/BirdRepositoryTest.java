package com.example.birdservice.repository;

import com.example.birdservice.model.Bird;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BirdRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BirdRepository birdRepository;

    @Test
    public void whenFindByName_thenReturnBird() {
        Bird bird = new Bird();
        bird.setName("Blue Jay");
        bird.setColor("Blue");
        entityManager.persist(bird);
        entityManager.flush();

        List<Bird> found = birdRepository.findByNameContainingIgnoreCase("Blue");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo(bird.getName());
    }

    @Test
    public void whenFindByColor_thenReturnBird() {
        Bird bird = new Bird();
        bird.setName("Cardinal");
        bird.setColor("Red");
        entityManager.persist(bird);
        entityManager.flush();

        List<Bird> found = birdRepository.findByColorIgnoreCase("red");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getColor()).isEqualTo("Red");
    }
}