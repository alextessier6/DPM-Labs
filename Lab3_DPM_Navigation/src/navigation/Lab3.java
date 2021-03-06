// Lab2.javaAVIbst

package navigation;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;


public class Lab3 {
	
	// Get port S1 used for the Ultrasonic Sensor
	private static final Port usPort = LocalEV3.get().getPort("S1"); 		
	private static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(usPort);

	// Static Resources:
	// Left motor connected to output A
	// Right motor connected to output D
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));

	// Constants
	public static double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 14.2;

	public static int travellingtox;
	public static int travellingtoy;
	
	public static void main(String[] args) {
		int buttonChoice;

		// some objects that need to be instantiated
		
		final TextLCD t = LocalEV3.get().getTextLCD();
		Odometer odometer = new Odometer(leftMotor, rightMotor);
		Driver Navigator = new Driver(odometer, leftMotor, rightMotor);
		Driver avoider = new Driver(odometer, leftMotor, rightMotor);
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer,t);
		
		
		//do while loops displays program selection. User has the choice to run the block avoidance program or the 
		//simple drive to coordinate program
		do {
			// clear the display
			t.clear();

			// ask the user whether the motors should drive to coordinates while avoiding blocks or not
			t.drawString("< Left | Right >", 0, 0);
			t.drawString("       |        ", 0, 1);
			t.drawString(" Avoid | Drive  ", 0, 2);
			t.drawString(" Block |  to    ", 0, 3);
			t.drawString("       | Coords ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		
		//User chooses to run the drive to coordinate program
		if (buttonChoice == Button.ID_RIGHT) {
			
			odometer.start();
			odometryDisplay.start();
			
			//set of coordinates to travel to
			Navigator.travelTo(60,30);
			Navigator.travelTo(30,30);
			Navigator.travelTo(30,60);
			Navigator.travelTo(60,0);
			
		} 
		
		//User chooses to run the block avoidance program
		if (buttonChoice == Button.ID_LEFT) {			

			ObstacleAvoidance avoid = new ObstacleAvoidance(odometer, usSensor, leftMotor, rightMotor, Navigator, avoider);
				
		
			odometer.start();
			odometryDisplay.start();
			avoid.start(); 
			travellingtox=0;   
			travellingtoy=60;  
			Navigator.travelTo(0,60);
	        	
			//used a thread sleeper in order to prevent the avoid thread from skipping over the rest of
			//the block of code should the robot detect a block
			while(ObstacleAvoidance.block==true)
				try { Thread.sleep(1); } catch(Exception e){}
			
			if(Navigator.isNavigating()==false){
				travellingtox=60;
				travellingtoy=0; 
			Navigator.travelTo(60,0);
			}
						
		}
		//checks if botton is pressed to exit program and return to main menu
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}
