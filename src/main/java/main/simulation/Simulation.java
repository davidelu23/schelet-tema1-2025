package main.simulation;

import fileio.PairInput;
import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
import main.entity.air.types.*;
import main.entity.animal.Animal;
import main.entity.animal.Status;
import main.entity.soil.Soil;
import main.robot.Robot;

public class Simulation{
    private final Territory territory;
    private final Robot robot;

    public Simulation(SimulationInput simulationInput) {
        robot = new Robot(simulationInput.getEnergyPoints());

        String[] territoryDim = simulationInput.getTerritoryDim().split("x");
        int rows = Integer.parseInt(territoryDim[0]);
        int cols = Integer.parseInt(territoryDim[1]);
        TerritorySectionParamsInput territorySectionParams = simulationInput.getTerritorySectionParams();
        this.territory = new Territory(territorySectionParams, rows, cols);
    }

    public PairInput getRobotLocation() {
        return robot.getPosition();
    }

    public final Robot getRobot() {
        return robot;
    }

    public final Territory getTerritory() {
        return territory;
    }

    private void updateEvents(int timePassed) {
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


    public void runEnviorment(int lastTimestamp, int currentTimestamp) {
        updateEvents(currentTimestamp - lastTimestamp);
        for (int i = 0; i < territory.getHeight(); i++) {
            for (int j = 0; j < territory.getWidth(); j++) {
                Animal animal = territory.getAnimalAt(i, j);
                if (animal == null) {
                    continue;
                }
                if (!animal.isScanned()) {
                    continue;
                }
                if (animal.getStatus() == Status.hungry) {
                    animal.roam(this);
                }
                if (animal.getStatus() == Status.wellFed) {
                    Soil soil = territory.getSoilAt(i, j);
                    soil.setOrganicMatter(soil.getOrganicMatter() + animal.getFertilizer());
                    animal.setStatus(Status.hungry);
                }
            }
        }
    }
}
