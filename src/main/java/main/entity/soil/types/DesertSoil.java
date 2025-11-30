package main.entity.soil.types;

import main.entity.soil.Soil;

public class DesertSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 0.5;
    private static final double WATER_RETENTION_MULTIPLIER = 0.3;
    private static final int SALINITY_MULTIPLIER = 2;
    private static final int HUNDRED = 100;

    private final double salinity;

    public DesertSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.salinity = salinity;
    }

    /**
     * Gets the salinity of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The salinity.
     */
    public double getSalinity() {
        return salinity;
    }

    /**
     * Calculates the soil quality score for desert soil.
     * This method is safe to be overridden by subclasses.
     * @return The soil quality score.
     */
    @Override
    public double getScore() {
        double score = (this.getNitrogen() * NITROGEN_MULTIPLIER)
                + (this.getWaterRetention() * WATER_RETENTION_MULTIPLIER)
                - (salinity * SALINITY_MULTIPLIER);
        return normalizeScore(score);
    }

    /**
     * Calculates the interaction probability for desert soil.
     * This method is safe to be overridden by subclasses.
     * @return The interaction probability.
     */
    @Override
    public double getInteractionProbability() {
        return normalizeScore((HUNDRED - this.getWaterRetention() + salinity))
                / HUNDRED * HUNDRED;
    }
}
