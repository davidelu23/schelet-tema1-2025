package main.entity.air.types;

import main.entity.air.Air;

public class PolarAir extends Air {
    private static final int MAX_SCORE = 142;
    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double HUMIDITY_MULTIPLIER = 0.5;
    private static final double ICE_CRYSTAL_MULTIPLIER = 0.01;
    private static final double WIND_SPEED_MULTIPLIER = 0.2;

    private final double iceCrystalConcentration;
    private static double windSpeed = 0;
    private static int duration = 0;

    public PolarAir(final String name, final double mass, final double humidity,
                    final double temperature, final double oxygenLevel,
                    final double iceCrystalConcentration) {
        super(name, mass, humidity, temperature, oxygenLevel, MAX_SCORE);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    public final double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }

    /**
     * Sets the wind speed for a polar storm.
     * @param value The wind speed.
     */
    public static void polarStorm(final double value) {
        windSpeed = value;
    }

    /**
     * Gets the current duration of the polar storm.
     * @return The duration.
     */
    public static int currentDuration() {
        return duration;
    }

    /**
     * Changes the duration of the polar storm.
     * @param value The new duration.
     */
    public static void changeDuration(final int value) {
        duration = Math.max(value, 0);
    }

    /**
     * Calculates the air quality score for polar air.
     * This method is safe to be overridden by subclasses.
     * @return The air quality score.
     */
    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * OXYGEN_MULTIPLIER)
                + (this.getHumidity() * HUMIDITY_MULTIPLIER)
                - (iceCrystalConcentration * ICE_CRYSTAL_MULTIPLIER));
        return normalAirQuality - (windSpeed * WIND_SPEED_MULTIPLIER);
    }
}
