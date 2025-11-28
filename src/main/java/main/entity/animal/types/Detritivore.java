package main.entity.animal.types;

import main.entity.animal.Animal;

public class Detritivore extends Animal {
    public Detritivore(String name, double mass) {
        double attackProbability = 0.90;
        super(name, mass, attackProbability);
    }
}
