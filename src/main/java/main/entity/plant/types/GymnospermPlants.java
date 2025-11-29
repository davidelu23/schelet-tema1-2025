package main.entity.plant.types;

import main.entity.plant.Plant;

public class GymnospermPlants extends Plant {
    public GymnospermPlants(String name, double mass) {
        double oxygenLevel = 0.0;
        double stuckProbability = 0.6;
        super(name, mass, oxygenLevel, stuckProbability);
    }
}
