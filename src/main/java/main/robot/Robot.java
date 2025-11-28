package main.robot;

import fileio.PairInput;

public class Robot {
    private PairInput position;

    public Robot() {
        position = new PairInput();
    }

    public PairInput getPosition() {
        return position;
    }

    public void moveUp() {
        position.setY(position.getY() + 1);
    }

    public void moveDown() {
        position.setY(position.getY() - 1);
    }

    public void moveLeft() {
        position.setX(position.getX() - 1);
    }

    public void moveRight() {
        position.setX(position.getX() + 1);
    }
}
