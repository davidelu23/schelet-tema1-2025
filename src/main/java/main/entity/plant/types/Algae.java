package main.entity.plant.types;

import main.entity.plant.Plant;

public class Algae extends Plant {
    private static final double OXYGEN_LEVEL = 0.5;
    private static final double STUCK_PROBABILITY = 0.2;

    public Algae(final String name, final double mass) {
        super(name, mass, OXYGEN_LEVEL, STUCK_PROBABILITY);
    }
}
