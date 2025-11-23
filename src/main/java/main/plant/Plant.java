package main.plant;

import main.Entity;
import main.Hazardous;

public class Plant extends Entity implements Hazardous {
    private final PlantType plantType;
    private double growthRate = 0;
    private Maturity maturity = Maturity.Young;

    public Plant (PlantType plantType, String name, double mass) {
        super(name, mass);
        this.plantType = plantType;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public void grow(double amount) {
        this.growthRate += amount;
        if (growthRate > 0) {
            ageUp();
            this.growthRate = 0;
        }
    }

    private void ageUp() {
        switch (maturity) {
            case Young: maturity = Maturity.Mature; break;
            case Mature: maturity = Maturity.Old; break;
            case Old: maturity = Maturity.Dead; break;
        }
    }

    public double getOxygen() {
        return plantType.getOxygenLevel() + maturity.getMaturity();
    }

    @Override
    public double getInteractionProbability() {
        return plantType.getStuckPosibility();
    }
}
