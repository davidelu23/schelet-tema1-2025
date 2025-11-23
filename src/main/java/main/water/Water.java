package main.water;

import main.Entity;
import main.Hazardous;

import static java.lang.Math.abs;

public class Water extends Entity {
    private double salinity;
    private double pH;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;

    public Water(String name, double mass, double salinity, double pH, double purity, double turbidity, double contaminantIndex, boolean isFrozen) {
        super(name, mass);
        this.salinity = salinity;
        this.pH = pH;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }

    public double getWaterQuality() {
        double purity_score = purity / 100;
        double pH_score = 1 - abs(pH - 7.5) / 7.5;
        double salinity_score = 1 - (salinity / 350);
        double turbidity_score = 1 - (turbidity / 100);
        double contaminant_score = 1 - (contaminantIndex / 100);
        int frozen_score = isFrozen ? 0 : 1;

        double water_quality = (0.3 * purity_score
                + 0.2 * pH_score
                + 0.15 * salinity_score
                + 0.1 * turbidity_score
                + 0.15 * contaminant_score
                + 0.2 * frozen_score) * 100;

        return water_quality;
    }
}
