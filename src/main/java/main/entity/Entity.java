package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Entity {
    private String name;
    private double mass;
    private boolean isScanned;

    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
        isScanned = false;
    }

    public final String getName() {
        return name;
    }

    public final double getMass() {
        return mass;
    }

    public final void setMass(double mass) {
        this.mass = mass;
    }

    @JsonIgnore
    public boolean isScanned() {
        return isScanned;
    }

    public final void scan() {
        isScanned = true;
    }

    public void perish() {

    }

    protected double normalizeScore(double score) {
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Math.round(normalizeScore * 100.0) / 100.0;
    }
}
