package main.entity.animal.types;

import fileio.PairInput;
import main.entity.animal.Animal;
import main.simulation.Simulation;

public class Herbivore extends Animal {
    public Herbivore(String name, double mass) {
        double attackProbability = 85;
        super(name, mass, attackProbability);
    }

    public void roam(Simulation simulation) {
        PairInput move = findBestWaterAndPlant(simulation);
        if (move != null) {
            return;
        }
        move = findBestPlant(simulation);
        if (move != null) {
            return;
        }
        move = findBestWater(simulation);
    }
}
