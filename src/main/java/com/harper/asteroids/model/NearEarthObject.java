package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NearEarthObject(
        @JsonProperty("id") 
        String id,
        @JsonProperty("name") 
        String name,
        @JsonProperty("nasa_jpl_url") 
        String nplUrl,
        @JsonProperty("absolute_magnitude_h") 
        double absoluteMagnitude,
        @JsonProperty("is_potentially_hazardous_asteroid") 
        boolean potentiallyHazardous,
        @JsonProperty("close_approach_data") 
        List<CloseApproachData> closeApproachData,
        @JsonProperty("is_sentry_object") 
        boolean isSentryObject) {
}
