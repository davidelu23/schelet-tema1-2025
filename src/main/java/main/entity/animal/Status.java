package main.entity.animal;

public enum Status {
    hungry(0),
    wellFed(1),
    sick(0);

    private final int fertilizer;

    Status(final int fertilizer) {
        this.fertilizer = fertilizer;
    }

    public int getFertilizer() {
        return fertilizer;
    }
}
