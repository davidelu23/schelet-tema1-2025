package main.entity.air.types;

import main.entity.air.Air;

public class DesertAir extends Air {
    private final double dustParticles;

    public DesertAir(String name, double mass, double humidity, double temperature,
                     double oxygenLevel, double dustParticles) {
        int maxScore = 65;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.dustParticles = dustParticles;
    }

    public double getDustParticles() {
        return dustParticles;
    }

    @Override
    public double getScore() {
        return normalizeScore((this.getOxygenLevel() * 2) - (dustParticles * 0.2)
                - (this.getTemperature() * 0.3));
    }
}
