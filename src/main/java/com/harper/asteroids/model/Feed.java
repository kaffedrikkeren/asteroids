package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Response for a feed query of Neos.
 */
public record Feed(
        @JsonProperty("element_count") 
        int elementCount,
        
        @JsonProperty("near_earth_objects") 
        Map<String, List<NearEarthObjectIds>> nearEarthObjects) {

    public List<String> getAllObjectIds() {
        return nearEarthObjects.values().stream()
                .flatMap(l -> l.stream())
                .map(n -> n.id())
                .collect(Collectors.toList());
    }
}
