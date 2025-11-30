package main.entity.air.types;

import main.entity.air.Air;

public class TropicalAir extends Air {
    private final double co2Level;
    private static double rainfall = 0;
    private static int duration = 0;

    public TropicalAir(String name, double mass, double humidity, double temperature,
                       double oxygenLevel, double co2Level) {
        int maxScore = 82;
        super(name, mass, humidity, temperature, oxygenLevel, maxScore);
        this.co2Level = co2Level;
    }

    public final double getCo2Level() {
        return co2Level;
    }

    public static void rainfall(double amount) {
        rainfall = amount;
    }

    public static int currentDuration() {
        return duration;
    }

    public static void changeDuration(int value) {
        duration = Math.max(value, 0);
    }

    @Override
    public double getScore() {
        double normalAirQuality = normalizeScore((this.getOxygenLevel() * 2) + (this.getHumidity() * 0.5)
                - (co2Level * 0.01));
        return normalAirQuality + (rainfall * 0.3);
    }
}
