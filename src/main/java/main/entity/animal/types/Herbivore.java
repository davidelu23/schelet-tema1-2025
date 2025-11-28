package main.entity.animal.types;

import main.entity.animal.Animal;

public class Herbivore extends Animal {
    public Herbivore(String name, double mass) {
        double attackProbability = 0.85;
        super(name, mass, attackProbability);
    }
}
