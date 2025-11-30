package main.entity.plant.types;

import main.entity.plant.Plant;

public class Fern extends Plant {
    private static final double STUCK_PROBABILITY = 0.3;

    public Fern(final String name, final double mass) {
        super(name, mass, 0.0, STUCK_PROBABILITY);
    }
}
