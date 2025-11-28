package main.entity.plant.types;

import main.entity.plant.Plant;

public class FloweringPlant extends Plant {
    public FloweringPlant(String name, double mass) {
        double oxygenLevel = 6.0;
        double stuckProbability = 0.9;
        super(name, mass, oxygenLevel, stuckProbability);
    }
}
