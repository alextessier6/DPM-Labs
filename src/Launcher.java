import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

//
//DPM Lab 5 - Ball Launcher - Launcher class
//Written by Alexandre Tessier and Tyrone Wong
//This class is responsible for, first of all, getting the robot to face the appropriate target. It then implements commands to get the
//robot to launch a ball.

public class Launcher {
	public static int ROTATE_SPEED = 150;
	public static int MaxSpeed = 2000;  		// Arbitrary value of 2000 to make sure the motors go as fast as they can
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private EV3LargeRegulatedMotor catapult;
	public static int launchAngle=130;
	public static double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 14.2;
	int buttonChoice;
	
	//angle orientation
	double Orientation;
	
	// Collects the resources (3 motors) used by the launcher class
	public Launcher(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,EV3LargeRegulatedMotor catapult) {
		this.leftMotor=leftMotor;
		this.rightMotor=rightMotor;
		this.catapult=catapult;	
	}
	
	//methods for shooting when the robot is already facing the target (target placed in the center)
	public void middleShot(){
		// Sets the speed of the three motors
		catapult.setSpeed(MaxSpeed);
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		// Launches the ball
		do{
			// Wait for the user to press the enter button
			buttonChoice = Button.waitForAnyPress();		
			if(buttonChoice==Button.ID_ENTER){
				//throw the ball by rotating the motor used for the catapult by an angle (negative to have the catapult arm rotate forward)
				catapult.rotate(-launchAngle);	
			
				//return to launch position
				catapult.rotate(launchAngle);	
			}	
			
		// Will keep launching balls when the enter button is pressed until the user asks to exit the program	
		}while(buttonChoice != Button.ID_ESCAPE);	
	}
	
	public void leftShot(){
		
		// Calculate the angle by which the robot must rotate in order to face the left target
		Orientation=Math.atan2(-1, 3);
		
		// Set the speed of the three motors
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		catapult.setSpeed(MaxSpeed);

		// Rotate the robot by the appropriate angle in order to face the left target
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK,Math.toDegrees(Orientation)), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK,Math.toDegrees(Orientation)), false);
		
		// Launches the ball
		do{
			// Wait for the user to press the enter button
			buttonChoice = Button.waitForAnyPress();		
			if(buttonChoice==Button.ID_ENTER){
				//throw the ball by rotating the motor used for the catapult by an angle (negative to have the catapult arm rotate forward)
				catapult.rotate(-launchAngle);	
					
				//return to launch position
				catapult.rotate(launchAngle);	
			}	
					
		// Will keep launching balls when the enter button is pressed until the user asks to exit the program	
		}while(buttonChoice != Button.ID_ESCAPE);
	}	
	
	public void rightShot(){
		
		// Calculate the angle by which the robot must rotate in order to face the right target
		Orientation=Math.atan2(1, 3);;
		
		// Set the speed of the three motors
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		catapult.setSpeed(MaxSpeed);

		// Rotate the robot by the appropriate angle in order to face the right target
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(Orientation)), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(Orientation)), false);
		
		
		// Launches the ball
		do{
			// Wait for the user to press the enter button
			buttonChoice = Button.waitForAnyPress();		
			if(buttonChoice==Button.ID_ENTER){
				//throw the ball by rotating the motor used for the catapult by an angle (negative to have the catapult arm rotate forward)
				catapult.rotate(-launchAngle);	
							
				//return to launch position
				catapult.rotate(launchAngle);	
			}	
							
		// Will keep launching balls when the enter button is pressed until the user asks to exit the program	
		}while(buttonChoice != Button.ID_ESCAPE);
	}	
	
	// Methods provided to convert angle into readable data for the motors
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));	
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	


}