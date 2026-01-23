package com.example.birdservice.controller;

import com.example.birdservice.model.Bird;
import com.example.birdservice.repository.BirdRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BirdController.class)
public class BirdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BirdRepository birdRepository;

    @Test
    public void testGetAllBirds() throws Exception {
        Bird bird = new Bird();
        bird.setName("Sparrow");
        bird.setColor("Brown");
        
        when(birdRepository.findAll()).thenReturn(Arrays.asList(bird));

        mockMvc.perform(get("/api/birds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sparrow"));
    }

    @Test
    public void testCreateBird() throws Exception {
        Bird bird = new Bird();
        bird.setId(1L);
        bird.setName("Robin");

        when(birdRepository.save(any(Bird.class))).thenReturn(bird);

        mockMvc.perform(post("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Robin\", \"color\": \"Red\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Robin"));
    }

    @Test
    public void testGetBirdById() throws Exception {
        Bird bird = new Bird();
        bird.setId(1L);
        bird.setName("Eagle");

        when(birdRepository.findById(1L)).thenReturn(Optional.of(bird));

        mockMvc.perform(get("/api/birds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Eagle"));
    }
}