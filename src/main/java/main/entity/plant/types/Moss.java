package main.entity.plant.types;

import main.entity.plant.Plant;

public class Moss extends Plant {
    public Moss(String name, double mass) {
        double oxygenLevel = 0.8;
        double stuckProbability = 0.4;
        super(name, mass, oxygenLevel, stuckProbability);
    }
}
