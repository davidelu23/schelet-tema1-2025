package main.entity.animal.types;

import main.entity.animal.Animal;

public class Omnivore extends Animal {
    public Omnivore(String name, double mass) {
        double attackProbability = 0.60;
        super(name, mass, attackProbability);
    }
}
