package main.plant;

public enum Maturity {
    Young(0.4),
    Mature(0.7),
    Old(0.4),
    Dead(0.0);

    private final double maturity;

    Maturity(double maturity) {
        this.maturity = maturity;
    }

    public double getMaturity() {
        return maturity;
    }
}
