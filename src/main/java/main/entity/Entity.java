package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.PairInput;

public abstract class Entity {
    private String name;
    private double mass;
    private boolean isScanned;
    private PairInput position;
    private int timestamp;

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

    public final void scan(PairInput position, int timestamp) {
        isScanned = true;
        this.position = position;
        this.timestamp = timestamp;
    }

    @JsonIgnore
    public final PairInput getPosition() {
        return position;
    }

    @JsonIgnore
    public final int getTimestamp() {
        return timestamp;
    }

    public final void setPosition(PairInput position) {
        this.position = position;
    }

    public void updateEnvironment(Object simulation) {}

    protected double normalizeScore(double score) {
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Math.round(normalizeScore * 100.0) / 100.0;
    }
}
