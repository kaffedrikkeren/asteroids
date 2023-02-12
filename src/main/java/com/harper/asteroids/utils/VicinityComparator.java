package com.harper.asteroids.utils;

import com.harper.asteroids.model.CloseApproachData;
import com.harper.asteroids.model.Distances;
import com.harper.asteroids.model.NearEarthObject;

import java.util.Comparator;
import java.util.Optional;

public class VicinityComparator implements Comparator<NearEarthObject> {

    public int compare(NearEarthObject neo1, NearEarthObject neo2) {

        Optional<Distances> neo1ClosestPass = neo1.closeApproachData().stream()
                .min(Comparator.comparing(CloseApproachData::missDistance))
                .map(min -> min.missDistance());
        Optional<Distances> neo2ClosestPass = neo2.closeApproachData().stream()
                .min(Comparator.comparing(CloseApproachData::missDistance))
                .map(min -> min.missDistance());

        if (neo1ClosestPass.isPresent()) {
            if (neo2ClosestPass.isPresent()) {
                return neo1ClosestPass.get().compareTo(neo2ClosestPass.get());
            } else
                return 1;
        } else
            return -1;
    }
}
