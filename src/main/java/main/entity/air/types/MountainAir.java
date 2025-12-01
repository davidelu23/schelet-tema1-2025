package main.entity.air.types;

import main.entity.air.Air;

public class MountainAir extends Air {
    private static final int MAX_SCORE = 78;
    private static final int ALTITUDE_DIVISOR = 1000;
    private static final double ALTITUDE_FACTOR = 0.5;
    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double HUMIDITY_MULTIPLIER = 0.6;
    private static final double HIKER_FACTOR = 0.1;

    private final double altitude;
    private static int numberOfHikers = 0;
    private static int duration = 0;

    public MountainAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double altitude) {
        super(name, mass, humidity, temperature, oxygenLevel, MAX_SCORE, "MountainAir");
        this.altitude = altitude;
    }

    public final double getAltitude() {
        return altitude;
    }

    /**
     * Sets the number of hikers.
     * @param amount The number of hikers.
     */
    public static void peopleHiking(final int amount) {
        numberOfHikers = amount;
    }

    /**
     * Gets the current duration of the hiking event.
     * @return The duration.
     */
    public static int currentDuration() {
        return duration;
    }

    /**
     * Changes the duration of the hiking event.
     * @param value The new duration.
     */
    public static void changeDuration(final int value) {
        duration = Math.max(value, 0);
    }

    /**
     * Calculates the air quality score for mountain air.
     * This method is safe to be overridden by subclasses.
     * @return The air quality score.
     */
    @Override
    public double getScore() {
        double oxygenFactor = this.getOxygenLevel()
                - (altitude / ALTITUDE_DIVISOR * ALTITUDE_FACTOR);
        double normalAirQuality = normalizeScore((oxygenFactor * OXYGEN_MULTIPLIER)
                + (this.getHumidity() * HUMIDITY_MULTIPLIER));
        return normalAirQuality - (numberOfHikers * HIKER_FACTOR);
    }
}
