package main.entity.animal.types;

import main.entity.animal.Animal;

public class Carnivore extends Animal {
    public Carnivore(String name, double mass) {
        double attackProbability = 0.30;
        super(name, mass, attackProbability);
    }
}
