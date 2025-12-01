package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class FactNotYetSavedException extends Exception implements Error {
    public FactNotYetSavedException() { }

    /**
     * Gets the error message.
     * @param mapper The object mapper.
     * @param commandInput The command input.
     * @return The error message.
     */
    @Override
    public ObjectNode getError(final ObjectMapper mapper, final CommandInput commandInput) {
        ObjectNode errorResult = mapper.createObjectNode();
        errorResult.put("command", commandInput.getCommand());
        errorResult.put("message", "ERROR: Fact not yet saved. Cannot perform action");
        errorResult.put("timestamp", commandInput.getTimestamp());
        return errorResult;
    }
}
