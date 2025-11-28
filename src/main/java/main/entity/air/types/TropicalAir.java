package main.entity.air.types;

import main.entity.air.Air;

public class TropicalAir extends Air {
    private final double co2Level;

    public TropicalAir(String name, double mass, double humidity, double temperature,
                       double oxygenLevel, double co2Level) {
        int maxScore = 82;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.co2Level = co2Level;
    }

    public double getCo2Level() {
        return co2Level;
    }

    @Override
    public double getScore() {
        return normalizeScore((this.getOxygenLevel() * 2) + (this.getHumidity() * 0.5)
                - (co2Level * 0.01));
    }
}
