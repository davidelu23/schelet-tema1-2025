package main.entity.soil.types;

import main.entity.soil.Soil;

public class TundraSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 0.7;
    private static final double ORGANIC_MATTER_MULTIPLIER = 0.5;
    private static final double PERMAFROST_MULTIPLIER = 1.5;
    private static final int FIFTY = 50;
    private static final int HUNDRED = 100;

    private final double permafrostDepth;

    public TundraSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }

    /**
     * Gets the permafrost depth of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The permafrost depth.
     */
    public double getPermafrostDepth() {
        return permafrostDepth;
    }

    /**
     * Calculates the soil quality score for tundra soil.
     * This method is safe to be overridden by subclasses.
     * @return The soil quality score.
     */
    @Override
    public double getScore() {
        double score = (this.getNitrogen() * NITROGEN_MULTIPLIER)
                + (this.getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                - (permafrostDepth * PERMAFROST_MULTIPLIER);
        return normalizeScore(score);
    }

    /**
     * Calculates the interaction probability for tundra soil.
     * This method is safe to be overridden by subclasses.
     * @return The interaction probability.
     */
    @Override
    public double getInteractionProbability() {
        return normalizeScore((FIFTY - permafrostDepth)) / FIFTY * HUNDRED;
    }
}
