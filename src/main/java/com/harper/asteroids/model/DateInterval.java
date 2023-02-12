package com.harper.asteroids.model;

import java.time.LocalDate;

public record DateInterval(
        LocalDate startDate,
        LocalDate endDate) {
}
