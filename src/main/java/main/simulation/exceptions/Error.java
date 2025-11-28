package main.simulation.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public interface Error {
    ObjectNode getError(ObjectMapper mapper, CommandInput commandInput);
}
