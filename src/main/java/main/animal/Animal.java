package main.animal;

import main.Entity;
import main.Hazardous;
import main.plant.Plant;

public class Animal extends Entity implements Hazardous {
    private final AnimalType animalType;
    private Status status = Status.hungry;

    public Animal(AnimalType animalType, String name, double mass) {
        super(name, mass);
        this.animalType = animalType;
    }

    public double getInteractionProbability() {
        return this.animalType.getAttackChance();
    }

    public void eatAnimal(Animal animal) {
        this.status = Status.wellFed;
        this.setMass(this.mass + animal.getMass());
        animal.perish();
    }

    public void eatPlant(Plant plant) {
        this.status = Status.wellFed;
        this.setMass(this.mass + plant.getMass());
        plant.perish();
    }

    public void drinkWater() {
        this.status = Status.wellFed;
    }
}
