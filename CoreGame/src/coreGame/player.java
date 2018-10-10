package coreGame;

import java.awt.Polygon;
import java.util.ArrayList;

public class player extends Sprite { // this is the player class, it handles and draws the player

	final double MAINSPEED = 5; // normal speed
	final double HALFSPEED = 2.5; // speed you get when you hold slow button

	public player() {

		// players drawpoints
		// drawpoints are the shape of the object
		shape = new Polygon();
		shape.addPoint(2, 2);
		shape.addPoint(-2, 2);
		shape.addPoint(-2, -2);
		shape.addPoint(2, -2);
		drawShape = new Polygon();
		drawShape.addPoint(2, 2);
		drawShape.addPoint(-2, 2);
		drawShape.addPoint(-2, -2);
		drawShape.addPoint(2, -2);

		rotation = 0.15; // speed at which players draw points rotate
		angle = -Math.PI / 2; // initial angle
		canWarp = true; // the player object can warp through the sides of the
						// game
	}

	public void reset() { // method is called when game starts or you die.

		xposition = 450;
		yposition = 450;
		xspeed = 0;
		yspeed = 0;

	}

}
