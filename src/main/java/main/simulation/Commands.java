package main.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.PairInput;
import main.entity.Entity;
import main.entity.air.Air;
import main.entity.air.types.DesertAir;
import main.entity.air.types.MountainAir;
import main.entity.air.types.PolarAir;
import main.entity.air.types.TemperateAir;
import main.entity.air.types.TropicalAir;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.soil.Soil;
import main.entity.water.Water;
import main.robot.Move;
import main.robot.Robot;
import main.simulation.exceptions.FactNotYetSavedException;
import main.simulation.exceptions.NotEnoughBatteryException;
import main.simulation.exceptions.NotEnoughEnergyException;
import main.simulation.exceptions.ObjectNotFoundException;
import main.simulation.exceptions.RobotChargingException;
import main.simulation.exceptions.SimulationAlreadyStartedException;
import main.simulation.exceptions.SimulationNotStartedException;
import main.simulation.exceptions.SubjectNotYetSavedException;
import main.simulation.exceptions.WeatherChangeDoesNotAffectException;

import java.util.List;
import java.util.Map;

public class Commands {
    protected ObjectMapper mapper;
    private static final int SCAN_CASE_WATER = 3;
    private static final int SCAN_CASE_PLANT = 1;
    private static final int SCAN_CASE_ANIMAL = 0;
    private static final double OXYGEN_INCREASE = 0.3;
    private static final double ORGANIC_MATTER_INCREASE = 0.3;
    private static final double HUMIDITY_INCREASE = 0.2;
    private static final double MOISTURE_INCREASE = 0.2;


    /**
     * Starts the simulation.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationAlreadyStartedException if the simulation has already started.
     */
    protected ObjectNode startSimulation(final CommandInput commandInput,
                                         final Simulation simulation)
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

    /**
     * Prints the environmental conditions at the robot's location.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     */
    protected ObjectNode printEnvConditions(final CommandInput commandInput,
                                            final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        if (simulation.getRobot().getTimeToCharge() > 0) {
            throw new RobotChargingException();
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

    /**
     * Prints the map of the territory.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     */
    protected ObjectNode printMap(final CommandInput commandInput, final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        if (simulation.getRobot().getTimeToCharge() > 0) {
            throw new RobotChargingException();
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
                sectionNode.put("totalNrOfObjects", (plant != null ? 1 : 0)
                        + (animal != null ? 1 : 0) + (water != null ? 1 : 0));

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

    /**
     * Moves the robot to the best possible location.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws NotEnoughBatteryException if the robot does not have enough battery.
     * @throws RobotChargingException if the robot is charging.
     */
    protected ObjectNode moveRobot(final CommandInput commandInput, final Simulation simulation)
            throws SimulationNotStartedException, NotEnoughBatteryException,
            RobotChargingException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        if (simulation.getRobot().getTimeToCharge() > 0) {
            throw new RobotChargingException();
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
                "The robot has successfully moved to position ("
                        + simulation.getRobotLocation().getX()
                        + ", " + simulation.getRobotLocation().getY() + ").");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Gets the energy status of the robot.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     */
    protected ObjectNode getEnergyStatus(final CommandInput commandInput,
                                         final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        if (simulation.getRobot().getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "TerraBot has "
                + simulation.getRobot().getEnergy() + " energy points left.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Recharges the robot's battery.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     */
    protected ObjectNode rechargeBattery(final CommandInput commandInput,
                                         final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        robot.setTimeToCharge(commandInput.getTimeToCharge());
        robot.setEnergy(robot.getEnergy() + commandInput.getTimeToCharge());
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "Robot battery is charging.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Changes the weather conditions.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     */
    protected ObjectNode changeWeatherConditions(final CommandInput commandInput,
                                                 final Simulation simulation)
            throws SimulationNotStartedException, WeatherChangeDoesNotAffectException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        String event = commandInput.getType();
        Air air = simulation.getTerritory().getAirAt(simulation.getRobot().getPosition().getX(),
                                                    simulation.getRobot().getPosition().getY());
        switch (event) {
            case "rainfall" -> {
                if (!air.getType().equals("TropicalAir")) {
                    throw new WeatherChangeDoesNotAffectException();
                }
                TropicalAir.rainfall(commandInput.getRainfall());
                TropicalAir.changeDuration(2);
            }
            case "polarStorm" -> {
                if (!air.getType().equals("PolarAir")) {
                    throw new WeatherChangeDoesNotAffectException();
                }
                PolarAir.polarStorm(commandInput.getWindSpeed());
                PolarAir.changeDuration(2);
            }
            case "newSeason" -> {
                if (!air.getType().equals("TemperateAir")) {
                    throw new WeatherChangeDoesNotAffectException();
                }
                TemperateAir.newSeason(commandInput.getSeason());
                TemperateAir.changeDuration(2);
            }
            case "desertStorm" -> {
                if (!air.getType().equals("DesertAir")) {
                    throw new WeatherChangeDoesNotAffectException();
                }
                DesertAir.desertStorm(commandInput.isDesertStorm());
                DesertAir.changeDuration(2);
            }
            case "peopleHiking" -> {
                if (!air.getType().equals("MountainAir")) {
                    throw new WeatherChangeDoesNotAffectException();
                }
                MountainAir.peopleHiking(commandInput.getNumberOfHikers());
                MountainAir.changeDuration(2);
            }
            default -> {
                throw new WeatherChangeDoesNotAffectException();
            }
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "The weather has changed.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Scans an object at the robot's location.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     * @throws ObjectNotFoundException if the object is not found.
     * @throws NotEnoughEnergyException if the robot does not have enough battery.
     */
    protected ObjectNode scanObject(final CommandInput commandInput, final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException,
            ObjectNotFoundException, NotEnoughEnergyException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        if (Robot.getScanCost() > robot.getEnergy()) {
            throw new NotEnoughEnergyException();
        }
        int caseIdentifier = (commandInput.getColor().equals("none") ? 1 : 0)
                + (commandInput.getSound().equals("none") ? 1 : 0)
                + (commandInput.getSmell().equals("none") ? 1 : 0);
        String type = "";
        int x = simulation.getRobotLocation().getX();
        int y = simulation.getRobotLocation().getY();
        int timestamp = commandInput.getTimestamp();
        Entity object = switch (caseIdentifier) {
            case SCAN_CASE_WATER -> {
                type = "water";
                Water water = simulation.getTerritory().getWaterAt(x, y);
                if (water != null) {
                    water.scan(robot.getPosition(), timestamp);
                    robot.addToWaterInventory(water);
                }
                yield water;
            }
            case SCAN_CASE_PLANT -> {
                type = "plant";
                Plant plant = simulation.getTerritory().getPlantAt(x, y);
                if (plant != null) {
                    plant.scan(robot.getPosition(), timestamp);
                    robot.addToPlantInventory(plant);
                }
                yield plant;
            }
            case SCAN_CASE_ANIMAL -> {
                type = "animal";
                Animal animal = simulation.getTerritory().getAnimalAt(x, y);
                if (animal != null) {
                    animal.scan(robot.getPosition(), timestamp);
                    robot.addToAnimalInventory(animal);
                }
                yield animal;
            }
            default -> null;
        };
        if (object == null) {
            throw new ObjectNotFoundException();
        }
        robot.setEnergy(robot.getEnergy() - Robot.getScanCost());
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        String article = type.equals("water") ? "" : (type.equals("animal") ? "an " : "a ");
        result.put("message", "The scanned object is " + article + type + ".");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Learns a fact about an object.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     * @throws NotEnoughBatteryException if the robot does not have enough battery.
     */
    protected ObjectNode learnFact(final CommandInput commandInput, final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException,
            NotEnoughBatteryException, SubjectNotYetSavedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        if (Robot.getLearnCost() > robot.getEnergy()) {
            throw new NotEnoughBatteryException();
        }
        String topic = commandInput.getComponents();
        String fact = commandInput.getSubject();
        Plant plant = robot.findPlantByName(topic);
        Animal animal = robot.findAnimalByName(topic);
        Water water = robot.findWaterByName(topic);
        if (plant == null && animal == null && water == null) {
            throw new SubjectNotYetSavedException();
        }
        robot.setEnergy(robot.getEnergy() - Robot.getLearnCost());
        robot.addFact(topic, fact);
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "The fact has been successfully saved in the database.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Prints the knowledge base of the robot.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     */
    protected ObjectNode printKnowledgeBase(final CommandInput commandInput,
                                            final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        ArrayNode outputArray = mapper.createArrayNode();
        for (Map.Entry<String, List<String>> entry : robot.getKnowledgeBase().entrySet()) {
            ObjectNode topicNode = mapper.createObjectNode();
            topicNode.put("topic", entry.getKey());
            ArrayNode factsArray = mapper.createArrayNode();
            for (String fact : entry.getValue()) {
                factsArray.add(fact);
            }
            topicNode.set("facts", factsArray);
            outputArray.add(topicNode);
        }
        result.set("output", outputArray);
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Improves the environment by planting vegetation, fertilizing soil,
     * or increasing humidity/moisture.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     * @throws NotEnoughBatteryException if the robot does not have enough battery.
     */
    protected ObjectNode improveEnvironment(final CommandInput commandInput,
                                            final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException, NotEnoughBatteryException,
            SubjectNotYetSavedException, FactNotYetSavedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        if (Robot.getImproveCost() > robot.getEnergy()) {
            throw new NotEnoughBatteryException();
        }
        String componentName = commandInput.getName();
        String improvementType = commandInput.getImprovementType();
        Plant plant = robot.findPlantByName(componentName);
        Animal animal = robot.findAnimalByName(componentName);
        Water water = robot.findWaterByName(componentName);
        if (plant == null && animal == null && water == null) {
            throw new SubjectNotYetSavedException();
        }
        if (!robot.getKnowledgeBase().containsKey(componentName)) {
            throw new FactNotYetSavedException();
        }
        robot.setEnergy(robot.getEnergy() - Robot.getImproveCost());
        String message = "";
        int x = robot.getPosition().getX();
        int y = robot.getPosition().getY();
        switch (improvementType) {
            case "plantVegetation" -> {
                Air air = simulation.getTerritory().getAirAt(x, y);
                if (air != null) {
                    air.updateOxygenLevel(OXYGEN_INCREASE);
                }
                message = "The " + componentName + " was planted successfully.";
            }
            case "fertilizeSoil" -> {
                Soil soil = simulation.getTerritory().getSoilAt(x, y);
                if (soil != null) {
                    soil.setOrganicMatter(soil.getOrganicMatter() + ORGANIC_MATTER_INCREASE);
                }
                message = "The soil was successfully fertilized using " + componentName;
            }
            case "increaseHumidity" -> {
                Air air = simulation.getTerritory().getAirAt(x, y);
                if (air != null) {
                    air.updateHumidity(HUMIDITY_INCREASE);
                }
                message = "The humidity was successfully increased using " + componentName;
            }
            case "increaseMoisture" -> {
                Soil soil = simulation.getTerritory().getSoilAt(x, y);
                if (soil != null) {
                    soil.updateWaterRetention(MOISTURE_INCREASE);
                }
                message = "The moisture was successfully increased using " + componentName;
            }
            default -> {
            }
        }

        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", message);
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Ends the simulation.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     */
    protected ObjectNode endSimulation(final CommandInput commandInput,
                                       final Simulation simulation)
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
