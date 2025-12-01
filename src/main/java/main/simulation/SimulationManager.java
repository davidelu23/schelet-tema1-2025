package main.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import main.simulation.exceptions.FactNotYetSavedException;
import main.simulation.exceptions.NotEnoughBatteryException;
import main.simulation.exceptions.NotEnoughEnergyException;
import main.simulation.exceptions.ObjectNotFoundException;
import main.simulation.exceptions.RobotChargingException;
import main.simulation.exceptions.SimulationAlreadyStartedException;
import main.simulation.exceptions.SimulationNotStartedException;
import main.simulation.exceptions.SubjectNotYetSavedException;
import main.simulation.exceptions.WeatherChangeDoesNotAffectException;

public final class SimulationManager extends Commands {
    private static Simulation simulation;
    private static int simIndex;

    public SimulationManager(final ObjectMapper mapper) {
        this.mapper = mapper;
        simulation = null;
        simIndex = 0;
    }

    /**
     * Runs the simulation.
     * This method is safe to be overridden by subclasses.
     * @param input The input loader.
     * @param output The output node.
     */
    public void run(final InputLoader input, final ArrayNode output) {
        int lastTimestamp = 0;
        for (CommandInput commandInput : input.getCommands()) {
            ObjectNode command = null;
            if (simulation != null) {
                simulation.runEnviorment(lastTimestamp, commandInput.getTimestamp());
                simulation.getRobot().setTimeToCharge(simulation.getRobot().getTimeToCharge()
                        - (commandInput.getTimestamp() - lastTimestamp));
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
                    case "changeWeatherConditions" ->
                            changeWeatherConditions(commandInput, simulation);
                    case "scanObject" -> scanObject(commandInput, simulation);
                    case "learnFact" -> learnFact(commandInput, simulation);
                    case "printKnowledgeBase" -> printKnowledgeBase(commandInput, simulation);
                    case "improveEnvironment" -> improveEnvironment(commandInput, simulation);
                    case "endSimulation" -> {
                        ObjectNode aux = endSimulation(commandInput, simulation);
                        simulation = null;
                        if (input.getSimulations().get(simIndex)
                                != input.getSimulations().getLast()) {
                            simIndex++;
                        }
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
            } catch (ObjectNotFoundException e) {
                command = e.getError(mapper, commandInput);
            } catch (SubjectNotYetSavedException e) {
                command = e.getError(mapper, commandInput);
            } catch (FactNotYetSavedException e) {
                command = e.getError(mapper, commandInput);
            } catch (NotEnoughEnergyException e) {
                command = e.getError(mapper, commandInput);
            } catch (WeatherChangeDoesNotAffectException e) {
                command = e.getError(mapper, commandInput);
            } catch (NullPointerException _) { }
            output.add(command);
            lastTimestamp = commandInput.getTimestamp();
        }
    }
}
