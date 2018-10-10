package coreGame;
import java.awt.*;
public class Sprite { //The sprite class handles all objects that are drawn and interact on the gui.
	
	    double xposition; // holds the x coordinate of an object
	    double yposition; // holds the y coordinate of an object
	    double xspeed; //the speed in which an object travels the x axis
	    double yspeed; //the speed in which an object travels the y axis
	    double angle; //the angle of an objects drawn points
	    Polygon shape, drawShape; //the objects drawn points
	    double rotation; //increments an objects angle
	    double THRUST; //gives the object a direction by changing the x and y speed based on angle.
	    boolean canWarp; //if an object can wrap around the screen.
	
	public void updatePosition() {
	      //  counter++;
	     
		if (rotation != 0) {
	    	 angle += rotation; //rotate if a rotation is specified
	     }
	      
	    xposition += xspeed; //every tick update xpos
	    yposition += yspeed; //every tick update ypos
	 
	    if (canWarp == true) { // if you can warp check if you can warp.  
	    	 wraparound();
	     }
	        
	        int x,y;
	        
	        for (int i = 0; i < shape.npoints; i++) //for all of an objects drawn points
	        {
	            x = (int)Math.round(shape.xpoints[i]*Math.cos(angle) - shape.ypoints[i]*Math.sin(angle)); //update the xpoints based on angle
	            y = (int)Math.round(shape.xpoints[i]*Math.sin(angle) + shape.ypoints[i]*Math.cos(angle)); //update the y points based on angle
	            drawShape.xpoints[i] = x;
	            drawShape.ypoints[i] = y;
	        }
	        
	        drawShape.invalidate(); // you need to call this after updating any polygon draw points.
	        drawShape.translate((int)Math.round(xposition), (int)Math.round(yposition)); //moves points to correct coordinates of that object
	        
	}
	
	  public void paint(Graphics g){
	        g.drawPolygon(drawShape); // draw the points
	    }
	
	  private void wraparound() {
	        if (xposition > 900)
	        {
	        	//if your off the right side, go to the left side
	            xposition = 0;
	        }
	        
	        if (xposition < 0)
	        {
	        	//if your off the left side, go to the right side
	            xposition = 900;
	        }
	        
	        if (yposition > 600)
	        {
	        	//if your out the bottom side, go to the top side
	            yposition = 0;
	        }
	        
	        if (yposition < 0)
	        {
	        	//if your out the top side, go to the bottom side
	            yposition = 600;
	        }
	    }
}
