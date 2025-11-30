package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.PairInput;

public abstract class Entity {
    private static final int MAX_SCORE_INT = 100;
    private static final double MAX_SCORE_DOUBLE = 100.0;

    private String name;
    private double mass;
    private boolean isScanned;
    private PairInput position;
    private int timestamp;

    public Entity(final String name, final double mass) {
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

    public final void setMass(final double mass) {
        this.mass = mass;
    }

    /**
     * Checks if the entity has been scanned.
     * This method is safe to be overridden by subclasses.
     * @return true if the entity has been scanned, false otherwise.
     */
    @JsonIgnore
    public boolean isScanned() {
        return isScanned;
    }

    /**
     * Marks the entity as scanned and records its position and the timestamp of the scan.
     * @param scanPosition The position where the entity was scanned.
     * @param scanTimestamp The timestamp of the scan.
     */
    public final void scan(final PairInput scanPosition, final int scanTimestamp) {
        isScanned = true;
        this.position = scanPosition;
        this.timestamp = scanTimestamp;
    }

    @JsonIgnore
    public final PairInput getPosition() {
        return position;
    }

    @JsonIgnore
    public final int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the position of the entity.
     * @param position The new position of the entity.
     */
    public final void setPosition(final PairInput position) {
        this.position = position;
    }

    /**
     * Updates the entity's state in the simulation.
     * This method is designed to be overridden by subclasses.
     * @param simulation The simulation instance.
     */
    public void updateEnvironment(final Object simulation) { }

    /**
     * Normalizes a score to a value between 0 and 100, rounded to two decimal places.
     * This method is safe to be overridden by subclasses.
     * @param score The score to be normalized.
     * @return The normalized score.
     */
    protected double normalizeScore(final double score) {
        double normalizedScore = Math.max(0, Math.min(MAX_SCORE_INT, score));
        return Math.round(normalizedScore * MAX_SCORE_DOUBLE) / MAX_SCORE_DOUBLE;
    }
}
