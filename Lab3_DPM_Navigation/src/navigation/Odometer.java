/*
 * Odometer.java
 */

package navigation;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Odometer extends Thread {
	// robot position
	private double x, y, theta;
//	private int leftMotorTachoCount, rightMotorTachoCount;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;
	private int LTachoLast, RTachoLast, LTachoNow, RTachoNow;
	private double distL, distR, deltaDist, deltaTheta, dx, dy;
	private static double Wheel_Radius=2.1;
	private static double Wheel_Base=14.2;
	
	// lock object for mutual exclusion
	private Object lock;

	// default constructor
	public Odometer(EV3LargeRegulatedMotor leftMotor,EV3LargeRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
			
		//Setting initial values for tachometer values. Takes into account the most recent and the last tacho values
		LTachoLast=0;
		RTachoLast=0;
		LTachoNow=0;
		RTachoNow=0;
		
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

	
		// While loop keeps track of wheel rotation/tachometer readings in order to constantly update the display settings
		//of the x,y and theta values in respect to the initial position.
		
		while (true) {
			updateStart = System.currentTimeMillis();
			
			//Assigning the Tachometer values from the left and right values
			LTachoNow=leftMotor.getTachoCount();
			RTachoNow=rightMotor.getTachoCount();
			
			
			//Taking the wheel rotation with the wheel radius and calculating distance traveled using most recent tachometer values
			distL= Math.PI*Wheel_Radius*(LTachoNow-LTachoLast)/180;
			distR= Math.PI*Wheel_Radius*(RTachoNow-RTachoLast)/180;
		
			//reassigning the Tachometer values with each while loop run 
			LTachoLast=LTachoNow;
			RTachoLast=RTachoNow;
			
			
			//Finding out the change in distance based on the average distance traveled by each wheel
			deltaDist=0.5*(distL+distR);
			
			//Finding out the change in angle with respect to the original using the distance between wheels
			deltaTheta=(distL-distR)/Wheel_Base;
			
			// Setting the display values to update
			synchronized (lock) {
				
				theta += deltaTheta;
				
				//Getting the distance moved in x and y
				dx = deltaDist * Math.sin(theta);
				dy = deltaDist * Math.cos(theta);
				
				x += dx;
				y += dy;
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] position) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
				position[0] = x;
				position[1] = y;
				position[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}