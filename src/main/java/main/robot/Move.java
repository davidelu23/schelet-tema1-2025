package main.robot;

import fileio.PairInput;

public class Move {
    private final int energyConsumed;
    private final PairInput input;

    public Move(PairInput input, int energyConsumed) {
        this.energyConsumed = energyConsumed;
        this.input = input;
    }

    public  int getEnergyConsumed() {
        return energyConsumed;
    }
    public PairInput getInput() {
        return input;
    }
}
