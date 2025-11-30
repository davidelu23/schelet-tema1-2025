package main.entity.soil.types;

import main.entity.Hazardous;
import main.entity.soil.Soil;

public class GrasslandSoil extends Soil {
    private final double rootDensity;

    public GrasslandSoil(String name, double mass, double nitrogen, double waterRetention,
                         double soilpH, double organicMatter, double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = rootDensity;
    }

    public double getRootDensity() {
        return rootDensity;
    }

    @Override
    public double getScore() {
        double score = (this.getNitrogen() * 1.3) + (this.getOrganicMatter() * 1.5)
                        + (rootDensity * 0.8);
        return normalizeScore(score);
    }

    @Override
    public double getInteractionProbability() {
        return ((50 - rootDensity) + this.getWaterRetention() * 0.5) / 75 * 100;
    }
}
