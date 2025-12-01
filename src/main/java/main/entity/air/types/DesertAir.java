package main.entity.air.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.entity.air.Air;

public class DesertAir extends Air {
    private static final int MAX_SCORE = 65;
    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double DUST_PARTICLE_MULTIPLIER = 0.2;
    private static final double TEMPERATURE_MULTIPLIER = 0.3;
    private static final int DESERT_STORM_PENALTY = 30;

    private final double dustParticles;
    private static boolean desertStorm = false;
    private static int duration = 0;

    public DesertAir(final String name, final double mass, final double humidity,
                     final double temperature, final double oxygenLevel,
                     final double dustParticles) {
        super(name, mass, humidity, temperature, oxygenLevel, MAX_SCORE, "DesertAir");
        this.dustParticles = dustParticles;
    }

    @JsonIgnore
    public final double getDustParticles() {
        return dustParticles;
    }

    /**
     * Checks if there is a desert storm.
     * @return true if there is a desert storm, false otherwise.
     */
    public final boolean isDesertStorm() {
        return desertStorm;
    }

    /**
     * Sets the desert storm status.
     * @param value The new desert storm status.
     */
    public static void desertStorm(final boolean value) {
        desertStorm = value;
    }

    /**
     * Gets the current duration of the desert storm.
     * @return The duration.
     */
    public static int currentDuration() {
        return duration;
    }

    /**
     * Changes the duration of the desert storm.
     * @param value The new duration.
     */
    public static void changeDuration(final int value) {
        duration = Math.max(value, 0);
    }

    /**
     * Calculates the air quality score for desert air.
     * This method is safe to be overridden by subclasses.
     * @return The air quality score.
     */
    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * OXYGEN_MULTIPLIER)
                - (dustParticles * DUST_PARTICLE_MULTIPLIER)
                - (this.getTemperature() * TEMPERATURE_MULTIPLIER));
        return normalAirQuality - (desertStorm ? DESERT_STORM_PENALTY : 0);
    }
}
