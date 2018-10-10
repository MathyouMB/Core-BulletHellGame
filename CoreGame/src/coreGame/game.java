package coreGame;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.util.ArrayList;
	
//Matthew M-B
//05/06/2017
//This is my grade 11 programming summative.

@SuppressWarnings("serial")
public class game extends Applet implements KeyListener, ActionListener {

	Image offscreen; // canvas
	Graphics offg; // canvas
	static player Player; // player
	Core theCore; // thing in center
	Coin theCoin; // thing you collect
	boolean gameOver = false; // if your dead
	boolean slowMode; // if your slow
	boolean gameMenu = true; // if your in the menu
	boolean testMode = false; // if your in test mode
	Timer timer; // timer to update action listener
	int menuButtonNum = 0; // if your on the quit or play buttons in main menu
	int score;
	public void init() { // init is called when the window first opens.

		gameMenu = true; // when you start, your in menu mode
		Player = new player(); // declare player
		theCore = new Core(); // declare the thing that shoots
		theCoin = new Coin(450, 300, Math.random() + 10); // declare the coin
		this.setSize(900, 600); // set window size
		this.addKeyListener(this); // add key listner
		timer = new Timer(20, this); // timer updates action listener
		offscreen = createImage(this.getWidth(), this.getHeight()); // canvas
																	// size //
																	// size
		offg = offscreen.getGraphics(); // the canvas
		Frame window = (Frame) this.getParent().getParent(); // window size
		window.setTitle("Core"); // the windows title
		Player.reset();// when you open the player gets its initial x and y
	}

	public void start() { //timer default
		timer.start();
	}

	public void stop() { //timer default
		timer.stop();
	}

	public void paint(Graphics g) { // this is the paint, everything on screen
									// is painted here
		
		offg.setColor(Color.BLACK); // background is black
		offg.fillRect(0, 0, 900, 600); // fill the background with black
		offg.setColor(Color.WHITE); // text is white

		if (gameOver == true) { // if you die
			offg.setFont(new Font("Ariel", Font.PLAIN, 40));
			if (score != 1) {
				offg.drawString("You got "+score+" points", 350, 300);
			} else {
				offg.drawString("You got "+score+" point", 350, 300);
			}
			offg.setFont(new Font("Ariel", Font.PLAIN, 12));
			offg.drawString("Press R to restart", 450, 350);
		}

		if (gameMenu == true) { // if your in the menu mode

			offg.setFont(new Font("Comic Sans MS", Font.ITALIC, 100));
			offg.drawString("Core", 340, 150);
			offg.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
			offg.drawString("a bad game by Matt MB", 395, 180);
			offg.setFont(new Font("Ariel", Font.PLAIN, 36));

			// to indicate which menu button is selected, one is yellow.
			if (menuButtonNum == 0) { // if Play is selected, its yellow
				offg.setColor(Color.YELLOW);
				offg.drawString("Play", 420, 500);
			} else {
				offg.setColor(Color.WHITE); // if its not selected its white
				offg.drawString("Play", 420, 500);
			}
			if (menuButtonNum == 1) { // if Quit is selected its yellow
				offg.setColor(Color.YELLOW);
				offg.drawString("Quit", 420, 550);
			} else {
				offg.setColor(Color.WHITE); // if its not selected its white
				offg.drawString("Quit", 420, 550);
			}

		}

		if (testMode == true) { // test mode lets you see certain important
								// numbers
			
			offg.setFont(new Font("Ariel", Font.PLAIN, 12));
			offg.drawString("" + theCore.health, (int) theCore.xposition - 3,
					(int) theCore.yposition + 5);
			offg.drawString("xposition: " + Player.xposition, 750, 550);
			offg.drawString("yposition: " + Player.yposition, 750, 570);
			offg.drawString("BulletRot: " + theCore.bulletThrustVariant, 750,
					530);
			offg.drawString("AngleRela: " + theCore.relativeAngle, 750, 510);
			offg.drawString("CoreAngle: " + theCore.angle, 750, 490);
			

		}
		if (gameOver == false) {
			
		for (int i = 0; i < theCore.bulletList.size(); i++) { // draw the cores
																// bullets

			offg.setColor(theCore.bulletList.get(i).bulletColor);
			theCore.bulletList.get(i).paint(offg);

		}
		for (int j = 0; j < theCore.bulletList.size(); j++) { // the bullets
																// have their
																// own bullets,
																// draw those
																// aswell
			for (int i = 0; i < theCore.bulletList.get(j).bulletList.size(); i++) {

				offg.setColor(theCore.bulletList.get(j).bulletList.get(i).bulletColor);
				theCore.bulletList.get(j).bulletList.get(i).paint(offg);
			}
		}
		}

		offg.setColor(Color.YELLOW); // player and core are yellow

		if (gameOver == false) {
		if (gameMenu == false) { // if your not in the menu , paint the main
									// game objects
			offg.setFont(new Font("Ariel", Font.PLAIN, 12));
			offg.drawString("Points: "+score, 25, 20);
			Player.paint(offg);
			if (theCore.CoinCanForm == true){
				theCoin.paint(offg);
			}
		}
		theCore.paint(offg); // paint the core
		}
		offg.setFont(new Font("Ariel", Font.PLAIN, 12));
		//offg.drawString("0"+Player.drawShape.xpoints[0]+"", 350, 300);
		//offg.drawString("1"+Player.drawShape.xpoints[1]+"", 350, 310);
		//offg.drawString("2"+Player.drawShape.xpoints[2]+"", 370, 320);
		g.drawImage(offscreen, 0, 0, this); // draw the canvas
		repaint(); // repaint every update
	}

	public void update(Graphics g) {//update the paint
		paint(g); // update the paint

	}

	public void actionPerformed(ActionEvent e) { //this is whats updated by the timer

		theCore.updatePosition(); // core has its own update position
		if (theCore.CoinCanForm == true) {
			theCoin.updatePosition(); // if the coin can form, update its
										// position
		}
		Player.updatePosition(); // update players position
		CheckCollide(); // check for any collisions

	}

	public boolean collision(Sprite thing1, Sprite thing2) { // returns true if
																// 2 objects are
																// colliding

		// takes 2 sprites because all drawn objects extend the sprite class

		int x, y;

		for (int i = 0; i < thing1.drawShape.npoints; i++) { // loop through the
																// polygons
																// drawpoints
			x = thing1.drawShape.xpoints[i];
			y = thing1.drawShape.ypoints[i];

			if (thing2.drawShape.contains(x, y)) { // if they are inside each
													// other
				return true; // collide = true
			}
		}

		for (int i = 0; i < thing2.drawShape.npoints; i++) {
			x = thing2.drawShape.xpoints[i];
			y = thing2.drawShape.ypoints[i];

			if (thing1.drawShape.contains(x, y)) { // if thing 2 is in thing 1
				return true; // your colliding
			}
		}

		return false; // if neither objects were inside eachother, you didnt
						// collide
	}

	public void CheckCollide() {// check collisions
		for (int j = 0; j < theCore.bulletList.size(); j++) { // if player hits
																// a bullet
			if (collision(theCore.bulletList.get(j), Player)) {
				if (gameMenu == false){
					if (testMode == false){
						gameOver = true; // you died
						restart(); // reset the things
					}
				}
			}
		}
		if (theCore.CoinCanForm == true) { // if the coin exists
			if (collision(theCoin, Player)) {
				score +=1; //score up
				theCore.health -= 1; // core health goes down
				theCoin.jump(); // coin gets new location

			}
		}

		if (theCore.CoinCanForm == true) {
			if (collision(theCoin, theCore)) {
				theCoin.jump(); // if coins new location is on the shooting guy,
								// move it again

			}
		}

	}

	@SuppressWarnings("static-access")
	public void keyPressed(KeyEvent e) { //if you press a key

		if (e.getKeyCode() == KeyEvent.VK_T) {
			testMode = true; // activate testmode

		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (gameMenu == false) { // if your in game,
				gameMenu = true; // go to menu
			} else {
				System.exit(0); // if your in menu , close game
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (menuButtonNum == 0) { // if you hit enter on play
				gameMenu = false; // if you enter game, menu mode is off
				restart(); // reset the objects
			}

			if (menuButtonNum == 1) { // if you hit enter on quit
				System.exit(0); // close window
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			slowMode = true; // if your holding space you move a bit slower
		}

		if (e.getKeyCode() == KeyEvent.VK_D) { // go right
			if (slowMode == false) {
				Player.xspeed = Player.MAINSPEED;
			} else {
				Player.xspeed = Player.HALFSPEED;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_A) { // go left
			if (slowMode == false) {
				Player.xspeed = -Player.MAINSPEED;
			} else {
				Player.xspeed = -Player.HALFSPEED;
			}
		}

		if ((e.getKeyCode() == KeyEvent.VK_W)
				|| (e.getKeyCode() == KeyEvent.VK_UP)) { // go up
			if (slowMode == false) {
				Player.yspeed = -Player.MAINSPEED;
			} else {
				Player.yspeed = -Player.HALFSPEED;
			}
			if (gameMenu == true) {
				menuButtonNum = 0; // move menu select up
			}
		}
		if ((e.getKeyCode() == KeyEvent.VK_S)
				|| (e.getKeyCode() == KeyEvent.VK_DOWN)) { // go down
			if (slowMode == false) {
				Player.yspeed = Player.MAINSPEED;
			} else {
				Player.yspeed = Player.HALFSPEED;
			}
		
			if (gameMenu == true) {
				menuButtonNum = 1; // move menu select down
			}
		}
		if ((gameOver == true) || (testMode == true)) { // if your dead or in
														// test mode
			if (e.getKeyCode() == KeyEvent.VK_R) {
				restart(); // you can reset the round
				score = 0; //resetscore
				gameOver = false; // no longer dead

			}
		}
	}

	@SuppressWarnings("static-access")
	public void keyReleased(KeyEvent e) { // if you release a key,

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			slowMode = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_D) {
			Player.xspeed = 0;
		}

		if (e.getKeyCode() == KeyEvent.VK_A) {
			Player.xspeed = 0;
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			Player.yspeed = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			Player.yspeed = 0;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT
				&& e.getKeyCode() == KeyEvent.VK_LEFT) {
			Player.xspeed = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP
				&& e.getKeyCode() == KeyEvent.VK_DOWN) {
			Player.yspeed = 0;
		}

	}

	public void restart() { // resets the objects for next round.
		
		theCore.restart();
		Player.reset();
		theCore.pickASequence();
	}

	public void keyTyped(KeyEvent e) { //key listener default
	}

}
