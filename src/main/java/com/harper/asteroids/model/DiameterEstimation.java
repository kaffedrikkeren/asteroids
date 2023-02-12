package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiameterEstimation(
        @JsonProperty("estimated_diameter_min") 
        Double min,

        @JsonProperty("estimated_diameter_max") 
        Double max) {
}
