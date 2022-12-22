package com.harper.asteroids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

/**
 * Definition for Neo - Near Earth Object
 *
 */
public class NearEarthObject {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("nasa_jpl_url")
    private String nplUrl;

    @JsonProperty("absolute_magnitude_h")
    private double absoluteMagnitude;

    @JsonProperty("is_potentially_hazardous_asteroid")
    private boolean potentiallyHazardous;

    @JsonProperty("close_approach_data")
    private List<CloseApproachData> closeApproachData;

    @JsonProperty("is_sentry_object")
    private boolean isSentryObject;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNplUrl() {
        return nplUrl;
    }

    public double getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    public boolean isPotentiallyHazardous() {
        return potentiallyHazardous;
    }

    public List<CloseApproachData> getCloseApproachData() {
        List<CloseApproachData> approachData = closeApproachData;

        LocalDate today = LocalDate.now();

        Date monday = Date.from(today.with(previousOrSame(MONDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date sunday = Date.from(today.with(previousOrSame(SUNDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // filter out asteroids not approaching this week
        approachData = approachData.stream().filter(ad -> ad.getCloseApproachDate().after(monday) && ad.getCloseApproachDate().before(sunday)).collect(Collectors.toList());

        return approachData;
    }

    public boolean isSentryObject() {
        return isSentryObject;
    }
}
