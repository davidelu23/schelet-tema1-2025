package main.entity.plant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fileio.PairInput;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.air.Air;
import main.entity.plant.types.*;
import main.simulation.Simulation;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Algae.class, name = "Algae"),
        @JsonSubTypes.Type(value = Fern.class, name = "Ferns"),
        @JsonSubTypes.Type(value = FloweringPlant.class, name = "FloweringPlants"),
        @JsonSubTypes.Type(value = GymnospermPlants.class, name = "GymnospermsPlants"),
        @JsonSubTypes.Type(value = Moss.class, name = "Mosses")
})
public abstract class Plant extends Entity implements Hazardous {
    private double growthRate = 0;
    private Maturity maturity = Maturity.Young;
    private final double oxygenLevel;
    private final double stuckProbability;

    public Plant (String name, double mass, double oxygenLevel, double stuckProbability) {
        super(name, mass);
        this.oxygenLevel = oxygenLevel;
        this.stuckProbability = stuckProbability;
    }

    public void grow(double amount) {
        this.growthRate += amount;
        if (growthRate >= 1.0) {
            ageUp();
            this.growthRate = 0;
        }
    }

    private void ageUp() {
        switch (maturity) {
            case Young: maturity = Maturity.Mature; break;
            case Mature: maturity = Maturity.Old; break;
            case Old: maturity = Maturity.Dead; break;
        }
    }

    @JsonIgnore
    public double getOxygen() {
        return maturity.getMaturity() + oxygenLevel;
    }

    @JsonIgnore
    public double getInteractionProbability() {
        return normalizeScore(stuckProbability);
    }

    @JsonIgnore
    public Maturity getMaturity() {
        return maturity;
    }

    @Override
    public void updateEnvironment(Object simulation) {
        this.grow(0.2);
        Simulation sim = (Simulation) simulation;
        if (this.maturity == Maturity.Dead) {
            sim.getTerritory().removePlant(this.getPosition().getX(), this.getPosition().getY());
            return;
        }
        Air air = sim.getTerritory().getAirAt(this.getPosition().getX(), this.getPosition().getY());
        if (air != null) {
            air.updateOxygenLevel(this.getOxygen());
        }
    }
}
