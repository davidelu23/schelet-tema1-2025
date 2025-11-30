package main.entity.air.types;

import main.entity.air.Air;

public class MountainAir extends Air {
    private final double altitude;
    private static int numberOfHikers = 0;
    private static int duration = 0;

    public MountainAir(String name, double mass, double humidity, double temperature,
                       double oxygenLevel, double altitude) {
        int maxScore = 78;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.altitude = altitude;
    }

    public final double getAltitude() {
        return altitude;
    }

    public static void peopleHiking(int amount) {
        numberOfHikers = amount;
    }

    public static int currentDuration() {
        return duration;
    }

    public static void changeDuration(int value) {
        duration = Math.max(value, 0);
    }

    @Override
    public double getScore() {
        double oxygenFactor = this.getOxygenLevel() - (altitude / 1000 * 0.5);
        double normalAirQuality = normalizeScore((oxygenFactor * 2) + (this.getHumidity() * 0.6));
        return normalAirQuality - (numberOfHikers * 0.1);
    }
}
