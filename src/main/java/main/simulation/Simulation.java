package main.simulation;

import fileio.PairInput;
import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
import main.entity.air.types.DesertAir;
import main.entity.air.types.MountainAir;
import main.entity.air.types.PolarAir;
import main.entity.air.types.TemperateAir;
import main.entity.air.types.TropicalAir;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.water.Water;
import main.robot.Robot;

public final class Simulation {
    private final Territory territory;
    private final Robot robot;

    public Simulation(final SimulationInput simulationInput) {
        robot = new Robot(simulationInput.getEnergyPoints());

        String[] territoryDim = simulationInput.getTerritoryDim().split("x");
        int rows = Integer.parseInt(territoryDim[0]);
        int cols = Integer.parseInt(territoryDim[1]);
        TerritorySectionParamsInput territorySectionParams =
                simulationInput.getTerritorySectionParams();
        this.territory = new Territory(territorySectionParams, rows, cols);
    }

    /**
     * Gets the robot's location.
     * This method is safe to be overridden by subclasses.
     * @return The robot's location.
     */
    public PairInput getRobotLocation() {
        return robot.getPosition();
    }

    public Robot getRobot() {
        return robot;
    }

    public Territory getTerritory() {
        return territory;
    }

    private void updateEvents(final int timePassed) {
        MountainAir.changeDuration(MountainAir.currentDuration() - timePassed);
        DesertAir.changeDuration(DesertAir.currentDuration() - timePassed);
        TemperateAir.changeDuration(TemperateAir.currentDuration() - timePassed);
        PolarAir.changeDuration(PolarAir.currentDuration() - timePassed);
        TropicalAir.changeDuration(TropicalAir.currentDuration() - timePassed);
        if (MountainAir.currentDuration() == 0) {
            MountainAir.peopleHiking(0);
        }
        if (DesertAir.currentDuration() == 0) {
            DesertAir.desertStorm(false);
        }
        if (TemperateAir.currentDuration() == 0) {
            TemperateAir.newSeason("");
        }
        if (PolarAir.currentDuration() == 0) {
            PolarAir.polarStorm(0);
        }
        if (TropicalAir.currentDuration() == 0) {
            TropicalAir.rainfall(0);
        }
    }


    /**
     * Runs the environment simulation.
     * This method is safe to be overridden by subclasses.
     * @param lastTimestamp The last timestamp.
     * @param currentTimestamp The current timestamp.
     */
    public void runEnviorment(final int lastTimestamp, final int currentTimestamp) {
        updateEvents(currentTimestamp - lastTimestamp);

        for (Water water : robot.getWaterInventory()) {
            if (water.getTimestamp() % 2 == currentTimestamp % 2) {
                water.updateEnvironment(this);
            }
        }
        for (Plant plant : robot.getPlantInventory()) {
            plant.updateEnvironment(this);
        }
        for (Animal animal : robot.getAnimalInventory()) {
            if (animal.getTimestamp() % 2 == currentTimestamp % 2) {
                animal.updateEnvironment(this);
            }
        }
    }
}
