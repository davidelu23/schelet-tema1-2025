package main.robot;

import fileio.PairInput;
import main.entity.air.Air;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.soil.Soil;
import main.entity.water.Water;
import main.simulation.Simulation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Robot {
    private PairInput position;
    private int energy;
    private int timeToCharge;
    private static final int SCAN_ENERGY_COST = 7;
    private static final int LEARN_FACT_COST = 2;
    private static final int IMPROVE_ENVIRONMENT_COST = 10;
    private static final int MAX_SCORE = 1_000_000_000;
    private static final int DIRECTIONS = 4;

    private List<Plant> plantInventory;
    private List<Animal> animalInventory;
    private List<Water> waterInventory;
    private Map<String, List<String>> knowledgeBase;

    public Robot(final int energy) {
        position = new PairInput();
        this.energy = energy;
        timeToCharge = 0;
        plantInventory = new ArrayList<>();

        animalInventory = new ArrayList<>();
        waterInventory = new ArrayList<>();
        knowledgeBase = new LinkedHashMap<>();
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

    public static int getImproveCost() {
        return IMPROVE_ENVIRONMENT_COST;
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
     * Gets the knowledge base.
     * @return The knowledge base map.
     */
    public Map<String, List<String>> getKnowledgeBase() {
        return knowledgeBase;
    }

    /**
     * Adds a fact to the knowledge base for a given topic.
     * @param topic The topic (component) to add the fact to.
     * @param fact The fact (subject) to add.
     */
    public void addFact(final String topic, final String fact) {
        knowledgeBase.computeIfAbsent(topic, k -> new ArrayList<>()).add(fact);
    }

    /**
     * Finds a plant in the inventory by name.
     * @param name The name of the plant.
     * @return The plant if found, null otherwise.
     */
    public Plant findPlantByName(final String name) {
        return plantInventory.stream()
                .filter(plant -> plant.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds an animal in the inventory by name.
     * @param name The name of the animal.
     * @return The animal if found, null otherwise.
     */
    public Animal findAnimalByName(final String name) {
        return animalInventory.stream()
                .filter(animal -> animal.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds water in the inventory by name.
     * @param name The name of the water.
     * @return The water if found, null otherwise.
     */
    public Water findWaterByName(final String name) {
        return waterInventory.stream()
                .filter(water -> water.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a plant from the inventory.
     * @param plant The plant to remove.
     */
    public void removePlant(final Plant plant) {
        plantInventory.remove(plant);
    }

    /**
     * Removes an animal from the inventory.
     * @param animal The animal to remove.
     */
    public void removeAnimal(final Animal animal) {
        animalInventory.remove(animal);
    }

    /**
     * Removes water from the inventory.
     * @param water The water to remove.
     */
    public void removeWater(final Water water) {
        waterInventory.remove(water);
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
