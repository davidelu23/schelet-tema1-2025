package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.entity.animal.Status;
import main.simulation.Simulation;

public final class Parasite extends Animal {
    private static final int ATTACK_PROBABILITY = 10;

    public Parasite(final String name, final double mass) {
        super(name, mass, ATTACK_PROBABILITY);
    }

    /**
     * Defines the roaming behavior for a Parasite. It prioritizes finding another animal
     * to consume. If none is found, it will look for other resources.
     *
     * @param simulation The current state of the simulation.
     */
    public void roam(final Simulation simulation) {
        PairInput move = findBestAnimal(simulation);
        if (move != null) {
            eatAnimal(simulation.getTerritory().getAnimalAt(move.getX(), move.getY()));
            simulation.getTerritory().addAnimal(null, getPosition().getX(), getPosition().getY());
            simulation.getTerritory().addAnimal(this, getPosition().getX(), getPosition().getY());
            return;
        }
        move = findBestWaterAndPlant(simulation);
        if (move != null) {
            return;
        }
        move = findBestPlant(simulation);
        if (move != null) {
            return;
        }
        move = findBestWater(simulation);
    }

    private void eatAnimal(final Animal animal) {
        this.setMass(this.getMass() + animal.getMass());
        setStatus(Status.wellFed);
    }
}
