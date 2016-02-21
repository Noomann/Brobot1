package org.usfirst.frc.team6166.robot;

import edu.wpi.first.wpilibj.DigitalInput;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;	//Arm Motors
import edu.wpi.first.wpilibj.Spark;		//Drive Motors
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.*;


/**
 * MOTOR PORT INFORMATION
 * 
 * 0: right front - Spark motor controller - RED
 * 1: right rear - Spark motor controller - RED
 * 
 * 2: left front - Spark motor controller - BLACK
 * 3: left rear - Spark motor controller - BLACK
 * 
 * 4: front height - Victor motor controller 
 * 5: front tilt - Victor motor controller
 * 
 */

public class Robot extends IterativeRobot {

	RobotDrive chassis;// = new RobotDrive(0,1);
	RobotDrive controlArmHeight;
	RobotDrive controlArmTilt;
	Joystick rightStick;// = new Joystick(0);
	Joystick leftStick;// = new Joystick(1);

	int n;
	int X;
	int Y;
	int Z;

	double joystickPOV;
	DigitalInput limitSwitch = new DigitalInput(0);
	Counter counter = new Counter(limitSwitch);
	boolean armUp;	

	int session;
	Image frame;

	//Chassis Motor Controllers
	Spark frontRight = new Spark(0);	// Right Front
	Spark rearRight = new Spark(1);		// Right Rear
	Spark frontLeft = new Spark(2);		// Left Front
	Spark rearLeft = new Spark(3);		// Left Rear			

	//Arm Motors
	VictorSP armHeight = new VictorSP(4);	// Arm Height
	VictorSP armTilt = new VictorSP(5);		// Arm Tilt

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	private AnalogGyro gyro;
	double Kp = 0.03;

	public boolean isSwitchSet() {

		return counter.get() > 0;		
	}

	public void initializeCounter() {

		counter.reset();
	}

	public void robotInit() {

		gyro = new AnalogGyro(0);
		chassis = new RobotDrive(rearLeft,frontLeft,rearRight,frontRight); 
		controlArmHeight = new RobotDrive(armHeight,armHeight);
		controlArmTilt = new RobotDrive(armTilt,armTilt);


		rightStick = new Joystick(0);
		leftStick = new Joystick(1);

		/*Currently all of these need to be inverted for it to work.*/
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);    	    	

		//vision
		/*frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		// the camera name (ex "cam0") can be found through the roborio web interface
		session = NIVision.IMAQdxOpenCamera("cam0",
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxGrab(session, frame, 1);*/                
		CameraServer.getInstance().startAutomaticCapture("cam0");
	}   

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */



	public void autonomousInit() {

		gyro.reset();

		n = 1;	// n = duration in seconds.
		X = 0;
		Y = 0;
		Z = 0;

	}

	/**
	 * This function is called periodically during autonomous
	 */
	/*private void autonomousExample() {

		double angle = gyro.getAngle();
		gyro.reset();

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);    	
		if(X < 50 * n * 5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.25, angle*Kp);
			//armTilt.set(-0.5);
			X++;
		} else if (Y < 50 * n) {

			//chassis.drive(-0.25, 0.0);  // drive forwards half speed (- is forward, + backward) (-0.5,0))
			//armHeight.set(-0.5);
			Y++;
		} else if (Y < 50 * n * 2) {

			//chassis.drive(0.0, 0.0);					
			//armHeight.set(-0.5);
			Y++;
		} else if (Z < 50 * n * .5) {

			//armTilt.set(0.125);
			//armHeight.set(0.125);
			Z++;
		} else {

			chassis.drive(0.0, 0.0); 	// stop robot
		}
	}*/

	public void autonomousPeriodic() {

		//Only Run one obstacle at a time!

		//autonomousRoughTerrain();
		//autonomousRamparts();
		//autonomousBoop();
		//autonomousPortCullis();
		autonomousLowBar();
		//autonomousTurn90Left();
		//autonomousTurn90Right();
		//autonomousTurn180Left();
		//autonomousTurn180Right();
	}

	private void autonomousBoop() {

		double angle = gyro.getAngle();
		gyro.reset();

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 4) {//based on this, 50n = ~n second    		
			chassis.drive(-0.25, angle*Kp);
			X++;
		}
	}
	
	private void autonomousPortCullis(){
		double angle = gyro.getAngle();
		gyro.reset();

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);		
		if(X < 50 * n * .5) {//based on this, 50n = ~n second
			armHeight.set(.4);
			//chassis.drive(-0.25, angle*Kp);
			X++;
		} else if (Y < 50 * n * 4){
			chassis.drive(-.35, angle*Kp);
			Y++;
		} else if (Z < 50 * n * .5){
			armHeight.set(-.4);
			Z++;
		}
	}

	private void autonomousLowBar() {

		double angle = gyro.getAngle();
		gyro.reset();

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.3, angle*Kp);
			X++;
		}
	}

	private void autonomousRoughTerrain() {

		double angle = gyro.getAngle();
		gyro.reset();

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 2.5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.4, angle*Kp);
			X++;
		}
	}

	/*private void autonomousMoat() {//WARNING NOT CALIBRATED DO NOT USE

    	double angle = gyro.getAngle();
    	gyro.reset();

    	SmartDashboard.putNumber("Time - " + n + " Seconds", X);
    	if(X < 50 * n * 2) {//based on this, 50n = ~n second    		
    		chassis.drive(-0.4, angle*Kp);
			//armTilt.set(-0.5);
			X++;
		}
    }*/

	private void autonomousRamparts() {//WARNING NOT CALIBRATED DO NOT USE

		double angle = gyro.getAngle();
		gyro.reset();

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.25, angle*Kp);
			//armTilt.set(-0.5);
			X++;
		}
	}

	private void autonomousTurn90Left() {
		double angle = gyro.getAngle();
		gyro.reset();
		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(angle > 270 && angle <= 360) {//based on this, 50n = ~n second    		
			chassis.tankDrive(0.15, -0.15);
			//armTilt.set(-0.5);
			X++;
		}
	}

	private void autonomousTurn90Right() {

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 2) {//based on this, 50n = ~n second    		
			chassis.tankDrive(-0.15, 0.15);
			//armTilt.set(-0.5);
			X++;
		}
	}

	private void autonomousTurn180Left() {

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 4) {//based on this, 50n = ~n second    		
			chassis.tankDrive(0.15, -0.15);
			//armTilt.set(-0.5);
			X++;
		}
	}

	private void autonomousTurn180Right() {

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 4) {//based on this, 50n = ~n second    		
			chassis.tankDrive(-0.15, 0.15);
			//armTilt.set(-0.5);
			X++;
		}
	}

	/**
	 * This function is called once each time the robot enters tele-operated mode
	 */
	public void teleopInit() {

	}

	/**
	 * This function is called periodically during operator control
	 * 
	 * @see edu.wpi.first.wpilibj.IterativeRobot#teleopPeriodic()
	 * @param
	 * @author Holmen Robotics
	 * @version 2/5/2016
	 *   
	 */

	public void teleopPeriodic() {

		armUp = isSwitchSet();
		joystickPOV = rightStick.getPOV();

		chassis.arcadeDrive(rightStick, true);

		if(rightStick.getRawButton(3)) {
			armHeight.set(-0.40);
		}

		if(rightStick.getRawButton(5)){
			armHeight.set(0.3);        		
		}

		if(joystickPOV == 0.0)//not functioning
		{
			chassis.arcadeDrive(-0.25, 0.0);
		}

		if(joystickPOV == 45.0)
		{
			chassis.arcadeDrive(-0.15, 45.0);
		}

		if(joystickPOV == 90.0)//not functioning
		{
			chassis.arcadeDrive(-0.25, 45.0);
		}

		if(joystickPOV == 135.0)
		{
			chassis.arcadeDrive(0.15, -45.0);
		}

		if(joystickPOV == 180.0)//not functioning
		{
			chassis.arcadeDrive(0.25, 0.0);
		}

		if(joystickPOV == 225.0)
		{
			chassis.arcadeDrive(0.15, 45.0);
		}

		if(joystickPOV == 270.0) {//not functioning{

			chassis.arcadeDrive(0.25, 45.0);
		}

		if(joystickPOV == 315.0) {

			chassis.arcadeDrive(-0.15, -45.0);
		}  

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

}