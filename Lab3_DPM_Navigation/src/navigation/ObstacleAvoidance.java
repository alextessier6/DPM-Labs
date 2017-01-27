package navigation;

import lejos.hardware.sensor.*;
import lejos.hardware.ev3.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;


public class ObstacleAvoidance extends Thread {
	private static final int ROTATE_SPEED = 150;
	private static final int FORWARD_SPEED = 250;
//	private EV3UltrasonicSensor us;
	private Odometer odometer;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private Driver navigator;
	private EV3UltrasonicSensor usSensor;
	private int minDist=5;
	private int[] currentDist = new int[4];
	public float[] usData = new float[4];
	
	public ObstacleAvoidance (Odometer odometer, EV3UltrasonicSensor usSensor, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Driver navigator){
		this.odometer =  odometer;
		this.leftMotor=leftMotor;
		this.rightMotor=rightMotor;
		this.usSensor=usSensor;
		this.navigator=navigator;
		
	}
	
	public void run (){
		
	    while(true){
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0]=(int)(usData[0]*100.0);
//			currentDist[1]=(int)(usData[1]*100.0);
//			currentDist[2]=(int)(usData[2]*100.0);
//			currentDist[3]=(int)(usData[3]*100.0);
//			
//			double averageDist=mean(currentDist);
			if(currentDist[0]<=minDist){
//				leftMotor.stop();
//				rightMotor.stop();
				avoidBlock();
				
				navigator.travelTo(60, 0);
			}
//			try { Thread.sleep(10); } catch(Exception e){}
		}
	}	

//}
//public boolean blockDetection(){
//	if(usPoller.getDistance)
//}
public static double mean(int[] m) {
	double sum = 0;
	for (int i = 0; i < m.length; i++) {
	      sum += m[i];
	    }
	return sum / m.length;
}	
	

public void avoidBlock(){

	navigator.turnTo(-Math.PI/2);
	navigator.drive(30);
	navigator.turnTo(Math.PI/2);
	
	usSensor.getDistanceMode().fetchSample(usData, 0);
	currentDist[0]=(int)(usData[0]*100.0);
	if(currentDist[0]<=minDist)
		avoidBlock();
	else{
	navigator.drive(40);
	}
  }
}
