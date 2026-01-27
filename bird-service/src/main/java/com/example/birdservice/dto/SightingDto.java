package com.example.birdservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SightingDto {
    private Long id;
    private Long birdId;
    private String birdName;
    private String location;
    private LocalDateTime dateTime;
}