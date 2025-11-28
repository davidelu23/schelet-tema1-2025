package main.entity.air;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import main.entity.Entity;
import main.entity.air.types.MountainAir;
import main.entity.air.types.TemperateAir;
import main.entity.air.types.TropicalAir;
import main.entity.air.types.PolarAir;
import main.entity.air.types.DesertAir;

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
    private final double humidity;
    private final double temperature;
    private final double oxygenLevel;
    private final double maxScore;

    public Air(String name, double mass, double humidity, double temperature,
               double oxygenLevel, double maxScore) {
        super(name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.maxScore = maxScore;
    }

    public Air(Air air) {
        super(air.getName(), air.getMass());
        this.humidity = air.getHumidity();
        this.temperature = air.getTemperature();
        this.oxygenLevel = air.getOxygenLevel();
        this.maxScore = air.maxScore;
    }

    public final double getHumidity() {
        return humidity;
    }

    public final double getTemperature() {
        return temperature;
    }
    public final double getOxygenLevel() {
        return oxygenLevel;
    }

    @JsonProperty("airQuality")
    public abstract double getScore();

    @JsonIgnore
    public String getAirQuality() {
        double quality = normalizeScore(getScore());
        if (quality >= 70) {
            return "good";
        } else if (quality >= 40) {
            return "moderate";
        } else {
            return "poor";
        }
    }

    @JsonIgnore
    public boolean isToxic() {
        double toxicityAQ = normalizeScore(getScore());
        return toxicityAQ > (0.8 * maxScore);
    }
}
