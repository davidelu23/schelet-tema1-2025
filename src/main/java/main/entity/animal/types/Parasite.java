package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.simulation.Simulation;

public final class Parasite extends Animal {
    private static final int ATTACK_PROBABILITY = 10;

    public Parasite(final String name, final double mass) {
        super(name, mass, ATTACK_PROBABILITY);
    }

    /**
     * Defines the roaming behavior for a Parasite. It prioritizes finding another animal
     * to consume. If none is found, it will look for other resources.
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
