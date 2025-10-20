package com.ain.model;

public enum TransportType {
    CAR(0.21),
    BUS(0.10),
    TRAIN(0.04),
    BIKE(0.00),
    FOOT(0.00);

    private final double emissionFactor;

    TransportType(double emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public double getEmissionFactor() {
        return emissionFactor;
    }

    public static TransportType fromString(String name) {
        try {
            return TransportType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
