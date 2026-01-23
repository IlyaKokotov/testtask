package com.example.birdservice.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Sighting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bird_id")
    private Bird bird;

    private String location;
    private LocalDateTime dateTime;

    public Sighting() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bird getBird() { return bird; }
    public void setBird(Bird bird) { this.bird = bird; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
}