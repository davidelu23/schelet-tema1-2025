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
import main.simulation.exceptions.SimulationAlreadyStartedException;
import main.simulation.exceptions.SimulationNotStartedException;

public abstract class Commands {
    protected ObjectMapper mapper;

    protected ObjectNode startSimulation(CommandInput commandInput, Simulation simulation) throws SimulationAlreadyStartedException {
        if (simulation != null) {
            throw new SimulationAlreadyStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "Simulation has started.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode printEnvConditions(CommandInput commandInput, Simulation simulation) throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());

        PairInput position = simulation.getRobotLocation();
        int x = position.getX();
        int y = position.getY();

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.set("soil", mapper.valueToTree(simulation.getTerritory().getSoilAt(x, y)));
        outputNode.set("plants", mapper.valueToTree(simulation.getTerritory().getPlantAt(x, y)));
        outputNode.set("animals", mapper.valueToTree(simulation.getTerritory().getAnimalAt(x, y)));
        outputNode.set("water", mapper.valueToTree(simulation.getTerritory().getWaterAt(x, y)));
        outputNode.set("air", mapper.valueToTree(simulation.getTerritory().getAirAt(x, y)));

        result.set("output", outputNode);
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    protected ObjectNode printMap(CommandInput commandInput, Simulation simulation) throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());

        ArrayNode mapNode = mapper.createArrayNode();
        Territory territory = simulation.getTerritory();
        for (int j = 0; j < territory.getWidth(); j++) {
            for (int i = 0; i < territory.getHeight(); i++) {
                ObjectNode sectionNode = mapper.createObjectNode();
                ArrayNode sectionCoords = mapper.createArrayNode();
                sectionCoords.add(i);
                sectionCoords.add(j);
                sectionNode.set("section", sectionCoords);

                Plant plant = territory.getPlantAt(i, j);
                Animal animal = territory.getAnimalAt(i, j);
                Water water = territory.getWaterAt(i, j);
                sectionNode.put("totalNrOfObjects", (plant != null ? 1 : 0) + (animal != null ? 1 : 0) + (water != null ? 1 : 0));

                Air air = territory.getAirAt(i, j);
                if (air != null) {
                    sectionNode.put("airQuality", air.getAirQuality());
                } else {
                    sectionNode.put("airQuality", "not applicable");
                }

                Soil soil = territory.getSoilAt(i, j);
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

    protected ObjectNode endSimulation(CommandInput commandInput, Simulation simulation) throws SimulationNotStartedException {
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
