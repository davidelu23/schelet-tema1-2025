package main.entity.plant.types;

import main.entity.plant.Plant;

public final class GymnospermPlants extends Plant {
    private static final double OXYGEN_LEVEL = 0.0;
    private static final double STUCK_PROBABILITY = 0.6;

    public GymnospermPlants(final String name, final double mass) {
        super(name, mass, OXYGEN_LEVEL, STUCK_PROBABILITY);
    }
}
