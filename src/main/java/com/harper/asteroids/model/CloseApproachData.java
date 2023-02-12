package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public record CloseApproachData(
        @JsonProperty("close_approach_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
        LocalDate closeApproachDate,
        // @JsonProperty("close_approach_date_full")
        // @JsonDeserialize(using = LocalDateTimeDeserializer.class) 
        // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd hh:mm") 
        // LocalDateTime closeApproachDateTime,
        @JsonProperty("epoch_date_close_approach") 
        long closeApproachEpochDate,
        
        @JsonProperty("relative_velocity") 
        Velocities relativeVelocity,
        
        @JsonProperty("miss_distance") 
        Distances missDistance,
        
        @JsonProperty("orbiting_body")
        String orbitingBody) {

    public static BiFunction<LocalDate, LocalDate, Predicate<CloseApproachData>> dateFilterPredicate() {
        return (startDate, endDate) -> {
            return closestApproachDataObject -> closestApproachDataObject.closeApproachDate().isBefore(endDate)
                    && closestApproachDataObject.closeApproachDate().isAfter(startDate);
        };
    }
}
