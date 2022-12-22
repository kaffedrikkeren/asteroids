package com.harper.asteroids;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harper.asteroids.model.NearEarthObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Receives a set of neo ids and rates them after earth proximity.
 * Retrieves the approach data for them and sorts to the n closest.
 * https://api.nasa.gov/neo/rest/v1/neo/
 * Alerts if someone is possibly hazardous.
 */
public class ApproachDetector {
    private static final String NEO_URL = "https://api.nasa.gov/neo/rest/v1/neo/";
    private List<String> nearEarthObjectIds;
    private Client client;
    private ObjectMapper mapper = new ObjectMapper();

    public ApproachDetector(List<String> ids) {
        this.nearEarthObjectIds = ids;
        this.client = ClientBuilder.newClient();
        // fix UnrecognizedPropertyException thrown by NearEarthObject if not ignored
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Get the n closest approaches in this period
     * @param limit - n
     */
    public List<NearEarthObject> getClosestApproaches(int limit) {
        List<NearEarthObject> neos = new ArrayList<>(limit);

        int partitionSize = 10; // 10 queries per thread

        List<List<String>> partitions = new LinkedList<>();
        for (int i = 0; i < nearEarthObjectIds.size(); i += partitionSize) {
            partitions.add(nearEarthObjectIds.subList(i,
                    Math.min(i + partitionSize, nearEarthObjectIds.size())));
        }

        ExecutorService es = Executors.newFixedThreadPool(partitions.size()); // n number of threads in parallel


        for (List partition: partitions){
            es.execute(new NEOsCollector(partition, neos));
        }

        es.shutdown();
        try {
            // wait for queries to complete
            es.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (Exception e){
            System.err.println(e);
        }

        System.out.println("Received " + neos.size() + " neos, now sorting");
        return getClosest(neos, limit);
    }

    /**
     * Get the closest passing.
     * @param neos the NearEarthObjects
     * @param limit
     * @return
     */
    public static List<NearEarthObject> getClosest(List<NearEarthObject> neos, int limit) {
        return neos.stream()
                .filter(neo -> neo.getCloseApproachData() != null && ! neo.getCloseApproachData().isEmpty())
                .sorted(new VicinityComparator())
                .limit(limit)
                .collect(Collectors.toList());
    }

    class NEOsCollector implements Runnable{

        List<String> partitions;
        List<NearEarthObject> neos;

        public NEOsCollector(List<String> partitions, List<NearEarthObject> neos){
            this.partitions = partitions;
            this.neos = neos;
        }

        @Override
        public void run() {
            System.out.println("Running " + partitions.size() + " queries");
            for(String id: partitions) {
                try {
                    System.out.println("Check passing of object " + id);
                    Response response = client
                            .target(NEO_URL + id)
                            .queryParam("api_key", App.API_KEY)
                            .request(MediaType.APPLICATION_JSON)
                            .get();

                    NearEarthObject neo = mapper.readValue(response.readEntity(String.class), NearEarthObject.class);
                    neos.add(neo);
                } catch (IOException e) {
                    System.err.println("Failed scanning for asteroids: " + e);
                }
            }
        }
    }
}
