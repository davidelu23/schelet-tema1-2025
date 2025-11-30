package main.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import main.simulation.exceptions.*;

import java.io.NotActiveException;

public class SimulationManager extends Commands {
    private static Simulation simulation;
    private static int simIndex;

    public SimulationManager(ObjectMapper mapper) {
        this.mapper = mapper;
        simulation = null;
        simIndex = 0;
    }

    public void run(InputLoader input, ArrayNode output) {
        int lastTimestamp = 0;
        for (CommandInput commandInput : input.getCommands()) {
            ObjectNode command = null;
            if (simulation != null) {
                simulation.runEnviorment(lastTimestamp, commandInput.getTimestamp());
                simulation.getRobot().setTimeToCharge(simulation.getRobot().getTimeToCharge() - (commandInput.getTimestamp() - lastTimestamp));
            }
            try {
                command = switch (commandInput.getCommand()) {
                    case "startSimulation" -> {
                        ObjectNode aux = startSimulation(commandInput, simulation);
                        if (simulation == null) {
                            simulation = new Simulation(input.getSimulations().get(simIndex));
                        }
                        yield aux;
                    }
                    case "printEnvConditions" -> printEnvConditions(commandInput, simulation);
                    case "printMap" -> printMap(commandInput, simulation);
                    case "moveRobot" -> moveRobot(commandInput, simulation);
                    case "getEnergyStatus" -> getEnergyStatus(commandInput, simulation);
                    case "rechargeBattery" -> rechargeBattery(commandInput, simulation);
                    case "changeWeatherConditions" -> changeWeatherConditions(commandInput, simulation);
                    case "scanObject" -> scanObject(commandInput, simulation);
                    case "endSimulation" -> {
                        ObjectNode aux = endSimulation(commandInput, simulation);
                        simulation = null;
                        yield aux;
                    }
                    default -> null;
                };
            } catch (SimulationAlreadyStartedException e) {
                command = e.getError(mapper, commandInput);
            } catch (SimulationNotStartedException e) {
                command = e.getError(mapper, commandInput);
            } catch (NotEnoughBatteryException e) {
                command = e.getError(mapper, commandInput);
            } catch (RobotChargingException e) {
                command = e.getError(mapper, commandInput);
            } catch (ObjectNotFound e) {
                command = e.getError(mapper, commandInput);
            } catch (NullPointerException _) {}
            output.add(command);
            lastTimestamp = commandInput.getTimestamp();
        }
    }
}
