/*
 * SquareDriver.java
 */
package navigation;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Driver extends Thread {
	
	//Declaration of variables and fixed speeds of forward travel and rotation
	
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;
	public EV3LargeRegulatedMotor leftMotor;
	public EV3LargeRegulatedMotor rightMotor;
	public Odometer odometer;
	boolean navigating;
	
	public static double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 14.2;
	
	public Driver(Odometer odometer, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor){
		this.odometer =  odometer;
		this.leftMotor=leftMotor;
		this.rightMotor=rightMotor;
		navigating = false;
	}
	
	
	//drive method checks if the navigating boolean is true, if true, perform nothing. 
	//If false, robot travels a distance (travelDist)
	public void drive (double travelDist) {
		
			if(navigating==true){
				
			}
			
			else {
			leftMotor.setSpeed(FORWARD_SPEED);
			rightMotor.setSpeed(FORWARD_SPEED);

			leftMotor.rotate(convertDistance(WHEEL_RADIUS, travelDist), true);
			rightMotor.rotate(convertDistance(WHEEL_RADIUS, travelDist), false);
			}
	}
	
	
	//convertDistance method returns distance into radians*********** not sure
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	//convertsAngle method returns angle ************************* not sure
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	
	//calculateTheta takes the current coordinates x1,y1 with the current angle and the desirable coordinates x2,y2
	//and returns the difference in angle between the current angle and the desirable angle heading towards the desirable coordinates
	public double calculateTheta (double x1, double x2, double y1, double y2, double currentTheta){
	
		double newTheta=0;
		double deltaTheta;
		double displacementX = x2-x1;
		double displacementY = y2-y1;
		
		//Calculates the angle the robot needs to head at
		if(displacementX>0 && displacementY>0)
			newTheta = Math.atan(displacementX/displacementY);
		
		else if(displacementX>0 && displacementY<0)
			newTheta = Math.atan(displacementX/displacementY)+Math.PI;
		
		else if (displacementX<0 && displacementY>0)
			newTheta = Math.atan(displacementX/displacementY);
		
		else if (displacementX<0 && displacementY<0)
			newTheta = Math.atan(displacementX/displacementY)-Math.PI;
			
		deltaTheta = newTheta-currentTheta;
		
		//Insures the angle calculated is the smallest angle necessary
		if(deltaTheta>=-Math.PI && deltaTheta<=Math.PI)
			return deltaTheta;
		
		else if (deltaTheta<-Math.PI)
			return deltaTheta+(2*Math.PI);
		
		else if (deltaTheta>Math.PI)
			return deltaTheta-(2*Math.PI);
		
		else
			return 0;
	}
	
	//travelTo method receives a destination specified by x2 and y2 and sets the Mindstorm to travel to that destination
	public void travelTo (double x2, double y2){
		
		double[] position = new double[3];
		odometer.getPosition(position);
		double x1 = position[0];
		double y1 = position[1];
		double currentTheta = position[2];
		
		double deltaX = x1-x2;
		double deltaY = y1-y2;
		double turnTheta = calculateTheta(x1, x2, y1, y2,currentTheta);
		double travelDist = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
	
		if(navigating==true){
			
		}
		
		//else statement orients the robot towards the destinatin using the turnTo method and travels using the drive method
		else {
		turnTo(turnTheta);
		drive (travelDist);
		}
	}
	
	
	//turnTo method receives a theta angle and rotates the robot to that degree
	public void turnTo (double theta){
		
		if(navigating==true){
		}
		
		else {
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(theta)), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(theta)), false);
		}
	}
	
	//boolean method returns navigating as true or false, used for certain for loops above
	boolean isNavigating(){
		return navigating;	
	}
	
	void setisnav(boolean isnav){
		navigating=isnav;
	}
}
