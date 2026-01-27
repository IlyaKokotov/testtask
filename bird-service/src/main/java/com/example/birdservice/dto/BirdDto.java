package com.example.birdservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BirdDto {
    private Long id;
    
    @NotBlank(message = "Bird name is mandatory")
    private String name;
    
    @NotBlank(message = "Color is mandatory")
    private String color;
    
    @Positive(message = "Weight must be positive")
    private Double weight;
    
    @Positive(message = "Height must be positive")
    private Double height;
}