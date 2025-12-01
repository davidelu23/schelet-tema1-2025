package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.simulation.Simulation;

public final class Detritivore extends Animal {
    private static final int ATTACK_PROBABILITY = 90;

    public Detritivore(final String name, final double mass) {
        super(name, mass, ATTACK_PROBABILITY);
    }

    /**
     * Defines the roaming behavior for a Detritivore. It prioritizes moving towards
     * locations with both water and plants, then just plants, then just water.
     * @param simulation The current state of the simulation.
     */
    public void roam(final Simulation simulation) {
        PairInput move = findBestWaterAndPlant(simulation);
        if (move == null) {
            move = findBestPlant(simulation);
        }
        if (move == null) {
            move = findBestWater(simulation);
        }
        if (move == null) {
            move = findBestRemainingMove(simulation);
        }
        simulation.getTerritory()
                .removeAnimal(this.getPosition().getX(), this.getPosition().getY());
        this.setPosition(move);
    }
}
