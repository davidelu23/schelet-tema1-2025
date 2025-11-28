package main.entity.water;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.entity.Entity;

import static java.lang.Math.abs;

public class Water extends Entity {
    private final String type;
    private final double salinity;
    private final double pH;
    private final double purity;
    private final double turbidity;
    private final double contaminantIndex;
    private final boolean isFrozen;

    public Water(String name, double mass, String type, double salinity, double pH, double purity, double turbidity, double contaminantIndex, boolean isFrozen) {
        super(name, mass);
        this.type = type;
        this.salinity = salinity;
        this.pH = pH;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }

    public final String getType() {
        return type;
    }

    @JsonIgnore
    public double getWaterQuality() {
        double purity_score = purity / 100;
        double pH_score = 1 - abs(pH - 7.5) / 7.5;
        double salinity_score = 1 - (salinity / 350);
        double turbidity_score = 1 - (turbidity / 100);
        double contaminant_score = 1 - (contaminantIndex / 100);
        int frozen_score = isFrozen ? 0 : 1;

        return (0.3 * purity_score
                + 0.2 * pH_score
                + 0.15 * salinity_score
                + 0.1 * turbidity_score
                + 0.15 * contaminant_score
                + 0.2 * frozen_score) * 100;
    }

    @JsonIgnore
    public final double getSalinity() {
        return salinity;
    }

    @JsonIgnore
    public double getpH() {
        return pH;
    }

    @JsonIgnore
    public final double getPurity() {
        return purity;
    }

    @JsonIgnore
    public final double getTurbidity() {
        return turbidity;
    }

    @JsonIgnore
    public final double getContaminantIndex() {
        return contaminantIndex;
    }

    @JsonIgnore
    public final boolean isFrozen() {
        return isFrozen;
    }
}
