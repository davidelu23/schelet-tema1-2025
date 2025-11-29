package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.entity.animal.Status;
import main.simulation.Simulation;

public class Parasite extends Animal {
    public Parasite(String name, double mass) {
        double attackProbability = 10;
        super(name, mass, attackProbability);
    }

    public void roam(Simulation simulation) {
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
        if (move != null) {
            return;
        }
    }

    private void eatAnimal(Animal animal) {
        this.setMass(this.getMass() + animal.getMass());
        setStatus(Status.wellFed);
    }
}
