package com.community.filmjury.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "criteria")
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Criterion name is required")
    private String name;

    @NotBlank(message = "Criterion description is required")
    private String description;

    @NotNull(message = "Maximum score is required")
    @Min(value = 1, message = "Maximum score must be at least 1")
    private Double maxScore;

    public Criterion() {
    }

    public Criterion(Long id, String name, String description, Double maxScore) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxScore = maxScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }
}