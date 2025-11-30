package main.entity.soil.types;

import main.entity.soil.Soil;

public class ForestSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 1.2;
    private static final int ORGANIC_MATTER_MULTIPLIER = 2;
    private static final double WATER_RETENTION_SCORE_MULTIPLIER = 1.5;
    private static final double LEAF_LITTER_SCORE_MULTIPLIER = 0.3;
    private static final double WATER_RETENTION_INTERACTION_MULTIPLIER = 0.6;
    private static final double LEAF_LITTER_INTERACTION_MULTIPLIER = 0.4;
    private static final int INTERACTION_PROBABILITY_DIVISOR = 80;
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final double leafLitter;

    public ForestSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.leafLitter = leafLitter;
    }

    /**
     * Gets the leaf litter amount of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The leaf litter amount.
     */
    public double getLeafLitter() {
        return leafLitter;
    }

    /**
     * Calculates the soil quality score for forest soil.
     * This method is safe to be overridden by subclasses.
     * @return The soil quality score.
     */
    @Override
    public double getScore() {
        double score = (this.getNitrogen() * NITROGEN_MULTIPLIER)
                + (this.getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                + (this.getWaterRetention() * WATER_RETENTION_SCORE_MULTIPLIER)
                + (leafLitter * LEAF_LITTER_SCORE_MULTIPLIER);
        return normalizeScore(score);
    }

    /**
     * Calculates the interaction probability for forest soil.
     * This method is safe to be overridden by subclasses.
     * @return The interaction probability.
     */
    @Override
    public double getInteractionProbability() {
        return normalizeScore((this.getWaterRetention() * WATER_RETENTION_INTERACTION_MULTIPLIER
                + leafLitter * LEAF_LITTER_INTERACTION_MULTIPLIER))
                / INTERACTION_PROBABILITY_DIVISOR * PERCENTAGE_MULTIPLIER;
    }
}
