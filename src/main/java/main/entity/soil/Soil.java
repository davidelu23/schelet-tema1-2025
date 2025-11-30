package main.entity.soil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.soil.types.ForestSoil;
import main.entity.soil.types.GrasslandSoil;
import main.entity.soil.types.SwampSoil;
import main.entity.soil.types.TundraSoil;
import main.entity.soil.types.DesertSoil;


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
    private final double nitrogen;
    private final double waterRetention;
    private final double soilpH;
    private double organicMatter;

    public Soil(String name, double mass, double nitrogen, double waterRetention, double soilpH, double organicMatter) {
        super(name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }

    public double getNitrogen() {
        return nitrogen;
    }

    public double getWaterRetention() {
        return waterRetention;
    }

    public double getSoilpH() {
        return soilpH;
    }

    public double getOrganicMatter() {
        return organicMatter;
    }

    public final void setOrganicMatter(double organicMatter) {
        this.organicMatter = organicMatter;
    }

    @JsonProperty("soilQuality")
    public abstract double getScore();

    @JsonIgnore
    public String getSoilQuality() {
        double quality = getScore();
        if (quality >= 70) {
            return "good";
        } else if (quality >= 40) {
            return "moderate";
        } else {
            return "poor";
        }
    }
}
