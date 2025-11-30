package main.entity.soil.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.entity.Hazardous;
import main.entity.soil.Soil;

public class DesertSoil extends Soil {
    private final double salinity;

    public DesertSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.salinity = salinity;
    }

    public double getSalinity() {
        return salinity;
    }

    @Override
    public double getScore() {
        double score = (this.getNitrogen() * 0.5) + (this.getWaterRetention() * 0.3)
                        - (salinity * 2);
        return normalizeScore(score);
    }

    @Override
    public double getInteractionProbability() {
        return (100 - this.getWaterRetention() + salinity) / 100 * 100;
    }
}
