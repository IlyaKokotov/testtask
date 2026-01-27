package com.example.birdservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SightingRequestDto {
    @NotNull(message = "Bird ID is required")
    private Long birdId;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "DateTime is required")
    private LocalDateTime dateTime;
}