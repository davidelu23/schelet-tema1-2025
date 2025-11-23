package main;

public abstract class Entity {
    protected String name;
    protected double mass;
    protected boolean isScanned = false;

    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }

    public Entity() {
        this.name = "";
        this.mass = 0;
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void perish() {

    }
}

