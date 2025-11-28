package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class SimulationAlreadyStartedException extends Exception implements Error {
    public SimulationAlreadyStartedException() {}

    @Override
    public ObjectNode getError(ObjectMapper mapper, CommandInput commandInput) {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "ERROR: Simulation already started. Cannot perform action");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }
}