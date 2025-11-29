package main.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.PairInput;
import main.entity.air.Air;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.soil.Soil;
import main.entity.water.Water;
import main.robot.Move;
import main.robot.Robot;
import main.simulation.exceptions.NotEnoughBatteryException;
import main.simulation.exceptions.SimulationAlreadyStartedException;
import main.simulation.exceptions.SimulationNotStartedException;

import java.io.NotActiveException;

public abstract class Commands {
    protected ObjectMapper mapper;

    protected ObjectNode startSimulation(CommandInput commandInput, Simulation simulation)
            throws SimulationAlreadyStartedException {
        if (simulation != null) {
            throw new SimulationAlreadyStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "Simulation has started.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode printEnvConditions(CommandInput commandInput, Simulation simulation)
            throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());

        PairInput position = simulation.getRobotLocation();
        int x = position.getX();
        int y = position.getY();

        ObjectNode outputNode = mapper.createObjectNode();
        Soil soil = simulation.getTerritory().getSoilAt(x, y);
        if (soil != null) {
            outputNode.set("soil", mapper.valueToTree(soil));
        }
        Plant plant = simulation.getTerritory().getPlantAt(x, y);
        if (plant != null) {
            outputNode.set("plants", mapper.valueToTree(plant));
        }
        Animal animal = simulation.getTerritory().getAnimalAt(x, y);
        if (animal != null) {
            outputNode.set("animals", mapper.valueToTree(animal));
        }
        Water water = simulation.getTerritory().getWaterAt(x, y);
        if (water != null) {
            outputNode.set("water", mapper.valueToTree(water));
        }
        Air air = simulation.getTerritory().getAirAt(x, y);
        if (air != null) {
            outputNode.set("air", mapper.valueToTree(air));
        }
        result.set("output", outputNode);
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode printMap(CommandInput commandInput, Simulation simulation)
            throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());

        ArrayNode mapNode = mapper.createArrayNode();
        Territory territory = simulation.getTerritory();
        for (int i = 0; i < territory.getHeight(); i++) {
            for (int j = 0; j < territory.getWidth(); j++) {
                ObjectNode sectionNode = mapper.createObjectNode();
                ArrayNode sectionCoords = mapper.createArrayNode();
                sectionCoords.add(j);
                sectionCoords.add(i);
                sectionNode.set("section", sectionCoords);

                Plant plant = territory.getPlantAt(j, i);
                Animal animal = territory.getAnimalAt(j, i);
                Water water = territory.getWaterAt(j, i);
                sectionNode.put("totalNrOfObjects", (plant != null ? 1 : 0) +
                                (animal != null ? 1 : 0) + (water != null ? 1 : 0));

                Air air = territory.getAirAt(j, i);
                if (air != null) {
                    sectionNode.put("airQuality", air.getAirQuality());
                } else {
                    sectionNode.put("airQuality", "not applicable");
                }

                Soil soil = territory.getSoilAt(j, i);
                if (soil != null) {
                    sectionNode.put("soilQuality", soil.getSoilQuality());
                } else {
                    sectionNode.put("soilQuality", "not applicable");
                }
                mapNode.add(sectionNode);
            }
        }

        result.set("output", mapNode);
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode moveRobot(CommandInput commandInput, Simulation simulation)
            throws SimulationNotStartedException, NotEnoughBatteryException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        Move move = robot.findBestMove(simulation);
        if (move.getEnergyConsumed() > robot.getEnergy()) {
            throw new NotEnoughBatteryException();
        }
        robot.setEnergy(robot.getEnergy() - move.getEnergyConsumed());
        robot.moveToPosition(move.getInput());
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message",
                "The robot has successfully moved to position (" + simulation.getRobotLocation().getX()
                        + ", " + simulation.getRobotLocation().getY() + ").");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode getEnergyStatus(CommandInput commandInput, Simulation simulation)
            throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "TerraBot has " + simulation.getRobot().getEnergy() + " energy points left.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode rechargeBattery(CommandInput commandInput, Simulation simulation)
            throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        robot.setTimeToCharge(commandInput.getTimeToCharge());
        robot.setEnergy(robot.getEnergy() + commandInput.getTimeToCharge());
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "Robot battery is charging.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode endSimulation(CommandInput commandInput, Simulation simulation)
            throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "Simulation has ended.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }
}
