package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.simulation.Simulation;

public final class Carnivore extends Animal {
    private static final int ATTACK_PROBABILITY = 30;

    public Carnivore(final String name, final double mass) {
        super(name, mass, ATTACK_PROBABILITY);
    }

    /**
     * Defines the roaming behavior for a Carnivore. It prioritizes finding another animal
     * to eat. If no animal is found, it will search for other resources.
     * @param simulation The current state of the simulation.
     */
    public void roam(final Simulation simulation) {
        PairInput move = findBestAnimal(simulation);
        if (move == null) {
            move = findBestWaterAndPlant(simulation);
        }
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
