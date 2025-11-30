package main.entity.soil.types;

import main.entity.Hazardous;
import main.entity.soil.Soil;

public class ForestSoil extends Soil {
    private final double leafLitter;

    public ForestSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.leafLitter = leafLitter;
    }

    public double getLeafLitter() {
        return leafLitter;
    }

    @Override
    public double getScore() {
        double score = (this.getNitrogen() * 1.2) + (this.getOrganicMatter() * 2)
                + (this.getWaterRetention() * 1.5) + (leafLitter * 0.3);
        return normalizeScore(score);
    }

    @Override
    public double getInteractionProbability() {
        return 	(this.getWaterRetention() * 0.6 + leafLitter * 0.4) / 80 * 100;
    }
}
