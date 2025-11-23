package main.animal;

public enum AnimalType {
    Herbivores(0.85),
    Carnivores(0.3),
    Omnivores(0.6),
    Detritivores(0.9),
    Parasites(0.1);

    private final double attackChance;

    AnimalType(double attackChance) {
        this.attackChance = attackChance;
    }

    public double getAttackChance() {
        return attackChance;
    }
}
