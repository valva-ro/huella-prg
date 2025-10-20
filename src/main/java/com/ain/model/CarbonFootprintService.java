package com.ain.model;

import java.util.Optional;

public class CarbonFootprintService {
    public double calculateWeekly(TransportType transport, double dailyKm, int daysPerWeek) {
        return Optional.ofNullable(transport)
                .map(t -> t.getEmissionFactor() * dailyKm * daysPerWeek)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport type"));
    }

    public String classifyImpact(double weeklyKg) {
        if (weeklyKg <= 5) return "Low";
        else if (weeklyKg <= 15) return "Medium";
        else return "High";
    }

    public String proposeCompensation(double weeklyKg) {
        double trees = weeklyKg / 0.40;
        double bikeKm = weeklyKg / TransportType.CAR.getEmissionFactor();
        return String.format("~%.1f trees (annual) or %.0f km by bike this week", trees, bikeKm);
    }
}
