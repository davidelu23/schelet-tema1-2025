package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Hazardous {
    @JsonIgnore
    double getInteractionProbability();
}
