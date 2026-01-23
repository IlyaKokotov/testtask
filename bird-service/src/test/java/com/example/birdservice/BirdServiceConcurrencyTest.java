package com.example.birdservice;

import com.example.birdservice.model.Bird;
import com.example.birdservice.repository.BirdRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrency test to verify thread safety and data integrity under load.
 * Simulates multiple threads creating bird records simultaneously.
 */
@SpringBootTest
public class BirdServiceConcurrencyTest {

    @Autowired
    private BirdRepository birdRepository;

    @Test
    public void testConcurrentBirdCreation() throws Exception {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            futures.add(CompletableFuture.runAsync(() -> {
                Bird bird = new Bird();
                bird.setName("ThreadBird-" + index);
                bird.setColor("Various");
                birdRepository.save(bird);
            }, executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long count = birdRepository.findAll().stream()
                .filter(b -> b.getName().startsWith("ThreadBird-"))
                .count();
        
        assertThat(count).isEqualTo(threadCount);
        executor.shutdown();
    }
}