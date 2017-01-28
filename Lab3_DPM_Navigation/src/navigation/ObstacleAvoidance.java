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
	private Driver avoider;
	private EV3UltrasonicSensor usSensor;
	private int minDist=8;
	private int[] currentDist = new int[4];
	public float[] usData = new float[4];
	public static boolean block = false;
	
	 
	
	public ObstacleAvoidance (Odometer odometer, EV3UltrasonicSensor usSensor, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Driver navigator, Driver avoider){
		this.odometer =  odometer;
		this.leftMotor=leftMotor;
		this.rightMotor=rightMotor;
		this.usSensor=usSensor;
		this.navigator=navigator;
		this.avoider=avoider;
		
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
				block = true;
				navigator.setisnav(true);
				avoidBlock();
				
				leftMotor.stop();
				rightMotor.stop();
				
//				try { Thread.sleep(5000); } catch(Exception e){}
				
//				if(navigator.navigating==true){
//				leftMotor.stop();
//				rightMotor.stop();
//				}
//				
//				if(navigator.navigating==false){
//					leftMotor.backward();
//					rightMotor.backward();
//				}
				avoider.travelTo(Lab3.travellingtox, Lab3.travellingtoy);
				block = false;
				navigator.setisnav(false);
			}
			
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
	
	
	avoider.turnTo(Math.PI/2);
	avoider.drive(30);
	avoider.turnTo(-Math.PI/2);
	
//	usSensor.getDistanceMode().fetchSample(usData, 0);
//	currentDist[0]=(int)(usData[0]*100.0);
//	if(currentDist[0]<=minDist)
//		avoidBlock();
//	else{
	avoider.drive(40);
	
//	}
  }
}
