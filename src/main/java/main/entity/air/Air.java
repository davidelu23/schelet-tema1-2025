package main.entity.air;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import main.entity.Entity;
import main.entity.air.types.DesertAir;
import main.entity.air.types.MountainAir;
import main.entity.air.types.PolarAir;
import main.entity.air.types.TemperateAir;
import main.entity.air.types.TropicalAir;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MountainAir.class, name = "MountainAir"),
        @JsonSubTypes.Type(value = TemperateAir.class, name = "TemperateAir"),
        @JsonSubTypes.Type(value = TropicalAir.class, name = "TropicalAir"),
        @JsonSubTypes.Type(value = PolarAir.class, name = "PolarAir"),
        @JsonSubTypes.Type(value = DesertAir.class, name = "DesertAir")
})
public abstract class Air extends Entity {
    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;
    private static final int PERCENTAGE_MULTIPLIER = 100;
    private static final double TOXICITY_THRESHOLD_FACTOR = 0.8;
    private static final int DECIMAL_ROUNDING_FACTOR = 10;

    private final String type;
    private double humidity;
    private final double temperature;
    private double oxygenLevel;
    private final int maxScore;

    public Air(final String name, final double mass, final double humidity,
               final double temperature, final double oxygenLevel, final int maxScore,
               final String type) {
        super(name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.maxScore = maxScore;
        this.type = type;
    }

    public Air(final Air air) {
        super(air.getName(), air.getMass());
        this.humidity = air.getHumidity();
        this.temperature = air.getTemperature();
        this.oxygenLevel = air.getOxygenLevel();
        this.maxScore = air.maxScore;
        this.type = air.type;
    }

    @JsonIgnore
    public final String getType() {
        return type;
    }

    @JsonIgnore
    public final double getHumidity() {
        return humidity;
    }

    /**
     * Returns the humidity rounded to one decimal place.
     * @return The rounded humidity.
     */
    @JsonProperty("humidity")
    public final double showHumidity() {
        return (double) Math.round(humidity * DECIMAL_ROUNDING_FACTOR) / DECIMAL_ROUNDING_FACTOR;
    }

    /**
     * Updates the humidity by a given value and normalizes it.
     * This method is safe to be overridden by subclasses.
     * @param value The value to add to the humidity.
     */
    public void updateHumidity(final double value) {
        humidity += value;
    }

    public final double getTemperature() {
        return temperature;
    }

    @JsonIgnore
    public final double getOxygenLevel() {
        return oxygenLevel;
    }

    /**
     * Returns the oxygen level rounded to one decimal place.
     * @return The rounded oxygen level.
     */
    @JsonProperty("oxygenLevel")
    public final double showOxygenLevel() {
        return (double) Math.round(oxygenLevel * DECIMAL_ROUNDING_FACTOR) / DECIMAL_ROUNDING_FACTOR;
    }

    /**
     * Calculates the air quality score.
     * This method is designed to be overridden by subclasses.
     * @return The air quality score.
     */
    @JsonProperty("airQuality")
    public abstract double getScore();

    /**
     * Determines the air quality category based on the score.
     * This method is safe to be overridden by subclasses.
     * @return A string representing the air quality ("good", "moderate", or "poor").
     */
    @JsonIgnore
    public String getAirQuality() {
        double quality = normalizeScore(getScore());
        if (quality >= GOOD_QUALITY_THRESHOLD) {
            return "good";
        } else if (quality >= MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        } else {
            return "poor";
        }
    }

    /**
     * Calculates the toxicity of the air based on its quality score.
     * This method is safe to be overridden by subclasses.
     * @return The toxicity score.
     */
    @JsonIgnore
    public double getToxicityAQ() {
        return normalizeScore(PERCENTAGE_MULTIPLIER * (1 - getScore() / maxScore));
    }

    /**
     * Checks if the air is toxic.
     * This method is safe to be overridden by subclasses.
     * @return true if the air is toxic, false otherwise.
     */
    @JsonIgnore
    public boolean isToxic() {
        return getToxicityAQ() > (TOXICITY_THRESHOLD_FACTOR * maxScore);
    }

    /**
     * Updates the oxygen level by adding a given amount and normalizes it.
     * This method is safe to be overridden by subclasses.
     * @param oxygenToAdd The amount of oxygen to add.
     */
    public void updateOxygenLevel(final double oxygenToAdd) {
        this.oxygenLevel += oxygenToAdd;
    }

    @Override
    public void updateEnvironment(final Object simulation) {

    }
}
