package com.harper.asteroids.model;

public record EstimatedDiameter(
        DiameterEstimation kilometers,
        DiameterEstimation meters,
        DiameterEstimation miles,
        DiameterEstimation feet) {
}
