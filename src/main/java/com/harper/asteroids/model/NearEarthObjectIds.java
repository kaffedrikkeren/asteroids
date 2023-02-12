package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NearEarthObjectIds(
        @JsonProperty("id") 
        String id,

        @JsonProperty("name") 
        String name) {
}
