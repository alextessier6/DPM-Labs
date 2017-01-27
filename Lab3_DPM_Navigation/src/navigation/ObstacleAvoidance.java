package navigation;

public class ObstacleAvoidance extends Thread {
	private static final int ROTATE_SPEED = 150;
	private static final int FORWARD_SPEED = 250;
	public avoid ()
}

public blockDetection(){
	
}

public avoidBlock(){
	leftMotor.setSpeed(ROTATE_SPEED);
	rightMotor.setSpeed(ROTATE_SPEED);

	leftMotor.rotate(convertAngle(leftRadius, width, 90.0), true);
	rightMotor.rotate(-convertAngle(rightRadius, width, 90.0), false);
	
	leftMotor.setSpeed(FORWARD_SPEED);
	rightMotor.setSpeed(FORWARD_SPEED);
	
	leftMotor.rotate(100,true);
	rightMotor.rotate(100,false);
	
	leftMotor.setSpeed(ROTATE_SPEED);
	rightMotor.setSpeed(ROTATE_SPEED);

	leftMotor.rotate(convertAngle(leftRadius, width, -90.0), true);
	rightMotor.rotate(-convertAngle(rightRadius, width, -90.0), false);
	
	
}
