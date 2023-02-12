package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Distances(
        @JsonProperty("astronomical") 
        Double astronomical,

        @JsonProperty("lunar") 
        Double lunar,

        @JsonProperty("kilometers") 
        Double kilometers,
        
        @JsonProperty("miles") 
        Double miles) implements Comparable<Distances> {

    @Override
    public int compareTo(Distances other) {
        return kilometers.compareTo(other.kilometers());
    }
}
