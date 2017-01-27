/*
 * SquareDriver.java
 */
package navigation;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Driver extends Thread {
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
	
	
	public void drive (double travelDist) {
		
			leftMotor.setSpeed(FORWARD_SPEED);
			rightMotor.setSpeed(FORWARD_SPEED);
			
			navigating = true;

			leftMotor.rotate(convertDistance(WHEEL_RADIUS, travelDist), true);
			rightMotor.rotate(convertDistance(WHEEL_RADIUS, travelDist), false);

			navigating = false;
	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
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
			newTheta = Math.atan(displacementX/displacementY)-Math.PI;
		
		else if (displacementX<0 && displacementY<0)
			newTheta = Math.atan(displacementX/displacementY)+Math.PI;
			
		deltaTheta = newTheta-currentTheta;
		
		//Make sure the angle calculated is the smallest angle necessary
		if(deltaTheta>=-Math.PI && deltaTheta<=Math.PI)
			return deltaTheta;
		
		else if (deltaTheta<-Math.PI)
			return deltaTheta+(2*Math.PI);
		
		else if (deltaTheta>Math.PI)
			return deltaTheta-(2*Math.PI);
		
		else
			return 0;
	}
	
	
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
		
		turnTo(turnTheta);
		drive (travelDist);
		
	}
	
	public void turnTo (double theta){
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(theta)), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(theta)), false);
	}
	
	boolean isNavigating(){
		return navigating;
	}
}
