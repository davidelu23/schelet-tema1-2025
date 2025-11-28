package main.entity.animal.types;

import main.entity.animal.Animal;

public class Parasite extends Animal {
    public Parasite(String name, double mass) {
        double attackProbability = 0.10;
        super(name, mass, attackProbability);
    }
}
