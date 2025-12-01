package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class NotEnoughEnergyException extends Exception implements Error {
    public NotEnoughEnergyException() { }

    /**
     * Gets the error message.
     * @param mapper The object mapper.
     * @param commandInput The command input.
     * @return The error message.
     */
    @Override
    public ObjectNode getError(final ObjectMapper mapper, final CommandInput commandInput) {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "ERROR: Not enough energy to perform action");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }
}
