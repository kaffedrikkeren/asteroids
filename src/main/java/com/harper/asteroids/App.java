package com.harper.asteroids;
import java.util.Arrays;


/**
 * Main app. Gets the list of closest asteroids from NASA at
 * https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=API_KEY
 * See documentation on the Asteroids - NeoWs API at https://api.nasa.gov/
 *
 * Prints the 10 closest
 *
 * Risk of getting throttled if we don't sign up for own key on https://api.nasa.gov/
 * Set environment variable 'API_KEY' to override.
 */
public class App {

    public static void main(String[] args) throws Exception {
        boolean filterCloseApproachesThisWek = Arrays.asList(args).contains("filterCloseApproachesThisWek");
        ApproachDetector.checkForAsteroids(filterCloseApproachesThisWek);
    }
}
