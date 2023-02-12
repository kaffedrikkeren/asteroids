package com.harper.asteroids;

import com.harper.asteroids.model.CloseApproachData;
import com.harper.asteroids.model.DateInterval;
import com.harper.asteroids.model.NearEarthObject;
import com.harper.asteroids.utils.DateUtils;
import com.harper.asteroids.utils.RestUtils;
import com.harper.asteroids.utils.VicinityComparator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class TestVicinityComparator {

    private NearEarthObject neo1, neo2, neo3, neo4;
    @Before
    public void setUp() throws IOException {
        neo1 = RestUtils.getMapper().readValue(getClass().getResource("/neo_example.json"), NearEarthObject.class);
        neo2 = RestUtils.getMapper().readValue(getClass().getResource("/neo_example2.json"), NearEarthObject.class);
        neo3 = RestUtils.getMapper().readValue(getClass().getResource("/neo_example3.json"), NearEarthObject.class);
        neo4 = RestUtils.getMapper().readValue(getClass().getResource("/neo_example4.json"), NearEarthObject.class);

    }

    @Test
    public void testOrder() {
        VicinityComparator comparator = new VicinityComparator();

        assertThat(comparator.compare(neo1, neo2), greaterThan(0));
        assertThat(comparator.compare(neo2, neo1), lessThan(0));
        assertEquals(comparator.compare(neo1, neo1), 0);
    }


    @Test
    public void testPredicator() {  
        LocalDate startDate = LocalDate.of(2023, 2, 5);
        LocalDate enDate = LocalDate.of(2023, 2, 12);
        DateInterval dateInterval = DateUtils.getDateInterval(startDate, enDate);
        Predicate<CloseApproachData> p = CloseApproachData.dateFilterPredicate().apply(dateInterval.startDate(), dateInterval.endDate());
        List<CloseApproachData> closestApproaches  = List.of(neo3, neo4).stream().flatMap(d -> d.closeApproachData().stream().filter(p)).collect(Collectors.toList());
        assertEquals(closestApproaches.size(), 2);
    }

}
