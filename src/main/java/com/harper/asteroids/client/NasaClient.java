package com.harper.asteroids.client;

import java.time.LocalDate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import com.harper.asteroids.model.Feed;
import com.harper.asteroids.model.NearEarthObject;
import com.harper.asteroids.utils.RestUtils;

public class NasaClient {
    private static final String NASA_API_URL = "https://api.nasa.gov";
    private static final Client client = ClientBuilder.newClient(new ClientConfig());
    private final String apiKey;

    public NasaClient(String apiKey) {
        this.apiKey = apiKey;
    }

    private static String getNeoFeedUrl() {
        return NASA_API_URL + "/neo/rest/v1/feed";
    }

    private static String getNeoUrl() {
        return NASA_API_URL + "/neo/rest/v1/neo/";
    }

    public Feed getFeedOfClosestNearEarthObjects(LocalDate startDate, LocalDate endDate) throws Exception {
        Response response = client
                .target(getNeoFeedUrl())
                .queryParam("start_date", startDate.toString())
                .queryParam("end_date", endDate.toString())
                .queryParam("api_key", this.apiKey)
                .request(MediaType.APPLICATION_JSON)
                .get();
        String jsonResponse = RestUtils.getResponseBody(response);
        return RestUtils.getMapper().readValue(jsonResponse, Feed.class);
    }

    public NearEarthObject getNearEarthObject(String id) {
        try {
            System.out.println("Check passing of object " + id);
            Response response = client
                    .target(getNeoUrl() + id)
                    .queryParam("api_key", this.apiKey)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            String jsonResponse = RestUtils.getResponseBody(response);
            return RestUtils.getMapper().readValue(jsonResponse, NearEarthObject.class);
        } catch (Exception e) {
            return null;
        }
    }

}