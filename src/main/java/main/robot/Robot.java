package main.robot;

import fileio.PairInput;
import main.entity.air.Air;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.soil.Soil;
import main.simulation.Simulation;

import java.awt.*;

public class Robot {
    private PairInput position;
    private int energy;
    private int timeToCharge;

    public Robot(int energy) {
        position = new PairInput();
        this.energy = energy;
        timeToCharge = 0;
    }

    public final PairInput getPosition() {
        return position;
    }

    public final int getEnergy() {
        return energy;
    }

    public final void setEnergy(int energy) {
        this.energy = energy;
    }

    public final void setTimeToCharge(int timeToCharge) {
        this.timeToCharge = timeToCharge;
    }

    public final int getTimeToCharge() {
        return timeToCharge;
    }

    public void moveToPosition(PairInput position) {
        this.position = position;
    }

    public Move findBestMove(Simulation simulation) {
        int[] idx = {0, 1, 0, -1};
        int[] idy = {1, 0, -1, 0};
        int bestScore = 1000000000;
        PairInput move = new PairInput();
        for (int i = 0; i < 4; i++) {
            int x = position.getX() + idx[i];
            int y = position.getY() + idy[i];
            if (x < 0 || x >= simulation.getTerritory().getWidth()) {
                continue;
            }
            if (y < 0 || y >= simulation.getTerritory().getHeight()) {
                continue;
            }
            double sum = 0;
            int count = 0;
            Animal animal = simulation.getTerritory().getAnimalAt(x, y);
            if (animal != null) {
                sum += animal.getInteractionProbability();
                count++;
            }
            Plant plant = simulation.getTerritory().getPlantAt(x, y);
            if (plant != null) {
                sum += plant.getInteractionProbability();
                count++;
            }
            Soil soil = simulation.getTerritory().getSoilAt(x, y);
            if (soil != null) {
                sum += soil.getInteractionProbability();
                count++;
            }
            Air air = simulation.getTerritory().getAirAt(x, y);
            if (air != null) {
                sum += air.getToxicityAQ();
                count++;
            }
            double mean = Math.abs(sum / count);
            int result = (int) Math.round(mean);
            if (result < bestScore) {
                bestScore = result;
                move.setX(x);
                move.setY(y);
            }
        }
        return new Move(move, bestScore);
    }
}
