package com.harper.asteroids;

import com.harper.asteroids.client.NasaClient;
import com.harper.asteroids.model.CloseApproachData;
import com.harper.asteroids.model.DateInterval;
import com.harper.asteroids.model.NearEarthObject;
import com.harper.asteroids.utils.DateUtils;
import com.harper.asteroids.utils.EnvUtils;
import com.harper.asteroids.utils.VicinityComparator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Receives a set of neo ids and rates them after earth proximity.
 * Retrieves the approach data for them and sorts to the n closest.
 * https://api.nasa.gov/neo/rest/v1/neo/
 * Alerts if someone is possibly hazardous.
 */
public class ApproachDetector {
        private static final NasaClient nasaClient = new NasaClient(
                        EnvUtils.getEnvOrDefault("API_KEY", "DEMO_KEY"));

        /**
         * Scan space for asteroids close to earth
         */
        public static void checkForAsteroids(boolean filterClosestApproachesThisWek) throws Exception {

                List<NearEarthObject> neos = nasaClient
                                .getFeedOfClosestNearEarthObjects(LocalDate.now(), LocalDate.now())
                                .getAllObjectIds()
                                .parallelStream()
                                .map(nasaClient::getNearEarthObject)
                                .filter(neo -> neo != null)
                                .collect(Collectors.toList());

                Predicate<CloseApproachData> dateFilterPredicate = null;
                if (filterClosestApproachesThisWek) {
                        DateInterval commingWeekInterval = DateUtils.getCommingWeekInterval();
                        dateFilterPredicate = CloseApproachData.dateFilterPredicate().apply(
                                        commingWeekInterval.startDate(),
                                        commingWeekInterval.endDate());
                        System.out.println("Finding close approach data for comming week, from date: "
                                        + commingWeekInterval.startDate() + " to date:"
                                        + commingWeekInterval.endDate());
                }

                List<NearEarthObject> nearEarthObjects = getClosestApproaches(
                                neos,
                                10,
                                Optional.ofNullable(dateFilterPredicate));

                printNearthEarthObjects(nearEarthObjects);

        }

        public static List<NearEarthObject> getClosestApproaches(List<NearEarthObject> neos, int limit,
                        Optional<Predicate<CloseApproachData>> closeApproachPredicate) {
                Predicate<CloseApproachData> p = closeApproachPredicate.orElse(v -> true);

                return neos.stream()
                                .filter(neo -> neo.closeApproachData() != null && !neo.closeApproachData().isEmpty())
                                .map(neo -> {
                                        List<CloseApproachData> filteredApproachData = neo.closeApproachData()
                                                        .stream()
                                                        .filter(p)
                                                        .collect(Collectors.toList());
                                        return new NearEarthObject(
                                                        neo.id(),
                                                        neo.name(),
                                                        neo.nplUrl(),
                                                        neo.absoluteMagnitude(),
                                                        neo.potentiallyHazardous(),
                                                        filteredApproachData,
                                                        neo.isSentryObject());
                                })
                                .sorted(new VicinityComparator())
                                .limit(limit)
                                .collect(Collectors.toList());
        }

        private static void printNearthEarthObjects(List<NearEarthObject> nearEarthObjects) {
                System.out.println("Hazard?   Distance(km)    When                             Name");
                System.out.println("----------------------------------------------------------------------");
                nearEarthObjects.forEach(neo -> {
                        Optional<CloseApproachData> closestPass = neo.closeApproachData().stream()
                                        .min(Comparator.comparing(CloseApproachData::missDistance));

                        if (closestPass.isEmpty())
                                return;

                        System.out.println(formatNearthObject(neo, closestPass));

                });
        }

        private static String formatNearthObject(NearEarthObject neo, Optional<CloseApproachData> closestPass) {
                return String.format("%s       %12.3f  %s    %s",
                                (neo.potentiallyHazardous() ? "!!!" : " - "),
                                closestPass.get().missDistance().kilometers(),
                                closestPass.get().closeApproachDate(),
                                neo.name());
        }

}
