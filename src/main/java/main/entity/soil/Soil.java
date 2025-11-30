package main.entity.soil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.soil.types.DesertSoil;
import main.entity.soil.types.ForestSoil;
import main.entity.soil.types.GrasslandSoil;
import main.entity.soil.types.SwampSoil;
import main.entity.soil.types.TundraSoil;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ForestSoil.class, name = "ForestSoil"),
        @JsonSubTypes.Type(value = SwampSoil.class, name = "SwampSoil"),
        @JsonSubTypes.Type(value = TundraSoil.class, name = "TundraSoil"),
        @JsonSubTypes.Type(value = DesertSoil.class, name = "DesertSoil"),
        @JsonSubTypes.Type(value = GrasslandSoil.class, name = "GrasslandSoil")
})
public abstract class Soil extends Entity implements Hazardous {
    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;

    private final double nitrogen;
    private double waterRetention;
    private final double soilpH;
    private double organicMatter;

    public Soil(final String name, final double mass, final double nitrogen,
                final double waterRetention, final double soilpH, final double organicMatter) {
        super(name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }

    /**
     * Gets the nitrogen level of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The nitrogen level.
     */
    public double getNitrogen() {
        return nitrogen;
    }

    /**
     * Gets the water retention level of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The water retention level.
     */
    public double getWaterRetention() {
        return waterRetention;
    }

    /**
     * Updates the water retention level of the soil.
     * This method is safe to be overridden by subclasses.
     * @param value The value to add to the water retention.
     */
    public void updateWaterRetention(final double value) {
        waterRetention += value;
        waterRetention = normalizeScore(waterRetention);
    }

    /**
     * Gets the pH of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The soil pH.
     */
    public double getSoilpH() {
        return soilpH;
    }

    /**
     * Gets the organic matter level of the soil.
     * This method is safe to be overridden by subclasses.
     * @return The organic matter level.
     */
    public double getOrganicMatter() {
        return organicMatter;
    }

    /**
     * Sets the organic matter level of the soil.
     * @param organicMatter The new organic matter level.
     */
    public final void setOrganicMatter(final double organicMatter) {
        this.organicMatter = organicMatter;
    }

    /**
     * Calculates the soil quality score.
     * This method is designed to be overridden by subclasses.
     * @return The soil quality score.
     */
    @JsonProperty("soilQuality")
    public abstract double getScore();

    /**
     * Determines the soil quality category based on the score.
     * This method is safe to be overridden by subclasses.
     * @return A string representing the soil quality ("good", "moderate", or "poor").
     */
    @JsonIgnore
    public String getSoilQuality() {
        double quality = getScore();
        if (quality >= GOOD_QUALITY_THRESHOLD) {
            return "good";
        } else if (quality >= MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        } else {
            return "poor";
        }
    }
}
