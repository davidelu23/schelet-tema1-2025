package main.entity.air.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.entity.air.Air;

public class DesertAir extends Air {
    private final double dustParticles;
    private static boolean desertStorm = false;
    private static int duration = 0;

    public DesertAir(String name, double mass, double humidity, double temperature,
                     double oxygenLevel, double dustParticles) {
        int maxScore = 65;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.dustParticles = dustParticles;
    }

    @JsonIgnore
    public final double getDustParticles() {
        return dustParticles;
    }

    public final boolean isDesertStorm() {
        return desertStorm;
    }

    public static void desertStorm(boolean value) {
        desertStorm = value;
    }

    public static int currentDuration() {
        return duration;
    }

    public static void changeDuration(int value) {
        duration = Math.max(value, 0);
    }

    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * 2) - (dustParticles * 0.2)
                - (this.getTemperature() * 0.3));
        return normalAirQuality - (desertStorm ? 30 : 0);
    }
}
