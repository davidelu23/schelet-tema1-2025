package main.entity.air.types;

import main.entity.air.Air;

public class TropicalAir extends Air {
    private static final int MAX_SCORE = 82;
    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double HUMIDITY_MULTIPLIER = 0.5;
    private static final double CO2_MULTIPLIER = 0.01;
    private static final double RAINFALL_MULTIPLIER = 0.3;

    private final double co2Level;
    private static double rainfall = 0;
    private static int duration = 0;

    public TropicalAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double co2Level) {
        super(name, mass, humidity, temperature, oxygenLevel, MAX_SCORE);
        this.co2Level = co2Level;
    }

    public final double getCo2Level() {
        return co2Level;
    }

    /**
     * Sets the rainfall amount.
     * @param amount The rainfall amount.
     */
    public static void rainfall(final double amount) {
        rainfall = amount;
    }

    /**
     * Gets the current duration of the rainfall.
     * @return The duration.
     */
    public static int currentDuration() {
        return duration;
    }

    /**
     * Changes the duration of the rainfall.
     * @param value The new duration.
     */
    public static void changeDuration(final int value) {
        duration = Math.max(value, 0);
    }

    /**
     * Calculates the air quality score for tropical air.
     * This method is safe to be overridden by subclasses.
     * @return The air quality score.
     */
    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * OXYGEN_MULTIPLIER)
                + (this.getHumidity() * HUMIDITY_MULTIPLIER)
                - (co2Level * CO2_MULTIPLIER));
        return normalAirQuality + (rainfall * RAINFALL_MULTIPLIER);
    }
}
