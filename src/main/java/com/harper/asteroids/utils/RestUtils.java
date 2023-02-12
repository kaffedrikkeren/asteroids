package com.harper.asteroids.utils;

import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class RestUtils {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static String getResponseBody(Response response) {
        int statusCode = response.getStatus();
        if (statusCode == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            throw new RuntimeException(
                    String.format("Received statuscode %d, full error message %s", statusCode, response.readEntity(String.class)));
        }
    }
}
