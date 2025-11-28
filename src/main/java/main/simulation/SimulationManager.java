package main.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import main.simulation.exceptions.SimulationAlreadyStartedException;
import main.simulation.exceptions.SimulationNotStartedException;

public class SimulationManager extends Commands {
    private static Simulation simulation;
    private static int simIndex;

    public SimulationManager(ObjectMapper mapper) {
        this.mapper = mapper;
        simulation = null;
        simIndex = 0;
    }

    public void run(InputLoader input, ArrayNode output) {
        for (CommandInput commandInput : input.getCommands()) {
            ObjectNode command = null;
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
            } catch (NullPointerException _) {

            }
            output.add(command);
        }
    }
}
