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



/**
 ************************************************** NOTES **************************************************
 *                                          MOTOR PORT INFORMATION                                         
 *                                                                                                         
 *           Number    :            Motor             -      Controller type     -      Color              
 *       ============================================================================================      
 *       =                                       Drive Train                                        =      
 *       =     0       :         right front          -  Spark motor controller  -       RED        =      
 *       =     1       :         right rear           -  Spark motor controller  -       RED        =      
 *       =     2       :          left front          -  Spark motor controller  -      BLACK       =      
 *       =     3       :          left rear           -  Spark motor controller  -      BLACK       =      
 *       =                                                                                          =      
 *       =     4       :           arm control        -  Victor motor controller -      BLACK       =      
 *       ============================================================================================      
 *                                                                                                          
 *                                            CODE WE DIDN'T USE
 *                        Category     -           Function             -     Reason                                                                              
 *       
 *                          Vision     -      Shape in camera view      -     Didn't help us accomplish our tasks any better
 * 
 * frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
 * the camera name (ex "cam0") can be found through the roborio web interface
 * session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
 * NIVision.IMAQdxConfigureGrab(session);
 * NIVision.IMAQdxGrab(session, frame, 1);
 * 
 *                         Driving     -      Gyroscopic Turning        -     Gyroscope not functioning as we imagined
 *  
 *  if(angle >= 85 && angle <= 95) {
 *   		turnComplete = true;
 *   	}
 *   	else if(angle > 270 && angle <= 360 && turnComplete == false ) {//based on this, 50n = ~n second    		
 *   		//chassis.tankDrive(-0.15, 0.15);
 *   		//chassis.drive(-.375, 1);
 *   		chassis.drive(-.275, -1);
 *			//armTilt.set(-0.5);
 *			X++;
 *   	}
 *   	else if(angle >= 0 && angle <85 && turnComplete == false){
 *   		
 *   		//chassis.tankDrive(-0.15, 0.15);
 *   		chassis.drive(-0.375, 1);
 *   		chassis.drive(0.375, -1);
 *   		chassis.drive(-.275, -1);
 *   		//chassis.drive(.375, -1);
 *   	}
 *   	else if(angle > 95 && angle < 270 && turnComplete == false) {
 *   		
 *   		//chassis.tankDrive(0.15, -0.15);
 *   		chassis.drive(-0.375, -1);
 *   		chassis.drive(0.375, 1);
 *   	} else {
 *   		chassis.drive(.275, 1);
 *   		//chassis.drive(-.375, -1);
 *   	}
 *   	else {
 *   		
 *   		chassis.drive(0.0, 0.0);
 *   	}
 * 
 *       
 */

public class Robot extends IterativeRobot {

	RobotDrive chassis;// = new RobotDrive(0,1);
	RobotDrive controlArm;
	Joystick rightStick;// = new Joystick(0);
	Joystick leftStick;// = new Joystick(1);

	int n;
	int X;
	int Y;
	int Z;
	int Q;
	int W;
	int V;
	int I;
	
	boolean turnComplete = false;
	boolean armUp;	
	boolean armDown;
	boolean autonomousTask1;
	
	DigitalInput limitSwitch1 = new DigitalInput(0);
	DigitalInput limitSwitch2 = new DigitalInput(1);
	
	Counter counter1 = new Counter(limitSwitch1);
	Counter counter2 = new Counter(limitSwitch2);
	
	
	
	
	int session;
	Image frame;

	//Drive Train Motor Controllers
	Spark frontRight = new Spark(0);	// Right Front
	Spark rearRight = new Spark(1);		// Right Rear
	Spark frontLeft = new Spark(2);		// Left Front
	Spark rearLeft = new Spark(3);		// Left Rear			

	//Arm Motors
	VictorSP arm = new VictorSP(4);	// Arm Control

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	private AnalogGyro gyro;
	double Kp = 0.000000000008;

	public boolean isSwitchSet1() {

		return counter1.get() > 0;	
	}
	
	public boolean isSwitchSet2() {
		return counter2.get() > 0;
	}

	public void initializeCounter1() {

		counter1.reset();
	}
	
	public void initializeCounter2() {
		
		counter2.reset();
	}

	public void robotInit() {

		gyro = new AnalogGyro(1);
		chassis = new RobotDrive(rearLeft,frontLeft,rearRight,frontRight);

		rightStick = new Joystick(0);
		leftStick = new Joystick(1);

		//Currently all of these need to be inverted for it to work
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);    	    	
		chassis.setExpiration(0.1);
		
		//vision                
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
		Q = 0;
		W = 0;
		V = 0;
		I = 0;
		autonomousTask1 = false;
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		

    	gyro.reset();
    	
		//Only Run one obstacle at a time!
    	
    	//Calibrated:
    	
		//autonomousRoughTerrain();
		//autonomousStraight();
		//autonomousPortCullis();
		//autonomousLowBar();
    	
    	
    	//Iffy, not used in practice/qualifier yet:
		
    	//autonomousMoat();    	
    	//autonomousShovelTheFries();
		
    	
    	//Not even a little bit:
    	
		//autonomousRamparts();
	}

	private void autonomousStraight() {

		double angle = gyro.getAngle();
		
		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 1.5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.4, angle*Kp);
			X++;
		}
	}
	
	private void autonomousMoat() {
		double angle = gyro.getAngle();
		
		if(X < 50 * n * 2) {
			chassis.drive(-.5, angle*Kp);
			X++;
		}
	}
	
	private void autonomousPortCullis() {
		double angle = gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);		
		if(X < 50 * n * .5) {//based on this, 50n = ~n second
			arm.set(.3);
			X++;
		} else if (Y < 50 * n * 3){
			chassis.drive(-.35, angle*Kp);
			Y++;
		} else if (Z < 50 * n * .5){
			arm.set(-.3);
			Z++;
		}
	}

	private void autonomousLowBar() {

		double angle = gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 2) {//based on this, 50n = ~n second    		
			chassis.drive(-0.4, angle*Kp);
			X++;
		}
	}

	private void autonomousRoughTerrain() {

		double angle = gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " + n + " Seconds", X);
		if(X < 50 * n * 2.5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.4, angle*Kp);
			X++;
		}
	}
	
	private void autonomousRamparts() {

		double angle = gyro.getAngle();

		if(X < 50 * n * 5) {//based on this, 50n = ~n second    		
			chassis.drive(-0.25, angle*Kp);
			X++;
    	}
    }
	
	private void autonomousShovelTheFries() {
		double angle = gyro.getAngle();
		
		if (X < 50 * n * 2.015) {				//previously n * 2.015, 2.03, 2.0, 2.05, 1.85 1.75, 1.7, 1.5
			chassis.drive(-.25, angle * Kp);
			X++;
		} else if(Y < 50 * n * .2) {
			chassis.drive(0.0, angle * Kp);		//stops robot, lowers arm
			arm.set(0.25);
			Y++;
		} else if (V < 50 * n * .5) {
			chassis.drive(0.0, angle * Kp);		//stops robot and arm movement
			V++;
		} else if (Z < 50 * n * .6) {
			chassis.drive(-0.75, angle * Kp); 	//Power to climb over chevalt de fries, previously -.5, -.25
			Z++;
		} else if (Q < 50 * n * .2) {
			chassis.drive(-0.35, angle * Kp);	//drives robot over chevalt
			Q++;
		} else if (W < 50 * n * 1) {
			chassis.drive(-0.35, angle * Kp);
			W++;
		} else if (I < 50 * n * .1) {
			arm.set(-0.25);						//brings arm up
			I++;
		}
		
	}

	/**
	 * This function is called once each time the robot enters tele-operated mode
	 */
	public void teleopInit() {
		gyro.reset();
	}

	/**
	 * This function is called periodically during operator control
	 * 
	 * @see edu.wpi.first.wpilibj.IterativeRobot#teleopPeriodic()
	 * @param
	 * @author Holmen Robotics
	 * @version 4/30/2016
	 *   
	 */

	public void teleopPeriodic() {
		double angle = gyro.getAngle();
    	SmartDashboard.putDouble("Angle", angle);
		armUp = isSwitchSet1();
		armDown = isSwitchSet2();

		chassis.arcadeDrive(rightStick, true);

		if(rightStick.getRawButton(3) && armUp != true) {
			arm.set(-0.30);
			initializeCounter2();			
		} else if (armDown==true){
			arm.set(0.0);
			initializeCounter2();
		}
		

		if(rightStick.getRawButton(5) && armDown != true){
			arm.set(0.3);
			initializeCounter1();
		} else if (armUp==true){
			arm.set(0.0);    
			initializeCounter1();
		}		 
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
}