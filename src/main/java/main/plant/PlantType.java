package main.plant;

public enum PlantType {
    FLOWERING(6.0,0.9),
    GYMNOSPERM(0.0,0.6),
    FERN(0.0,0.3),
    MOSS(0.8,0.4),
    ALGAE(0.5,0.2);

    private final double oxygenLevel;
    private final double stuckPosibility;

    PlantType(double oxygenLevel, double stuckPosibility) {
        this.oxygenLevel = oxygenLevel;
        this.stuckPosibility = stuckPosibility;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public double getStuckPosibility() {
        return stuckPosibility;
    }
}
