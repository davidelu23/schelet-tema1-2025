package main.entity.air.types;

import main.entity.air.Air;

public class PolarAir extends Air {
    private final double iceCrystalConcentration;

    public PolarAir(String name, double mass, double humidity, double temperature,
                    double oxygenLevel, double iceCrystalConcentration) {
        int maxScore = 	142;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    public double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }

    @Override
    public double getScore() {
        return normalizeScore((this.getOxygenLevel() * 2) + (this.getHumidity() * 0.5)
                - (iceCrystalConcentration * 0.01));
    }
}
