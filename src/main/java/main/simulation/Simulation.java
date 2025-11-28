package main.simulation;

import fileio.PairInput;
import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
import main.robot.Robot;

public class Simulation{
    private final Territory territory;
    private final Robot robot;
    private int energyPoints;

    public Simulation(SimulationInput simulationInput) {
        this.energyPoints = simulationInput.getEnergyPoints();
        robot = new Robot();

        String[] territoryDim = simulationInput.getTerritoryDim().split("x");
        int rows = Integer.parseInt(territoryDim[0]);
        int cols = Integer.parseInt(territoryDim[1]);
        TerritorySectionParamsInput territorySectionParams = simulationInput.getTerritorySectionParams();
        this.territory = new Territory(territorySectionParams, rows, cols);
    }

    public PairInput getRobotLocation() {
        return robot.getPosition();
    }

    public final Territory getTerritory() {
        return territory;
    }

    public final int getEnergyPoints() {
        return energyPoints;
    }

    public final void setEnergyPoints(int newEnergyPoints) {
        this.energyPoints = newEnergyPoints;
    }
}
