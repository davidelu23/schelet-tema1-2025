package main.entity.plant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.air.Air;
import main.entity.plant.types.Algae;
import main.entity.plant.types.Fern;
import main.entity.plant.types.FloweringPlant;
import main.entity.plant.types.GymnospermPlants;
import main.entity.plant.types.Moss;
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
    private static final double DAILY_GROWTH_AMOUNT = 0.2;
    private double growthRate = 0;
    private Maturity maturity = Maturity.Young;
    private final double oxygenLevel;
    private final double stuckProbability;

    public Plant(final String name, final double mass,
                 final double oxygenLevel, final double stuckProbability) {
        super(name, mass);
        this.oxygenLevel = oxygenLevel;
        this.stuckProbability = stuckProbability;
    }

    /**
     * Increases the plant's growth rate and ages it up if the growth threshold is met.
     * @param amount The amount to grow by.
     */
    public void grow(final double amount) {
        this.growthRate += amount;
        if (growthRate >= 1.0) {
            ageUp();
            this.growthRate = 0;
        }
    }

    private void ageUp() {
        switch (maturity) {
            case Young:
                maturity = Maturity.Mature;
                break;
            case Mature:
                maturity = Maturity.Old;
                break;
            case Old:
                maturity = Maturity.Dead;
                break;
            default:
                break;
        }
    }

    /**
     * Gets the oxygen production level of the plant based on its maturity.
     * @return The total oxygen level.
     */
    @JsonIgnore
    public double getOxygen() {
        return maturity.getMaturity() + oxygenLevel;
    }

    /**
     * Gets the probability of a robot getting stuck when interacting with this plant.
     * @return A normalized score for interaction probability.
     */
    @JsonIgnore
    public double getInteractionProbability() {
        return normalizeScore(stuckProbability);
    }

    /**
     * Gets the current maturity stage of the plant.
     * @return The maturity enum value.
     */
    @JsonIgnore
    public Maturity getMaturity() {
        return maturity;
    }

    /**
     * Updates the plant's state for the current simulation tick. This includes growth
     * and affecting the environment's oxygen level.
     *
     * @param simulation The simulation object, which will be cast to a {@link Simulation}.
     */
    @Override
    public void updateEnvironment(final Object simulation) {
        this.grow(DAILY_GROWTH_AMOUNT);
        final Simulation sim = (Simulation) simulation;
        if (this.maturity == Maturity.Dead) {
            sim.getTerritory().removePlant(this.getPosition().getX(), this.getPosition().getY());
            return;
        }
        final Air air = sim.getTerritory()
                .getAirAt(this.getPosition().getX(), this.getPosition().getY());
        if (air != null) {
            air.updateOxygenLevel(this.getOxygen());
        }
    }
}
