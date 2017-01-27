// Lab2.java

package navigation;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;



public class Lab3 {
	
	// Get port S1 used for the Ultrasonic Sensor
//	private static final Port usPort = LocalEV3.get().getPort("S1");

	// Static Resources:
	// Left motor connected to output A
	// Right motor connected to output D
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));

	// Constants
	public static double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 14.2;

	public static void main(String[] args) {
		int buttonChoice;

		// some objects that need to be instantiated
		
		final TextLCD t = LocalEV3.get().getTextLCD();
		Odometer odometer = new Odometer(leftMotor, rightMotor);
		Driver Navigator = new Driver(odometer, leftMotor, rightMotor);
//		ObstacleAvoidance Avoider = new ObstacleAvoidance();
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer,t);
		
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

		if (buttonChoice == Button.ID_RIGHT) {
			
			odometer.start();
			odometryDisplay.start();
			Navigator.start();
			
			Navigator.travelTo(60,30);
			Navigator.travelTo(30,30);
			Navigator.travelTo(30,60);
			Navigator.travelTo(60,0);
			
		} if (buttonChoice == Button.ID_LEFT) {
			// start the odometer, the odometry display and (possibly) the
			// odometry correction
				
//			ObstacleAvoidance.start();
			odometer.start();
			odometryDisplay.start();
			Navigator.start();
			Navigator.travelTo(0,60);
			Navigator.travelTo(60,0);

			// spawn a new Thread to avoid Driver.drive() from blocking
//			(new Thread() {
//				public void run() {
//					Navigator.drive(leftMotor, rightMotor, WHEEL_RADIUS, WHEEL_RADIUS, TRACK);
//				}
//			}).start();
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}