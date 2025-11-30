package main.entity.plant.types;

import main.entity.plant.Plant;

public class FloweringPlant extends Plant {
    private static final double OXYGEN_LEVEL = 6.0;
    private static final double STUCK_PROBABILITY = 0.9;

    public FloweringPlant(final String name, final double mass) {
        super(name, mass, OXYGEN_LEVEL, STUCK_PROBABILITY);
    }
}
