package main.entity.air.types;

import main.entity.air.Air;

public class TemperateAir extends Air {
    private static final int MAX_SCORE = 84;
    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double HUMIDITY_MULTIPLIER = 0.7;
    private static final double POLLEN_MULTIPLIER = 0.1;
    private static final int SPRING_PENALTY = 15;

    private final double pollenLevel;
    private static String season;
    private static int duration = 0;

    public TemperateAir(final String name, final double mass, final double humidity,
                        final double temperature, final double oxygenLevel,
                        final double pollenLevel) {
        super(name, mass, humidity, temperature, oxygenLevel, MAX_SCORE);
        this.pollenLevel = pollenLevel;
    }

    public final double getPollenLevel() {
        return pollenLevel;
    }

    /**
     * Sets the new season.
     * @param value The new season.
     */
    public static void newSeason(final String value) {
        season = value;
    }

    /**
     * Gets the current duration of the season.
     * @return The duration.
     */
    public static int currentDuration() {
        return duration;
    }

    /**
     * Changes the duration of the season.
     * @param value The new duration.
     */
    public static void changeDuration(final int value) {
        duration = Math.max(value, 0);
    }

    /**
     * Calculates the air quality score for temperate air.
     * This method is safe to be overridden by subclasses.
     * @return The air quality score.
     */
    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * OXYGEN_MULTIPLIER)
                + (this.getHumidity() * HUMIDITY_MULTIPLIER)
                - (pollenLevel * POLLEN_MULTIPLIER));
        double seasonPenalty = "Spring".equalsIgnoreCase(season) ? SPRING_PENALTY : 0;
        return normalAirQuality - seasonPenalty;
    }
}
