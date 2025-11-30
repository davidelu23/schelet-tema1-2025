package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class ObjectNotFound extends Exception implements Error {
    public ObjectNotFound() {}

    @Override
    public ObjectNode getError(ObjectMapper mapper, CommandInput commandInput) {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", commandInput.getCommand());
        result.put("message", "ERROR: Object not found. Cannot perform action");
        result.put("timestamp", commandInput.getTimestamp());
        return result;
    }
}
