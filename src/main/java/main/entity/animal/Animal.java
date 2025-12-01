package main.entity.animal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fileio.PairInput;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.animal.types.Carnivore;
import main.entity.animal.types.Detritivore;
import main.entity.animal.types.Herbivore;
import main.entity.animal.types.Omnivore;
import main.entity.animal.types.Parasite;
import main.entity.plant.Plant;
import main.entity.water.Water;
import main.simulation.Simulation;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Carnivore.class, name = "Carnivores"),
        @JsonSubTypes.Type(value = Herbivore.class, name = "Herbivores"),
        @JsonSubTypes.Type(value = Omnivore.class, name = "Omnivores"),
        @JsonSubTypes.Type(value = Detritivore.class, name = "Detritivores"),
        @JsonSubTypes.Type(value = Parasite.class, name = "Parasites")
})
public abstract class Animal extends Entity implements Hazardous {
    private static final int MAX_SCORE = 1_000_000;
    private static final int DIRECTIONS = 4;
    private static final double INTERACTION_PROBABILITY_NORMALIZER = 10.0;
    private static final int INTERACTION_PROBABILITY_BASE = 100;
    private static final double WATER_INTAKE_RATE = 0.08;

    private Status status;
    private double attackProbability;
    private double fertilizer;

    public Animal(final String name, final double mass, final double attackProbability) {
        super(name, mass);
        this.attackProbability = attackProbability;
        this.fertilizer = 0;
        this.status = Status.hungry;
    }

    /**
     * Calculates the probability of a safe interaction with the animal.
     *
     * @return A normalized score representing interaction safety.
     */
    @JsonIgnore
    public double getInteractionProbability() {
        return normalizeScore((INTERACTION_PROBABILITY_BASE - attackProbability)
                / INTERACTION_PROBABILITY_NORMALIZER);
    }
    @JsonIgnore
    public final Status getStatus() {
        return status;
    }

    @JsonIgnore
    public final double getFertilizer() {
        return fertilizer;
    }

    public final void setFertilizer(final double fertilizer) {
        this.fertilizer = fertilizer;
    }

    public final void setStatus(final Status status) {
        this.status = status;
    }

    /**
     * Defines the roaming behavior of the animal, which is specific to each animal type.
     *
     * @param simulation The current state of the simulation.
     */
    public abstract void roam(Simulation simulation);

    /**
     * Finds the best adjacent animal to interact with.
     *
     * @param simulation The current simulation state.
     * @return The coordinates of the best adjacent animal, or null if none are found.
     */
    public PairInput findBestAnimal(final Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            if (simulation.getTerritory().getAnimalAt(x, y) != null) {
                move.setX(x);
                move.setY(y);
                return move;
            }
        }
        return null;
    }

    /**
     * Finds the best adjacent cell containing both water and a plant.
     *
     * @param simulation The current simulation state.
     * @return The coordinates of the best cell, or null if none are found.
     */
    public PairInput findBestWaterAndPlant(final Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        double bestScore = MAX_SCORE;
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            Water water = simulation.getTerritory().getWaterAt(x, y);
            Plant plant = simulation.getTerritory().getPlantAt(x, y);
            if (water == null || plant == null) {
                continue;
            }
            if (water.isScanned() && plant.isScanned()) {
                double score = simulation.getTerritory().getWaterAt(x, y).getWaterQuality();
                if (score < bestScore) {
                    bestScore = score;
                    move.setX(x);
                    move.setY(y);
                }
            }
        }
        if (bestScore != MAX_SCORE) {
            return move;
        }
        return null;
    }

    /**
     * Finds the best adjacent water source.
     *
     * @param simulation The current simulation state.
     * @return The coordinates of the best water source, or null if none are found.
     */
    public PairInput findBestWater(final Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        double bestScore = MAX_SCORE;
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            if (simulation.getTerritory().getWaterAt(x, y) == null) {
                continue;
            }
            if (simulation.getTerritory().getWaterAt(x, y).isScanned()) {
                double score = simulation.getTerritory().getWaterAt(x, y).getWaterQuality();
                if (score < bestScore) {
                    bestScore = score;
                    move.setX(x);
                    move.setY(y);
                }
            }
        }
        if (bestScore != MAX_SCORE) {
            return move;
        }
        return null;
    }

    /**
     * Finds the best adjacent plant.
     *
     * @param simulation The current simulation state.
     * @return The coordinates of the best adjacent plant, or null if none are found.
     */
    public PairInput findBestPlant(final Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            if (simulation.getTerritory().getPlantAt(x, y) == null) {
                continue;
            }
            if (simulation.getTerritory().getPlantAt(x, y).isScanned()) {
                move.setX(x);
                move.setY(y);
                return move;
            }
        }
        return null;
    }

    /**
     * Finds any valid adjacent move if no specific target (animal, plant, water) is found.
     *
     * @param simulation The current simulation state.
     * @return The coordinates of a valid adjacent cell, or null if none are available.
     */
    public PairInput findBestRemainingMove(final Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            move.setX(x);
            move.setY(y);
            return move;
        }
        return null;
    }

    /**
     * Makes the animal drink from a water source, increasing its mass and updating its status.
     *
     * @param water The water source to drink from.
     */
    public void drinkWater(final Water water) {
        double waterToDrink = Math.min(this.getMass() * WATER_INTAKE_RATE, water.getMass());
        water.setMass(water.getMass() - waterToDrink);
        this.setMass(this.getMass() + waterToDrink);
        this.status = Status.wellFed;
    }

    /**
     * Updates the animal's state for the current simulation tick. This includes roaming if
     * hungry or fertilizing the soil if well-fed.
     *
     * @param simulation The simulation object, which will be cast to a {@link Simulation}.
     */
    @Override
    public void updateEnvironment(final Object simulation) {
        Simulation sim = (Simulation) simulation;
        if (this.status == Status.hungry) {
            this.roam(sim);
        }
        if (this.status == Status.wellFed) {
            int x = this.getPosition().getX();
            int y = this.getPosition().getY();
            main.entity.soil.Soil soil = sim.getTerritory().getSoilAt(x, y);
            if (soil != null) {
                soil.setOrganicMatter(soil.getOrganicMatter() + this.fertilizer);
            }
            this.status = Status.hungry;
        }
    }

}
