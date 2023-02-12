package com.harper.asteroids;

import com.harper.asteroids.model.NearEarthObject;
import com.harper.asteroids.utils.RestUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class TestApproachDetector {

    private NearEarthObject neo1, neo2;

    @Before
    public void setUp() throws IOException {
        neo1 = RestUtils.getMapper().readValue(getClass().getResource("/neo_example.json"), NearEarthObject.class);
        neo2 = RestUtils.getMapper().readValue(getClass().getResource("/neo_example2.json"), NearEarthObject.class);

    }

    @Test
    public void testFiltering() {

        List<NearEarthObject> neos = List.of(neo1, neo2);
        List<NearEarthObject> filtered = ApproachDetector.getClosestApproaches(neos, 1, Optional.empty());
        //Neo2 has the closest passing at 5261628 kms away.
        // TODO: Neo2's closest passing is in 2028.
        // In Jan 202, neo1 is closer (5390966 km, vs neo2's at 7644137 km)
        assertEquals(1, filtered.size());
        assertEquals(neo2, filtered.get(0));

    }
}
