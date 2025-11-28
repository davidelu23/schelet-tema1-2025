package main.entity.plant.types;

import main.entity.plant.Plant;

public class Fern extends Plant {
    public Fern(String name, double mass) {
        double oxygenLevel = 0.0;
        double stuckProbability = 0.3;
        super(name, mass, oxygenLevel, stuckProbability);
    }
}
