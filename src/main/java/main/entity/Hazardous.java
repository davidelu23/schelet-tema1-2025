package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Hazardous {
    /**
     * Calculates the probability of a hazardous interaction.
     * @return The interaction probability.
     */
    @JsonIgnore
    double getInteractionProbability();
}
