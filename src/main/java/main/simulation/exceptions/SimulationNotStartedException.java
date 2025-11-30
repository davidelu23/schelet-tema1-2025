package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public final class SimulationNotStartedException extends Exception implements Error {
    public SimulationNotStartedException() { }

    /**
     * Gets the error message.
     * This method is safe to be overridden by subclasses.
     * @param mapper The object mapper.
     * @param commandInput The command input.
     * @return The error message.
     */
    @Override
    public ObjectNode getError(final ObjectMapper mapper, final CommandInput commandInput) {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "ERROR: Simulation not started. Cannot perform action");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }
}
