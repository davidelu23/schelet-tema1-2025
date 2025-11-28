package main.entity.soil.types;

import main.entity.soil.Soil;
import main.entity.Hazardous;

public class TundraSoil extends Soil implements Hazardous {
    private final double permafrostDepth;

    public TundraSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter,  double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }

    public double getPermafrostDepth() {
        return permafrostDepth;
    }

    @Override
    public double getScore() {
        double score = (this.getNitrogen() * 0.7) + (this.getOrganicMatter() * 0.5)
                        - (permafrostDepth * 1.5);
        return normalizeScore(score);
    }

    @Override
    public double getInteractionProbability() {
        return 	(50 - permafrostDepth) / 50 * 100;
    }
}
