package coreGame;

import java.awt.Polygon;

public class Coin extends Sprite { //the coin you collect

	public Coin(double x, double y, double a) {
		shape = new Polygon();
		shape.addPoint(0, 10);
		shape.addPoint(10, 0);
		shape.addPoint(0, -10);
		shape.addPoint(-10, 0);

		drawShape = new Polygon();
		drawShape.addPoint(0, 1);
		drawShape.addPoint(1, 0);
		drawShape.addPoint(0, -1);
		drawShape.addPoint(-1, 0);
		rotation = 0.05; //
		xposition = x;
		yposition = y;
		angle = a;

		xposition = (int) (Math.random() * 800 + 1); //start in random spot
		yposition = (int) (Math.random() * 600 + 1); //start in random
	}

	public void updatePosition() {
		angle += rotation;
		super.updatePosition();
	}

	public void jump() { //jump to somewhere the player is not

		if (game.Player.xposition > 400) {
			xposition = (int) (Math.random() * 100 + 100);
		} else {
			xposition = (int) (Math.random() * 200 + 600);
		}

		yposition = (int) (Math.random() * 400 + 100);
	}

}
