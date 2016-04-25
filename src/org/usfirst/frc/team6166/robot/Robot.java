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

	public static RobotDrive chassis;// = new RobotDrive(0,1);
	RobotDrive controlArm;
	Joystick rightStick;// = new Joystick(0);
	Joystick leftStick;// = new Joystick(1);

	public static int n;
	public static int X;
	public static int Y;
	public static int Z;
	public static int Q;
	public static int W;
	public static int V;
	public static int I;
	
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

	//Chassis Motor Controllers
	Spark frontRight = new Spark(0);	// Right Front
	Spark rearRight = new Spark(1);		// Right Rear
	Spark frontLeft = new Spark(2);		// Left Front
	Spark rearLeft = new Spark(3);		// Left Rear			

	//Arm Motors
	static VictorSP arm = new VictorSP(4);	// Arm Control

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public static AnalogGyro gyro;
	public static double Kp = 0.000000000008;//delete 0 zero(s) to revert

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

		/*Currently all of these need to be inverted for it to work.*/
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		chassis.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);    	    	
		chassis.setExpiration(0.1);
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
		Autonomous.PortCullis();
		//autonomousLowBar();
    	
    	//Calibrated But Not Tested:
    	
    	//autonomousShovelTheFries();
    	
    	//Not Calibrated:
    	
    	//autonomousMoat();
		//autonomousRamparts();
		//autonomousTurn90Left();
		//autonomousTurn90Right();
		//autonomousTurn180Left();
		//autonomousTurn180Right();
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
	 * @version 2/5/2016
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
