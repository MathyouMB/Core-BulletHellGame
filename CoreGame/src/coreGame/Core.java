package coreGame;

import java.awt.Color.*;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Core extends Sprite {//the core is the yellow thing thats shoots in the middle of the screen

	ArrayList<Bullet> bulletList; // array that holds all the bullets
	int health; // when 0 you change sequence
	int bulletTimer; //cooldown for bullet shooting
	int numColor; //used for switching bullet colors
	boolean CoinCanForm = false; // time when coin can form
	double bulletThrust; // bullet speed
	double relativeAngle; //angle of square
	double bulletRotation; //angle you add relative angle to turn the shape while shooting bullets at different angles
	double bulletThrustVariant; //used to give certain bullets different speeds
	int PhaseNum = 1; //your current bullet pattern
	int PhaseTimer; //counts the time needed to switch patterns
	int Phase4timer; //an extra timer used in phase 4
	int HoldSequence; //holds the current sequence,  later becomes old sequence
	int OldSequence; //holds last sequence so you can't have the same pattern twice back to back
	boolean reset; //makes sure you start with the first pattern in a sequence
	final int TOTALSEQUENCES = 6; //how many sequences their are total
	final int HEALTHRESET = 4; //how much health is needed to switch sequence
	
	public Core() { //contructor
		
		bulletList = new ArrayList();
		xposition = 450; 
		yposition = 300;
		shape = new Polygon();
		shape.addPoint(20, 20);
		shape.addPoint(-20, 20);
		shape.addPoint(-20, -20);
		shape.addPoint(20, -20);
		drawShape = new Polygon();
		drawShape.addPoint(5, 5);
		drawShape.addPoint(-5, 5);
		drawShape.addPoint(-5, -5);
		drawShape.addPoint(5, -5);
		relativeAngle = -Math.PI / 2 + bulletRotation; //keep turning + bullet angle adjust

	}
	public void updatePosition() { //update bullets, call behavior

		for (int i = 0; i < bulletList.size(); i++) { //  check if bullets are off the canvas
			if (bulletList.get(i).recursiveBullet == false) { // if a bullet is recursive, dont delete because it will delete all the little bullets
				if (bulletList.get(i).xposition > 1000
						|| bulletList.get(i).xposition < 0) {

					bulletList.remove(i); // if a bullet is off the canvas, it becomes null and is garbage collected
				}
				if (bulletList.get(i).yposition > 1000
						|| bulletList.get(i).yposition < 0) {
					bulletList.remove(i); // if a bullet is off the canvas, it becomes null and is garbage collected

				}
			}
		}
		
		//update the bullets 
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).updatePosition();

		}

		BossBehavior(); //the shooting and turning 

		super.updatePosition(); //do sprites update position

	}
	public void BossBehavior() { //increases phase timer, if hp = 0 call new sequence

		PhaseTimer++;

		if (health < 1) { // when you touch the coin hp goes down, it switches phase when you collect enough coins
			pickASequence(); // pick a new HoldSequence
		}

		if (HoldSequence == 1) {
			Sequence1();
		
		}

		if (HoldSequence == 2) {
			Sequence2();
		
		}

		if (HoldSequence == 3) {
			Sequence3(); ////there is a bug with phase 3
		
		}

		if (HoldSequence == 4) {
			Sequence4();
		
		}
		if (HoldSequence == 5) {
			Sequence5();
		
		}
		if (HoldSequence == 6) {
			Sequence6();
		}

	}
	public void pickASequence() { //pick a new sequence and reset timers and bullet array

		HoldSequence = (int) (Math.random() * TOTALSEQUENCES) + 1; // pick random number for sequence
		
		if (HoldSequence == OldSequence) { // if its the same as last phase...

			if (OldSequence + 1 < TOTALSEQUENCES) { 
				HoldSequence += 1; //increase hold sequence if your not going over 7
			} else if (OldSequence - 1 > 0) {
				HoldSequence -= 1; //decrease hold sequence if your not going under 0
			}

		}
	
		restart(); // reset cooldowns, bullets,  stats etc.
	}
	public void Sequence1() { // phase 1-3
		if (PhaseTimer > 100) { //if timer hits the turn over time
			PhaseNum += 1; //phase number changes
			PhaseTimer = 0; //timer resets
		}

		if (PhaseNum > 3) {
			PhaseNum = 1; //if you go over three start sequence over
			CoinCanForm = true;
		}
		if (PhaseNum == 1) { //slow green pattern
			Phase1();
		}
		if (PhaseNum == 2) { //red pattern
			Phase2();
		}
		if (PhaseNum == 3) {//cyan stick pattern
			Phase3();

		}
	}
	public void Sequence2() { //phase 4
		Phase4();
		if (PhaseTimer > 300) {
			CoinCanForm = true;
		}
	}
	public void Sequence3() {//phase5
		Phase5();
		if (PhaseTimer > 300) {
			CoinCanForm = true;
		}
	}
	public void Sequence4() {//phase6
		Phase6();
		if (PhaseTimer > 300) {
			CoinCanForm = true;
		}
	}
	public void Sequence5() {//phase7
		Phase7();
		if (PhaseTimer > 300) {
			CoinCanForm = true;
		}
	}
	public void Sequence6() {//phase 8-9
		if (PhaseTimer > 100) {
			PhaseNum += 1;
			PhaseTimer = 0;
		}

		if (PhaseNum > 2) {
			PhaseNum = 1;
			CoinCanForm = true;
		}
		if (PhaseNum == 1) {
			Phase8();

		}

		if (PhaseNum == 2) {
			Phase9();

		}

	}
	public void Phase1() { //slow green spinner pattern

		bulletThrust = 0.75; //bullet speed
		bulletRotation += 0.005; //
		bulletTimer++; //bullet cooldown
		if (bulletTimer > 1) {

			for (int i = 0; i <= 360; i += 360 / 5) { //360 is the total angle, divide by 5 for 5 tendrils

				relativeAngle = i; //the angle of the bullets direction

				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 1, Color.GREEN, 1, false,750)); //fire the bullet

				bulletTimer = 0; //reset cooldown
				angle += (relativeAngle / 100);// turn square
			}

		}
	}
	public void Phase2() { //red spinner pattern
	//all my patterns except phase 7 and 4 use the same algorithm as phase 1.. so just read those comments 
		bulletThrust = 2;
		bulletRotation += 0.005;
		bulletTimer++;
		if (bulletTimer > 5) {
			for (int i = 0; i <= 360; i += 360 / 10) {

				relativeAngle = i;

				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 1, Color.RED, 1, false,500));

				bulletTimer = 0;
				angle += (relativeAngle / 100);
			}

		}
	}
	public void Phase3() { //cyan stick pattern

		bulletThrust = 3;
		bulletRotation += 0.005;
		bulletTimer++;
		if (bulletTimer > 3) {

			for (int i = 0; i <= 360; i += 360 / 7) {

				relativeAngle = i;
				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 2, Color.CYAN, 1, false,500));

				bulletTimer = 0;
				angle += (relativeAngle / 100);
			}

		}
	}
	public void Phase4() { //orange and blue back and forth spinner

		//bulletThrust = 1;
		bulletRotation += 0.005;
		bulletTimer++;
		if (bulletTimer > 1) {

			for (int i = 0; i <= 360; i += 360 / 1) {

				relativeAngle = i;
				bulletThrust = 1;
				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 1, ColorMixTwo(Color.BLUE,
								Color.orange), 1, false,500));
				
				if ((Phase4timer > 150 && Phase4timer < 250) // at certain times spray our cyan stick bullets
						|| (Phase4timer > 400 && Phase4timer < 500)) {				
						
					bulletList.add(new Bullet(
							(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
							(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2,
							0, Math.sin(angle)/1.5, Math.cos(angle)/1.5, bulletThrust,
							bulletThrust, 0, 2, Color.CYAN, 1, false,500));
				}
				bulletTimer = 0;
				
				if (Phase4timer > 250) {
				//	bulletThrust = 1;
					angle += (relativeAngle / 0.075);
				}else{
				//	bulletThrust = 2;
					angle -= (relativeAngle / 0.075);
				}
				
			
				
				Phase4timer++; //cyan stick bullet timer
				if (Phase4timer > 500) {
					Phase4timer = 0;
				}

			}

		}
	}
    public void Phase5() { //orange and green slow spinner

		bulletThrust = 0.75;
		bulletRotation += 0.005;
		bulletTimer++;
		if (bulletTimer > 7) {
			
			for (int i = 0; i <= 360; i += 360 / 10) {

				relativeAngle = i;

				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 1, RandomColorsTwo(Color.ORANGE,
								Color.GREEN), 1, false,750));

				bulletTimer = 0;
				angle += (relativeAngle / 100);
			}

		}

	}
	public void Phase6() { //pink and purple rings

		bulletThrust = 0.75;
		bulletRotation += 0.01;
		bulletTimer++;

		if (bulletTimer > 85) {

			for (int i = 0; i <= 360; i += 360 / 180) {

				relativeAngle = i;

				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 1, ColorMixTwo(Color.MAGENTA,
								Color.PINK), 1, false,750));

				bulletTimer = 0;
				angle += (relativeAngle / 100);
			}

		}

	}
	public void Phase7() { //fire wavy phase

		bulletThrust = 0.75 * bulletThrustVariant; // certain bullets have modified speeds, variant changes over time
		bulletRotation += 0.01;
		if (bulletList.size() < 1000) { //if we dont have 1000, make the variant keep going up
			bulletThrustVariant += 0.01;
		}
		bulletTimer++;//bullet shoot cooldown

		if (bulletThrustVariant > 5) {
			bulletThrustVariant = 0; //reset the variant for the next pattern round
		}
		if (bulletTimer > 1) {

			for (int i = 0; i <= 360; i += 360 / 10) {

				relativeAngle = i;

				if (bulletList.size() < 1000) {
					bulletList.add(new Bullet(
							(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
							(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2,
							0, Math.sin(angle), Math.cos(angle), bulletThrust,
							1.01, 100, 1, RandomColorsTwo(Color.RED,
									Color.ORANGE), 1, false,750));
				}
				bulletTimer = 0;
				angle += (relativeAngle / 100);
			}

		}

	}
	public void Phase8() { // recursive yellow red blue stick bullets

		bulletThrust = 1.5;
		bulletRotation += 0.005;
		bulletTimer++;
		if (bulletTimer > 25) {

			for (int i = 0; i <= 360; i += 360 / 1) {

				relativeAngle = i;

				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 2, RandomColorsThree(Color.CYAN,
								Color.RED,Color.YELLOW), 1, true,500));

				bulletTimer = 0;
				angle += (relativeAngle / 100);
			}

		}

	}
	public void Phase9() { //green wavy pattern

		bulletThrust = 1.5;
		bulletRotation += 0.005;
		bulletTimer++;
		if (bulletTimer > 45) {

			for (int i = 0; i <= 360; i += 360 / 45) {

				angle = i;
				
				bulletList.add(new Bullet(
						(drawShape.xpoints[0] + drawShape.xpoints[2]) / 2,
						(drawShape.ypoints[0] + drawShape.ypoints[2]) / 2, 0,
						Math.sin(angle), Math.cos(angle), bulletThrust,
						bulletThrust, 0, 1, Color.GREEN, 1, false,750));

				bulletTimer = 0;
			}

		}

	}
	public Color ColorMixTwo(Color c1, Color c2) { //patterned colors
		
		
		numColor +=1; //next bullet will be the next color

		if (numColor %3 == 0) {
			return c1;
		} else {
			return c2;
		}


	}
	public Color RandomColorsTwo(Color c1, Color c2) { //2 random colors 

		int rndColor = 0;
		rndColor = (int) (Math.random() * 2) + 1;

		if (rndColor == 1) {
			return c1;
		} else {
			return c2;
		}

		// return c1;

	}
	public Color RandomColorsThree(Color c1, Color c2, Color c3) {//3 random colors

		int rndColor = 0;
		rndColor = (int) (Math.random() * 3) + 1;

		if (rndColor == 1) {
			return c1;
		} else if (rndColor == 2) {
			return c2;
		} else {
			return c3;
		}

	

	}
	public void restart() { //reset the stats and timers
		
		bulletList.clear();
		PhaseTimer = 0;
		bulletTimer = 0;
		health = HEALTHRESET;
		OldSequence = HoldSequence;
		CoinCanForm = false;
		reset = false; //make sure you start with the first phase in a sequence
	

	}
}
