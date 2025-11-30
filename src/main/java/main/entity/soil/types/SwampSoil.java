package main.entity.soil.types;

import main.entity.soil.Soil;

public class SwampSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 1.1;
    private static final double ORGANIC_MATTER_MULTIPLIER = 2.2;
    private static final int WATER_LOGGING_MULTIPLIER = 5;
    private static final int INTERACTION_PROBABILITY_MULTIPLIER = 10;

    private final double waterLogging;

    public SwampSoil(final String name, final double mass, final double nitrogen,
                     final double waterRetention, final double soilpH,
                     final double organicMatter, final double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.waterLogging = waterLogging;
    }

    /**
     * Gets the water logging level of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The water logging level.
     */
    public double getWaterLogging() {
        return waterLogging;
    }

    /**
     * Calculates the soil quality score for swamp soil.
     * This method is safe to be overridden by subclasses.
     * @return The soil quality score.
     */
    @Override
    public double getScore() {
        double score = (this.getNitrogen() * NITROGEN_MULTIPLIER)
                + (this.getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                - (waterLogging * WATER_LOGGING_MULTIPLIER);
        return normalizeScore(score);
    }

    /**
     * Calculates the interaction probability for swamp soil.
     * This method is safe to be overridden by subclasses.
     * @return The interaction probability.
     */
    @Override
    public double getInteractionProbability() {
        return normalizeScore(waterLogging) * INTERACTION_PROBABILITY_MULTIPLIER;
    }
}
