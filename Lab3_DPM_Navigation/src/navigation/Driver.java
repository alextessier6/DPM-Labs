/*
 * SquareDriver.java
 */
package navigation;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Driver {
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;
	public EV3LargeRegulatedMotor leftMotor;
	public EV3LargeRegulatedMotor rightMotor;
	public Odometer odometer;
	
	public static double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 14.2;
	
	public static void drive(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
			double leftRadius, double rightRadius, double width, double travelDist) {
		// reset the motors
		for (EV3LargeRegulatedMotor motor : new EV3LargeRegulatedMotor[] { leftMotor, rightMotor }) {
			motor.stop();
			motor.setAcceleration(3000);
		}

		// wait 5 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}

		for (int i = 0; i < 4; i++) {
			// drive forward two tiles
			leftMotor.setSpeed(FORWARD_SPEED);
			rightMotor.setSpeed(FORWARD_SPEED);

			leftMotor.rotate(convertDistance(travelDist, 91.44), true);
			rightMotor.rotate(convertDistance(travelDist, 91.44), false);

			// turn 90 degrees clockwise
			leftMotor.setSpeed(ROTATE_SPEED);
			rightMotor.setSpeed(ROTATE_SPEED);

			leftMotor.rotate(convertAngle(leftRadius, width, 90.0), true);
			rightMotor.rotate(-convertAngle(rightRadius, width, 90.0), false);
		}
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
		if(displacementX>0)
			newTheta = Math.atan(displacementY/displacementX);
		
		else if (displacementX<0 && displacementY>0)
			newTheta = Math.atan(displacementY/displacementX)+Math.PI;
		
		else if (displacementX<0 && displacementY<0)
			newTheta = Math.atan(displacementY/displacementX)-Math.PI;
			
		deltaTheta = newTheta-currentTheta;
		
		//Make sure the angle calculated is the smallest angle necessary
		if(deltaTheta>=-Math.PI && deltaTheta<=Math.PI)
			return deltaTheta;
		
		else if (deltaTheta<-Math.PI)
			return deltaTheta+(2*Math.PI);
		
		else if (deltaTheta>180)
			return deltaTheta-(2*Math.PI);
		
		else
			return 0;
	}
	
	
	public void travelTo (double x, double y){
		double deltaX = odometer.getX()-x;
		double deltaY = odometer.getY()-y;
		
		double travelDist = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
		
		if (odometer.getX()!=x&&odometer.getY()!=y)
			drive(leftMotor, rightMotor, WHEEL_RADIUS, WHEEL_RADIUS, TRACK, travelDist);
	}
	
	public void turnTo (double theta){
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, theta), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, theta), false);
	}
	
	
	
}