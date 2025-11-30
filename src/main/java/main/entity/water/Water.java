package main.entity.water;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.entity.Entity;
import main.entity.air.Air;
import main.entity.soil.Soil;
import main.simulation.Simulation;

import static java.lang.Math.abs;

public class Water extends Entity {
    private static final double MAX_PURITY = 100.0;
    private static final double IDEAL_PH = 7.5;
    private static final double MAX_SALINITY = 350.0;
    private static final double MAX_TURBIDITY = 100.0;
    private static final double MAX_CONTAMINANT_INDEX = 100.0;
    private static final double PURITY_WEIGHT = 0.3;
    private static final double PH_WEIGHT = 0.2;
    private static final double SALINITY_WEIGHT = 0.15;
    private static final double TURBIDITY_WEIGHT = 0.1;
    private static final double CONTAMINANT_WEIGHT = 0.15;
    private static final double FROZEN_WEIGHT = 0.2;
    private static final double HUMIDITY_UPDATE = 0.1;
    private static final double WATER_RETENTION_UPDATE = 0.1;
    private static final int QUALITY_MULTIPLIER = 100;

    private final String type;
    private final double salinity;
    private final double pH;
    private final double purity;
    private final double turbidity;
    private final double contaminantIndex;
    private final boolean isFrozen;

    public Water(final String name, final double mass, final String type,
                 final double salinity, final double pH, final double purity,
                 final double turbidity, final double contaminantIndex, final boolean isFrozen) {
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

    /**
     * Calculates the quality of the water based on several parameters.
     * This method is safe to be overridden by subclasses.
     * @return The water quality score.
     */
    @JsonIgnore
    public double getWaterQuality() {
        double purityScore = purity / MAX_PURITY;
        double phScore = 1 - abs(pH - IDEAL_PH) / IDEAL_PH;
        double salinityScore = 1 - (salinity / MAX_SALINITY);
        double turbidityScore = 1 - (turbidity / MAX_TURBIDITY);
        double contaminantScore = 1 - (contaminantIndex / MAX_CONTAMINANT_INDEX);
        int frozenScore = isFrozen ? 0 : 1;

        return (PURITY_WEIGHT * purityScore
                + PH_WEIGHT * phScore
                + SALINITY_WEIGHT * salinityScore
                + TURBIDITY_WEIGHT * turbidityScore
                + CONTAMINANT_WEIGHT * contaminantScore
                + FROZEN_WEIGHT * frozenScore) * QUALITY_MULTIPLIER;
    }

    @JsonIgnore
    public final double getSalinity() {
        return salinity;
    }

    /**
     * Gets the pH of the water.
     * This method is safe to be overridden by subclasses.
     * @return The pH of the water.
     */
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

    /**
     * Updates the environment based on the water's properties.
     * This method is designed to be overridden by subclasses.
     * @param simulation The simulation instance.
     */
    @Override
    public void updateEnvironment(final Object simulation) {
        Simulation sim = (Simulation) simulation;
        Air air = sim.getTerritory().getAirAt(this.getPosition().getX(),
                this.getPosition().getY());
        if (air != null) {
            air.updateHumidity(HUMIDITY_UPDATE);
        }
        Soil soil = sim.getTerritory().getSoilAt(this.getPosition().getX(),
                this.getPosition().getY());
        if (soil != null) {
            soil.updateWaterRetention(WATER_RETENTION_UPDATE);
        }
    }
}
