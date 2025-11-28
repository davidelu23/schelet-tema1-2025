package main.entity.air.types;

import main.entity.air.Air;

public class MountainAir extends Air {
    private final double altitude;

    public MountainAir(String name, double mass, double humidity, double temperature,
                       double oxygenLevel, double altitude) {
        int maxScore = 78;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    @Override
    public double getScore() {
        double oxygenFactor = this.getOxygenLevel() - (altitude / 1000 * 0.5);
        return normalizeScore((oxygenFactor * 2) + (this.getHumidity() * 0.6));
    }
}
