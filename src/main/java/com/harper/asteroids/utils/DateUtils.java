package com.harper.asteroids.utils;

import java.time.LocalDate;

import com.harper.asteroids.model.DateInterval;

public class DateUtils {

    public static DateInterval getDateInterval(LocalDate startLocalDate, LocalDate endLocalDate) {
        return new DateInterval(startLocalDate, endLocalDate);
    }
    
    public static DateInterval getCommingWeekInterval() {
        LocalDate weekStart = LocalDate.now().minusWeeks(1);
        LocalDate weekStop = weekStart.plusWeeks(1);
        return getDateInterval(weekStart, weekStop);
    }
}
