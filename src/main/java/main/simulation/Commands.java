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
import main.simulation.exceptions.NotEnoughBatteryException;
import main.simulation.exceptions.ObjectNotFound;
import main.simulation.exceptions.RobotChargingException;
import main.simulation.exceptions.SimulationAlreadyStartedException;
import main.simulation.exceptions.SimulationNotStartedException;

public class Commands {
    protected ObjectMapper mapper;

    /**
     * Starts the simulation.
     * This method is safe to be overridden by subclasses.
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
     * This method is safe to be overridden by subclasses.
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
     * This method is safe to be overridden by subclasses.
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
     * This method is safe to be overridden by subclasses.
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
     * This method is safe to be overridden by subclasses.
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
     * This method is safe to be overridden by subclasses.
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
     * This method is safe to be overridden by subclasses.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     */
    protected ObjectNode changeWeatherConditions(final CommandInput commandInput,
                                                 final Simulation simulation)
            throws SimulationNotStartedException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        String event = commandInput.getType();
        switch (event) {
            case "rainfall" -> {
                TropicalAir.rainfall(commandInput.getRainfall());
                TropicalAir.changeDuration(2);
            }
            case "polarStorm" -> {
                PolarAir.polarStorm(commandInput.getWindSpeed());
                PolarAir.changeDuration(2);
            }
            case "newSeason" -> {
                TemperateAir.newSeason(commandInput.getSeason());
                TemperateAir.changeDuration(2);
            }
            case "desertStorm" -> {
                DesertAir.desertStorm(commandInput.isDesertStorm());
                DesertAir.changeDuration(2);
            }
            case "peopleHiking" -> {
                MountainAir.peopleHiking(commandInput.getNumberOfHikers());
                MountainAir.changeDuration(2);
            }
            default -> {
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
     * This method is safe to be overridden by subclasses.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     * @throws ObjectNotFound if the object is not found.
     * @throws NotEnoughBatteryException if the robot does not have enough battery.
     */
    protected ObjectNode scanObject(final CommandInput commandInput, final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException,
            ObjectNotFound, NotEnoughBatteryException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        if (Robot.getScanCost() > robot.getEnergy()) {
            throw new NotEnoughBatteryException();
        }
        String type = commandInput.getColor() + commandInput.getSmell() + commandInput.getSound();
        int x = simulation.getRobotLocation().getX();
        int y = simulation.getRobotLocation().getY();
        int timestamp = commandInput.getTimestamp();
        Entity object = switch (type) {
            case "nonenonenone" -> {
                type = "water";
                Water water = simulation.getTerritory().getWaterAt(x, y);
                if (water != null) {
                    water.scan(robot.getPosition(), timestamp);
                    robot.addToWaterInventory(water);
                }
                yield water;
            }
            case "pinksweetnone" -> {
                type = "plant";
                Plant plant = simulation.getTerritory().getPlantAt(x, y);
                if (plant != null) {
                    plant.scan(robot.getPosition(), timestamp);
                    robot.addToPlantInventory(plant);
                }
                yield plant;
            }
            case "brownearthymuu" -> {
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
            throw new ObjectNotFound();
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
     * This method is safe to be overridden by subclasses.
     * @param commandInput The command input.
     * @param simulation The simulation instance.
     * @return The result of the command.
     * @throws SimulationNotStartedException if the simulation has not started.
     * @throws RobotChargingException if the robot is charging.
     * @throws NotEnoughBatteryException if the robot does not have enough battery.
     */
    protected ObjectNode learnFact(final CommandInput commandInput, final Simulation simulation)
            throws SimulationNotStartedException, RobotChargingException,
            NotEnoughBatteryException {
        if (simulation == null) {
            throw new SimulationNotStartedException();
        }
        Robot robot = simulation.getRobot();
        if (robot.getTimeToCharge() > 0) {
            throw new RobotChargingException();
        }
        if (Robot.getScanCost() > robot.getEnergy()) {
            throw new NotEnoughBatteryException();
        }
        robot.setEnergy(robot.getEnergy() - Robot.getLearnCost());
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "The fact has been successfully saved in the database.");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }

    /**
     * Ends the simulation.
     * This method is safe to be overridden by subclasses.
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
