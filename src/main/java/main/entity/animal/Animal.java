package main.entity.animal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fileio.PairInput;
import fileio.SimulationInput;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.plant.Plant;
import main.entity.animal.types.*;
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
    private Status status;
    private double attackProbability;
    private double fertilizer;

    public Animal(String name, double mass, double attackProbability) {
        super(name, mass);
        this.attackProbability = attackProbability;
        this.fertilizer = 0;
        this.status = Status.hungry;
    }

    @JsonIgnore
    public double getInteractionProbability() {
        return (100 - attackProbability) / 10.0;
    }

    @JsonIgnore
    public final Status getStatus() {
        return status;
    }

    @JsonIgnore
    public final double getFertilizer() {
        return fertilizer;
    }

    public final void setFertilizer(double fertilizer) {
        this.fertilizer = fertilizer;
    }

    public final void setStatus(Status status) {
        this.status = status;
    }

    public abstract void roam(Simulation simulation);

    public PairInput findBestAnimal(Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        for (int i = 0; i < 4; i++) {
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

    public PairInput findBestWaterAndPlant(Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        double bestScore = -1;
        for (int i = 0; i < 4; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            if (simulation.getTerritory().getWaterAt(x, y) != null && simulation.getTerritory().getPlantAt(x, y) != null) {
                double score = simulation.getTerritory().getWaterAt(x, y).getWaterQuality();
                if (score > bestScore) {
                    bestScore = score;
                    move.setX(x);
                    move.setY(y);
                }
            }
        }
        if (bestScore >= 0) {
            return move;
        }
        return null;
    }

    public PairInput findBestWater(Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        double bestScore = -1;
        for (int i = 0; i < 4; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            if (simulation.getTerritory().getWaterAt(x, y) != null) {
                double score = simulation.getTerritory().getWaterAt(x, y).getWaterQuality();
                if (score > bestScore) {
                    bestScore = score;
                    move.setX(x);
                    move.setY(y);
                }
            }
        }
        if (bestScore >= 0) {
            return move;
        }
        return null;
    }

    public PairInput findBestPlant(Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        for (int i = 0; i < 4; i++) {
            int x = this.getPosition().getX() + idx[i];
            int y = this.getPosition().getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            if (simulation.getTerritory().getPlantAt(x, y) != null) {
                move.setX(x);
                move.setY(y);
                return move;
            }
        }
        return null;
    }

    public PairInput findBestRemainingMove(Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        PairInput move = new PairInput();
        for (int i = 0; i < 4; i++) {
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

    public void drinkWater(Water water) {
        double intakeRate = 0.08;
        double waterToDrink = Math.min(this.getMass() * intakeRate, water.getMass());
        water.setMass(water.getMass() - waterToDrink);
        this.setMass(this.getMass() + waterToDrink);
        this.status = Status.wellFed;
    }

    @Override
    public void updateEnvironment(Object simulation) {
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
