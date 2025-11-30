package main.entity.soil.types;

import main.entity.Hazardous;
import main.entity.soil.Soil;

public class SwampSoil extends Soil {
    private final double waterLogging;

    public SwampSoil(String name, double mass, double nitrogen, double waterRetention,
                     double soilpH, double organicMatter, double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.waterLogging = waterLogging;
    }

    public double getWaterLogging() {
        return waterLogging;
    }

    @Override
    public double getScore() {
        double score = (this.getNitrogen() * 1.1) + (this.getOrganicMatter() * 2.2) -
                        (waterLogging * 5);
        return normalizeScore(score);
    }

    @Override
    public double getInteractionProbability() {
        return 	waterLogging * 10;
    }
}
