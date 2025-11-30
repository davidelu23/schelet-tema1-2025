package main.entity.plant.types;

import main.entity.plant.Plant;

public class Moss extends Plant {
    private static final double OXYGEN_LEVEL = 0.8;
    private static final double STUCK_PROBABILITY = 0.4;

    public Moss(final String name, final double mass) {
        super(name, mass, OXYGEN_LEVEL, STUCK_PROBABILITY);
    }
}
