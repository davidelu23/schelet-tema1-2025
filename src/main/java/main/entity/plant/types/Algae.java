package main.entity.plant.types;

import main.entity.plant.Plant;

public class Algae extends Plant {
    public Algae(String name, double mass) {
        double oxygenLevel = 0.5;
        double stuckProbability = 0.2;
        super(name, mass, oxygenLevel, stuckProbability);
    }
}
