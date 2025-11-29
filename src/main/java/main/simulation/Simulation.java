package main.simulation;

import fileio.PairInput;
import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
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

    public void runEnviorment() {
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
