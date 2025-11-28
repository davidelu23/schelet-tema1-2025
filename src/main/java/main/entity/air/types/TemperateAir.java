package main.entity.air.types;

import main.entity.air.Air;

public class TemperateAir extends Air {
    private final double pollenLevel;

    public TemperateAir(String name, double mass, double humidity, double temperature,
                        double oxygenLevel, double pollenLevel) {
        int maxScore = 84;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.pollenLevel = pollenLevel;
    }

    public double getPollenLevel() {
        return pollenLevel;
    }

    @Override
    public double getScore() {
        return normalizeScore((this.getOxygenLevel() * 2) + (this.getHumidity() * 0.7)
                - (pollenLevel * 0.1));
    }
}
