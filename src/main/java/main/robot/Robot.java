package main.robot;

import fileio.PairInput;
import main.entity.air.Air;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.soil.Soil;
import main.entity.water.Water;
import main.simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

public final class Robot {
    private PairInput position;
    private int energy;
    private int timeToCharge;
    private static final int SCAN_ENERGY_COST = 7;
    private static final int LEARN_FACT_COST = 2;
    private static final int MAX_SCORE = 1_000_000_000;
    private static final int DIRECTIONS = 4;

    private List<Plant> plantInventory;
    private List<Animal> animalInventory;
    private List<Water> waterInventory;

    public Robot(final int energy) {
        position = new PairInput();
        this.energy = energy;
        timeToCharge = 0;
        plantInventory = new ArrayList<>();
        animalInventory = new ArrayList<>();
        waterInventory = new ArrayList<>();
    }

    public PairInput getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public void setTimeToCharge(final int timeToCharge) {
        this.timeToCharge = Math.max(timeToCharge, 0);
    }

    public int getTimeToCharge() {
        return timeToCharge;
    }

    public static int getScanCost() {
        return SCAN_ENERGY_COST;
    }

    public static int getLearnCost() {
        return LEARN_FACT_COST;
    }

    public List<Plant> getPlantInventory() {
        return plantInventory;
    }

    /**
     * Adds a plant to the robot's inventory.
     * @param plant The plant to add.
     */
    public void addToPlantInventory(final Plant plant) {
        plantInventory.add(plant);
    }

    public List<Animal> getAnimalInventory() {
        return animalInventory;
    }

    /**
     * Adds an animal to the robot's inventory.
     * @param animal The animal to add.
     */
    public void addToAnimalInventory(final Animal animal) {
        animalInventory.add(animal);
    }

    public List<Water> getWaterInventory() {
        return waterInventory;
    }

    /**
     * Adds a water source to the robot's inventory.
     * @param water The water source to add.
     */
    public void addToWaterInventory(final Water water) {
        waterInventory.add(water);
    }

    /**
     * Moves the robot to a new position.
     * @param newPosition The target position.
     */
    public void moveToPosition(final PairInput newPosition) {
        this.position = newPosition;
    }

    /**
     * Finds the best move for the robot based on the surrounding environment.
     * @param simulation The current state of the simulation.
     * @return The best move to make.
     */
    public Move findBestMove(final Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        int bestScore = MAX_SCORE;
        PairInput move = new PairInput();
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = position.getX() + idx[i];
            int y = position.getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            double sum = 0;
            int count = 0;
            Animal animal = simulation.getTerritory().getAnimalAt(x, y);
            if (animal != null) {
                sum += animal.getInteractionProbability();
                count++;
            }
            Plant plant = simulation.getTerritory().getPlantAt(x, y);
            if (plant != null) {
                sum += plant.getInteractionProbability();
                count++;
            }
            Soil soil = simulation.getTerritory().getSoilAt(x, y);
            if (soil != null) {
                sum += soil.getInteractionProbability();
                count++;
            }
            Air air = simulation.getTerritory().getAirAt(x, y);
            if (air != null) {
                sum += air.getToxicityAQ();
                count++;
            }
            double mean = Math.abs(sum / count);
            int result = (int) Math.round(mean);
            if (result < bestScore) {
                bestScore = result;
                move.setX(x);
                move.setY(y);
            }
        }
        return new Move(move, bestScore);
    }
}
