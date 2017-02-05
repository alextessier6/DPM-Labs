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
			
			
			// rotate the robot until it sees no wall
			rotateAwayWall(true, noWall);
			// keep rotating until the robot sees a wall, then latch the angle
			rotateToWall(true, wallDist);
			
//			try { Thread.sleep(5000); } catch(Exception e){}
			angleA = odo.getAng();
					
			// switch direction and wait until it sees no wall
			rotateAwayWall(false, noWall);

			// keep rotating until the robot sees a wall, then latch the angle
			rotateToWall(false, wallDist);
			
//			try { Thread.sleep(5000); } catch(Exception e){}
			angleB = odo.getAng();
			
			if (angleA<angleB)
				deltaTheta = 45 - (angleA+angleB)/2;
			else
				deltaTheta = 225 - (angleA+angleB)/2;
			

			
			odo.getPosition(pos);
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			
			double newAngle = deltaTheta + pos[2];
			
//			try { Thread.sleep(5000); } catch(Exception e){}
			
//			navigator.setSpeeds(0,0);
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, newAngle}, new boolean [] {true, true, true});
			navigator.turnTo(0, true);
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			rotateAwayWall(true, noWall);
			
			rotateToWall(false, wallDist);
			rotateAwayWall(false, noWall);
			
			angleA = odo.getAng();
			
			rotateToWall(true, wallDist);
			rotateAwayWall(true, noWall);
			
			angleB = odo.getAng();
			
			if (angleA<angleB)
				deltaTheta = 45 - (angleA+angleB)/2 + 180;
			else
				deltaTheta = 225 - (angleA+angleB)/2 + 180;
			
			odo.getPosition(pos);
			double newAngle = deltaTheta + pos[2];
			
			odo.setPosition(new double [] {0.0, 0.0, newAngle}, new boolean [] {true, true, true});
			navigator.turnTo(0, true);
			
		}
	}
	
	private void rotateToWall(boolean right, int dist){
		
		if(right==true)
		navigator.setSpeeds(rotSpeed, -rotSpeed);
		
		else
		navigator.setSpeeds(-rotSpeed, rotSpeed);
		
		while(usData[0]>dist){
			usData[0]=getFilteredData();
		}
		
		navigator.setSpeeds(0, 0);
		
	}
	
	private void rotateAwayWall(boolean right, int dist){
		
		if(right==true)
			navigator.setSpeeds(rotSpeed, -rotSpeed);
			
		else
		navigator.setSpeeds(-rotSpeed, rotSpeed);
		
		while(usData[0]<dist){
			usData[0]=getFilteredData();
		}
		
		navigator.setSpeeds(0, 0);
	}

	private float getFilteredData() {
				
		usSensor.fetchSample(usData, 0);
	
		float distance = usData[0]*100;
		int filterControl=0;
		int FILTER_OUT = 10;
		
//		if(distance >= 21474)
//			distance=50;
//		else if (distance >= wallDist+40 && filterControl < FILTER_OUT) {
//			// bad value, do not set the distance var, however do increment the
//			// filter value
//			filterControl++;
//		} else if (distance >= wallDist+40 && filterControl > FILTER_OUT) {
//			// We have repeated large values, so there must actually be nothing
//			// there: leave the distance alone
//			return distance;
//		}	
//		 else {
//			// distance went below 255: reset filter and leave
//			// distance alone.
//			filterControl = 0;
//			return distance;
//			
//		}
		return distance;
	}

}
