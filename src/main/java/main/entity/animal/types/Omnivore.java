package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.simulation.Simulation;

public class Omnivore extends Animal {
    public Omnivore(String name, double mass) {
        double attackProbability = 60;
        super(name, mass, attackProbability);
    }

    public void roam(Simulation simulation) {
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

        int oldX = this.getPosition().getX();
        int oldY = this.getPosition().getY();
        simulation.getTerritory().removeAnimal(oldX, oldY);
        this.setPosition(move);
        simulation.getTerritory().addAnimal(this, move.getX(), move.getY());
    }
}
