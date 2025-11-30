package main.entity.air.types;

import main.entity.air.Air;

public class PolarAir extends Air {
    private final double iceCrystalConcentration;
    private static double windSpeed = 0;
    private static int duration = 0;

    public PolarAir(String name, double mass, double humidity, double temperature,
                    double oxygenLevel, double iceCrystalConcentration) {
        int maxScore = 	142;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    public final double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }

    public static void polarStorm(double value) {
        windSpeed = value;
    }

    public static int currentDuration() {
        return duration;
    }

    public static void changeDuration(int value) {
        duration = Math.max(value, 0);
    }

    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * 2) + (this.getHumidity() * 0.5)
                - (iceCrystalConcentration * 0.01));
        return normalizeScore(normalAirQuality - (windSpeed * 0.2));
    }
}
