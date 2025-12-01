package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public final class ObjectNotFoundException extends Exception implements Error {
    public ObjectNotFoundException() { }

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
        result.put("message", "ERROR: Object not found. Cannot perform action");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }
}
