package com.harper.asteroids.model;

import com.harper.asteroids.utils.RestUtils;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestParsing {

    @Test
    public void testFeedExampleDump() throws IOException {
        URL input = getClass().getResource("/feed_example.json");
        Feed feed = RestUtils.getMapper().readValue(input, Feed.class);
        System.out.println("Check feed");
        assertNotNull(feed);
        assertEquals(feed.elementCount(), feed.getAllObjectIds().size());

    }

    @Test
    public void testNeoExampleDump() throws IOException {
        URL input = getClass().getResource("/neo_example.json");
        NearEarthObject neo = RestUtils.getMapper().readValue(input, NearEarthObject.class);
        System.out.println("Check neo: " + neo);
        assertNotNull(neo);

    }

}
