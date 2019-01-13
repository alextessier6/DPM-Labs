import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.lcd.TextLCD;

//
//DPM Lab 5 - Ball Launcher - Main class
//Written by Alexandre Tessier and Tyrone Wong
//This class is responsible for calling the appropriate methods in order to have the robot launch a ball. The main class is designed to 
//take user inputs regarding the location of the target and to act accordingly.
//

public class Lab5 {
	static int buttonChoice;
	
	// Static Resources:
	// Left motor connected to output A
	// Right motor connected to output D
	// Catapult motor connected to output C
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	private static final EV3LargeRegulatedMotor catapult = new	EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	
	public static void main(String[] args) {
		
		Launcher shoot=new Launcher(leftMotor,rightMotor,catapult);
		final TextLCD t= LocalEV3.get().getTextLCD();
		
			// clear the display
			t.clear();

			// ask the user whether the robot should aim for the left, center, or right target
			t.drawString("<L| C |R>", 2, 2);
			
			// waits for user input
			buttonChoice = Button.waitForAnyPress();
			
			// clear the display
			t.clear();
			
			// tells the user to press any button for the robot to shoot the ball
			t.drawString("Press for", 0, 1);
			t.drawString("  shot", 0, 2);
		
		
		// If the user chooses the left target, the appropriate method is called (robot turns left and shoots)
		if(buttonChoice==Button.ID_LEFT){
			shoot.leftShot();
		}
		
		// If the user chooses the right target, the appropriate method is called (robot turns right and shoots)
		else if(buttonChoice==Button.ID_RIGHT){
			shoot.rightShot();
		}
		
		// If the user chooses the center target, the appropriate method is called (robot only shoots)
		else if (buttonChoice==Button.ID_ENTER){
			shoot.middleShot();	
		}
		
		// Checks if button is pressed to exit program and return to main menu
		while (Button.waitForAnyPress() != Button.ID_ESCAPE){
			System.exit(0);
		}
}
}