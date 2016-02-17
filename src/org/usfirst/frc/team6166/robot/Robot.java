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
	
	public boolean isSwitchSet() {
		return counter.get() > 0;
	}
	
	public void initializeCounter() {
		counter.reset();
	}

	public void robotInit() {
		
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
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam0",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
    }   
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
	
	
	
    public void autonomousInit() {
    	
    	n = 1;	// n = duration in seconds.
    	X = 0;
    	Y = 0;
    	Z = 0;
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	SmartDashboard.putNumber("Time - " + n + " Seconds", X);
    	if(X < 50 * n) {//based on this, 50n = ~n second    		
    		chassis.drive(-0.5, 0.0);
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
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	
    }

    /**
     * 
     * This function is called periodically during operator control
     * @see edu.wpi.first.wpilibj.IterativeRobot#teleopPeriodic()
     * @param
     * @author Holmen Robotics
     * @version 2/5/2016
     *   
     */
	public void teleopPeriodic() {    	
    	NIVision.IMAQdxStartAcquisition(session);

        while (isOperatorControl() && isEnabled()) {
        	NIVision.IMAQdxGrab(session, frame, 1);                
        	CameraServer.getInstance().setImage(frame);
        	chassis.arcadeDrive(rightStick, true);
        	//chassis.tankDrive(rightStick, leftStick);
        	
        	if(rightStick.getRawButton(3)){//Arm Height Down
        		armHeight.set(0.125);        		
            }
            
            if(rightStick.getRawButton(4)){//Arm Tilt Down
            	armTilt.set(0.1);
            	initializeCounter();
            } else if(armUp == true) {
            	armTilt.set(-.05);
            }
            
            if(rightStick.getRawButton(5)) {//Arm Height Up
            	armHeight.set(-0.125);
            }
            
            if(rightStick.getRawButton(6) && armUp != true) {//Arm Tilt Up
            	armTilt.set(-0.2);
            }            
            
        }
                        
    	
    	//Display to dash board
    	/*SmartDashboard.putBoolean("Right Stick - Button 1", rightStick.getRawButton(1));
    	SmartDashboard.putBoolean("Right Stick - Button 2", rightStick.getRawButton(2));
    	SmartDashboard.putBoolean("Right Stick - Button 3", rightStick.getRawButton(3));
    	SmartDashboard.putBoolean("Right Stick - Button 4", rightStick.getRawButton(4));
    	
    	SmartDashboard.putNumber("Right Stick - X Axis", rightStick.getRawAxis(0));
    	SmartDashboard.putNumber("Right Stick - Y Axis", rightStick.getRawAxis(1));
    	SmartDashboard.putNumber("Right Stick - Twist", rightStick.getRawAxis(2));
    	SmartDashboard.putNumber("Right Stick - Slider", rightStick.getRawAxis(3));
    	SmartDashboard.putNumber("Right Stick - POV", rightStick.getPOV());*/
    	
    	//arcade drive:
        //chassis.arcadeDrive(rightStick);
        
        //tank drive:
        //chassis.setSafetyEnabled(true);
    	
    	
        //data to dash board
        SmartDashboard.putNumber("Chassis", X);         
        NIVision.IMAQdxStopAcquisition(session);  
         
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}