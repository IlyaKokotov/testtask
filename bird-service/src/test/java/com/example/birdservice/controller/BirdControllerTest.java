package com.example.birdservice.controller;

import com.example.birdservice.dto.BirdDto;
import com.example.birdservice.service.BirdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BirdController.class)
public class BirdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BirdService birdService;

    @Test
    public void testGetAllBirds() throws Exception {
        BirdDto bird = new BirdDto(1L, "Sparrow", "Brown", 25.0, 15.0);
        when(birdService.findAll(null, null)).thenReturn(Arrays.asList(bird));

        mockMvc.perform(get("/api/birds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sparrow"));
    }

    @Test
    public void testCreateBird() throws Exception {
        BirdDto bird = new BirdDto(1L, "Robin", "Red", 20.0, 10.0);
        when(birdService.create(any(BirdDto.class))).thenReturn(bird);

        mockMvc.perform(post("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Robin\", \"color\": \"Red\", \"weight\": 20.0, \"height\": 10.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Robin"));
    }

    @Test
    public void testGetBirdById() throws Exception {
        BirdDto bird = new BirdDto(1L, "Eagle", "Brown", 1000.0, 50.0);
        when(birdService.findById(1L)).thenReturn(bird);

        mockMvc.perform(get("/api/birds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Eagle"));
    }
}