package coreGame;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

public class Bullet extends Sprite { //the class for all bullets

	int life = 750; // tracks how long the bullets been alive for (counts down cus it originally despawned bullet)
	int bulletDrawType; // which shape the bullet should be
	int Size; // the size the bullet should be
	Color bulletColor; // the colour the bullet should be
	double thrustChange; // life thrust should add secondary thrust
	double THRUST1; // initial thrust
	double THRUST2; // secondary thrust
	boolean changethrust = false; //a trigger that becomes true when life is less then thrustChange
	boolean recursiveBullet; //if the bullet should create more bullets (explode)
	boolean hasExploded = false; //a trigger to indicate if a bullet has exploded.
	ArrayList<Bullet> bulletList; //the bullet list

	public Bullet(double x, double y, double a, double xspd, double yspd,
			double t1, double t2, double tc, int ty, Color bc, int s, boolean rb,int l) {

		bulletDrawType = ty; // draw type 
		bulletColor = bc; //colour
		xposition = x;// xpos
		yposition = y;//ypos
		angle = a;//angle
		THRUST1 = t1;//initial thrust
		THRUST2 = t2;//secoundary thrust
		thrustChange = tc;//thrustchange
		Size = s; //size
		xspeed = xspd;//xspeed
		yspeed = yspd;//yspeed
		recursiveBullet = rb; // if it explodes
		life = l; //how long before your null by default
		
		bulletList = new ArrayList(); //init bulletList

		if (bulletDrawType == 1) { // if you specified to draw type 1 bullet
			shape = new Polygon();
			shape.addPoint(0 * Size, 1 * Size);
			shape.addPoint(1 * Size, 0 * Size);
			shape.addPoint(0 * Size, -1 * Size);
			shape.addPoint(-1 * Size, 0 * Size);

			drawShape = new Polygon();
			drawShape.addPoint(0 * Size, 1 * Size);
			drawShape.addPoint(1 * Size, 0 * Size);
			drawShape.addPoint(0 * Size, -1 * Size);
			drawShape.addPoint(-1 * Size, 0 * Size);
			rotation = 0;
		}

		if (bulletDrawType == 2) { // if you specified to draw type 1 bullet
			shape = new Polygon();
			shape.addPoint(0 * Size, 5 * Size);
			shape.addPoint(0 * Size, 0 * Size);
			shape.addPoint(0 * Size, -5 * Size);
			shape.addPoint(0 * Size, 0 * Size);

			drawShape = new Polygon();
			drawShape.addPoint(0 * Size, 5 * Size);
			drawShape.addPoint(0 * Size, 0 * Size);
			drawShape.addPoint(0 * Size, -5 * Size);
			drawShape.addPoint(0 * Size, 0 * Size);
			rotation = 0.1;

		}

	}

	public void updatePosition() {
		
		if (life > 0) {
			life--; //lose life
		}
		
			if (life > thrustChange) {  //if life is above thrustchange 
				THRUST = THRUST1; //your thrust is initial thrust
			} else {
	
				if (THRUST < 2) { 
					THRUST = THRUST2;
				}
				changethrust = false;
			}
	
			if (changethrust == false) {
				xspeed *= THRUST; //update to the secound speed 
				yspeed *= THRUST;
				changethrust = true; 
			}
	
		if (recursiveBullet == true) { //if your a recursive bullet
			if (hasExploded == false) { // and you havent exploded yet
				if (life < 350) { // and your life is below the explode time
					for (int i = 0; i <= 360; i += 360 / 45) {

						angle = i;
						//make some bullets
						bulletList
								.add(new Bullet(
										(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
										(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2,
										0, Math.sin(angle), Math.cos(angle),
										THRUST1, THRUST2, thrustChange, 1,
										bulletColor, 1, false,500));
						hasExploded = true; //bullet has exploded
					}
				}
			}
		}

		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).updatePosition();

		}

		
		super.updatePosition();
	}

}
