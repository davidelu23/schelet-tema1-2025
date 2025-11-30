package main.entity.air.types;

import main.entity.air.Air;

public class TemperateAir extends Air {
    private final double pollenLevel;
    private static String season;
    private static int duration = 0;

    public TemperateAir(String name, double mass, double humidity, double temperature,
                        double oxygenLevel, double pollenLevel) {
        int maxScore = 84;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.pollenLevel = pollenLevel;
    }

    public final double getPollenLevel() {
        return pollenLevel;
    }

    public static void newSeason(String value) {
        season = value;
    }

    public static int currentDuration() {
        return duration;
    }

    public static void changeDuration(int value) {
        duration = Math.max(value, 0);
    }

    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * 2) + (this.getHumidity() * 0.7)
                - (pollenLevel * 0.1));
        double seasonPenalty = "Spring".equalsIgnoreCase(season) ? 15 : 0;
        return normalAirQuality - seasonPenalty;
    }
}
