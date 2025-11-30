package main.robot;

import fileio.PairInput;

public final class Move {
    private final int energyConsumed;
    private final PairInput input;

    public Move(final PairInput input, final int energyConsumed) {
        this.energyConsumed = energyConsumed;
        this.input = input;
    }

    public int getEnergyConsumed() {
        return energyConsumed;
    }

    public PairInput getInput() {
        return input;
    }
}
