package main.entity.animal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import main.entity.Entity;
import main.entity.Hazardous;
import main.entity.plant.Plant;
import main.entity.animal.types.*;
import main.entity.water.Water;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Carnivore.class, name = "Carnivores"),
        @JsonSubTypes.Type(value = Herbivore.class, name = "Herbivores"),
        @JsonSubTypes.Type(value = Omnivore.class, name = "Omnivores"),
        @JsonSubTypes.Type(value = Detritivore.class, name = "Detritivores"),
        @JsonSubTypes.Type(value = Parasite.class, name = "Parasites")
})
public abstract class Animal extends Entity implements Hazardous {
    private Status status = Status.hungry;
    private double attackProbability;

    public Animal(String name, double mass, double attackProbability) {
        super(name, mass);
        this.attackProbability = attackProbability;
    }

    @JsonIgnore
    public double getInteractionProbability() {
        return (100 - attackProbability) / 10.0;
    }

    public void eatAnimal(Animal animal) {
        this.status = Status.wellFed;
        this.setMass(this.getMass() + animal.getMass());
        animal.perish();
    }

    public void eatPlant(Plant plant) {
        this.status = Status.wellFed;
        this.setMass(this.getMass() + plant.getMass());
        plant.perish();
    }

    public void drinkWater(Water water) {
        double intakeRate = 0.08;
        double waterToDrink = Math.min(this.getMass() * intakeRate, water.getMass());
        water.setMass(water.getMass() - waterToDrink);
        this.setMass(this.getMass() + waterToDrink);
        this.status = Status.wellFed;
    }
}
