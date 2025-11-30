package main.entity.soil.types;

import main.entity.soil.Soil;

public class GrasslandSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 1.3;
    private static final double ORGANIC_MATTER_MULTIPLIER = 1.5;
    private static final double ROOT_DENSITY_MULTIPLIER = 0.8;
    private static final int ROOT_DENSITY_BASE = 50;
    private static final double WATER_RETENTION_MULTIPLIER = 0.5;
    private static final int INTERACTION_PROBABILITY_DIVISOR = 75;
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final double rootDensity;

    public GrasslandSoil(final String name, final double mass, final double nitrogen,
                         final double waterRetention, final double soilpH,
                         final double organicMatter, final double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = rootDensity;
    }

    /**
     * Gets the root density of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The root density.
     */
    public double getRootDensity() {
        return rootDensity;
    }

    /**
     * Calculates the soil quality score for grassland soil.
     * This method is safe to be overridden by subclasses.
     * @return The soil quality score.
     */
    @Override
    public double getScore() {
        double score = (this.getNitrogen() * NITROGEN_MULTIPLIER)
                + (this.getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                + (rootDensity * ROOT_DENSITY_MULTIPLIER);
        return normalizeScore(score);
    }

    /**
     * Calculates the interaction probability for grassland soil.
     * This method is safe to be overridden by subclasses.
     * @return The interaction probability.
     */
    @Override
    public double getInteractionProbability() {
        return normalizeScore(((ROOT_DENSITY_BASE - rootDensity)
                + this.getWaterRetention() * WATER_RETENTION_MULTIPLIER))
                / INTERACTION_PROBABILITY_DIVISOR * PERCENTAGE_MULTIPLIER;
    }
}
