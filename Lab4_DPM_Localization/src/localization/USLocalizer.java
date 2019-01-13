//
//  DPM Lab 4 - Localization - USLocalizer class
//  Written by Alexandre Tessier and Tyrone Wong
//  This class is responsible for the localization of the robot, using a Lego EV3 Ultrasonic sensor. Using the distance at which the robot detects two walls, 
//  it calculates the angle the robot needs to turn by in order to face the proper positive x axis (0 degrees, parallel to the horizontal grid lines)
//

package localization;

import lejos.robotics.SampleProvider;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static float rotSpeed = 30;

	private Odometer odo;
	private SampleProvider usSensor;
	public static float[] usData;
	private LocalizationType locType;
	private Navigation navigator;
	private int wallDist=13;
	private int noWall=40;
	
	// Collect the data and the various methods/instances needed to create an instance of a ultrasonic sensor localizer
	public USLocalizer(Odometer odo,  SampleProvider usSensor, float[] usData, LocalizationType locType, Navigation navigator) {
		this.odo = odo;
		this.usSensor = usSensor;
		this.usData = usData;
		this.locType = locType;
		this.navigator = navigator;
	}

	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		double deltaTheta;

		if (locType == LocalizationType.FALLING_EDGE) {
			
			// Rotate the robot until it sees no wall
			rotateAwayWall(true, noWall);
			
			// Keep rotating (right) until the robot sees a wall
			rotateToWall(true, wallDist);
			
			// Latch the angle at which it detects the first wall
			angleA = odo.getAng();
					
			// Switch direction (now rotating left) and wait until it sees no wall
			rotateAwayWall(false, noWall);

			// Keep rotating until the robot sees a wall
			rotateToWall(false, wallDist);
			
			// Latch the angle at which it detects the second wall
			angleB = odo.getAng();
			
			// Calculate the angle the robot must add or substract from its current angle in 
			// order to be in the desired coordinate system (x parallel to the grid lines)
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			if (angleA<angleB)
				deltaTheta = 45 - (angleA+angleB)/2;
			
			// angleA is counterclockwise from angleB, so assume the average of the
			// angles to the right of angleB is 225 degrees past 'north'
			else
				deltaTheta = 225 - (angleA+angleB)/2;
			

			// Get the current position of the robot
			odo.getPosition(pos);
			
			// Calculate the robot's current angle, in the proper coordinate system
			double newAngle = deltaTheta + pos[2];
			
			// Update the robot's position in the odometer
			odo.setPosition(new double [] {0.0, 0.0, newAngle}, new boolean [] {true, true, true});
			
			// Turn to the appropriate 0 degree, parallel to the horizontal grid lines
			navigator.turnTo(0, true);
			
		} else {
			
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			// Rotate the robot until it sees no wall
			rotateAwayWall(true, noWall);
			
			// Keep rotating left until the robot detects a wall (first wall)
			rotateToWall(false, wallDist);
			
			// Keep rotating left until the robot doesn't detect a wall anymore
			rotateAwayWall(false, noWall);
			
			// Latch the angle at which it detects no wall (after the robot went past the second wall)
			angleA = odo.getAng();
			
			// Rotate the robot right until it detects again the first wall
			rotateToWall(true, wallDist);
			
			// Rotate until the robot doesn't detect the first wall anymore
			rotateAwayWall(true, noWall);
			
			// Latch the current angle of the robot
			angleB = odo.getAng();
			
			// Calculate the angle the robot must add or substract from its current angle in 
			// order to be in the desired coordinate system (x parallel to the grid lines)
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is, in theory, 45 degrees past 'north'
			// However, testing shows that the robot is closer to 40 degrees past north
			if (angleA<angleB)
				deltaTheta = 40 - (angleA+angleB)/2;
			
			// angleA is counterclockwise from angleB, so assume the average of the
			// angles to the right of angleB is, in theory, 225 degrees past 'north'
			// However, testing shows that the robot is closer to 220 degrees past north
			else
				deltaTheta = 220 - (angleA+angleB)/2;
			
			// Get the current position of the robot
			odo.getPosition(pos);
			
			// Calculate the robot's current angle, in the proper coordinate system
			double newAngle = deltaTheta + pos[2];
			
			// Update the robot's position in the odometer
			odo.setPosition(new double [] {0.0, 0.0, newAngle}, new boolean [] {true, true, true});
			
			// Turn to the appropriate 0 degree, parallel to the horizontal grid lines
			navigator.turnTo(0, true);
			
		}
	}
	
	// Method used to rotate the robot to a wall, using data from the ultrasonic sensor
	private void rotateToWall(boolean right, int dist){
		
		// If the user wants the robot to rotate to the right,
		// right wheel speed is set at negative left wheel speed
		if(right==true)
			navigator.setSpeeds(rotSpeed, -rotSpeed);
		
		// If the user wants the robot to rotate to the left, 
		// left wheel speed is set at negative right wheel speed
		else
			navigator.setSpeeds(-rotSpeed, rotSpeed);
		
		// While the ultrasonic detects objects further away than the given distance,
		// the robot keeps rotating
		while(usData[0]>dist)
			usData[0]=getData();
		
		// Stop the robot
		navigator.setSpeeds(0, 0);
		
	}
	
	// Method used to rotate the robot away from a wall, using data from the ultrasonic sensor
	private void rotateAwayWall(boolean right, int dist){
		
		// If the user wants the robot to rotate to the right,
		// right wheel speed is set at negative left wheel speed
		if(right==true)
			navigator.setSpeeds(rotSpeed, -rotSpeed);
		
		// If the user wants the robot to rotate to the left, 
		// left wheel speed is set at negative right wheel speed
		else
			navigator.setSpeeds(-rotSpeed, rotSpeed);
		
		// While the ultrasonic detects objects closer than the given distance,
		// the robot keeps rotating
		while(usData[0]<dist)
			usData[0]=getData();
		
		// Stop the robot
		navigator.setSpeeds(0, 0);
	}

	// Get the data from the ultrasonic sensor and appropriately convert it 
	private float getData() {
		
		// Fetch a sample distance
		usSensor.fetchSample(usData, 0);
	
		// Convert it to cm
		float distance = usData[0]*100;
		
		// Return the converted distance
		return distance;
	}

}
