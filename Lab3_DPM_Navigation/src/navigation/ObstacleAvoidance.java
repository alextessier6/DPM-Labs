package navigation;

import lejos.hardware.sensor.*;
import lejos.hardware.ev3.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class ObstacleAvoidance extends Thread {
	private static final int ROTATE_SPEED = 150;
	private static final int FORWARD_SPEED = 250;
	private EV3UltrasonicSensor us;
	private Odometer odometer;
	private Driver navigator;
	
	
//	public void avoid (EV3UltrasonicSensor us, Odometer odometer, Driver navigator){
//}
//
//}
//public void blockDetection(){
//	
//}
//
//public void avoidBlock(){
//
//	navigator.turnTo(90);
//	navigator.drive(30);
//	navigator.turnTo(-90);
}
