package org.usfirst.frc.team6166.robot;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Victor;//arm motors
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */



/**
 * MOTOR PORT INFORMATION
 * 
 * 0: right front - Spark motor controller
 * 1: right rear - Spark motor controller
 * 
 * 2: left front - Spark motor controller
 * 3: left rear - Spark motor controller
 * 
 * 4: front height - Victor motor controller
 * 5: front tilt - Victor motor controller
 * 
 */

public class Robot extends IterativeRobot {
	//public static OI oi;
	RobotDrive chassis;// = new RobotDrive(0,1);
	RobotDrive controlArmHeight;
	RobotDrive controlArmTilt;
	Joystick rightStick;// = new Joystick(0);
	Joystick leftStick;// = new Joystick(1);
	int n;
	int autoLoopCounter;
	int session;
    Image frame;
	/*
	 * ENCODERS
	 * 
	 * 
	 * Explanation:
	 * 
	 * Setting encoder parameters:
	 * 
	 * Encoder encoder1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	 * encoder1.setMaxPeriod(.1);
	 * encoder1.setMinRate(10);
	 * encoder1.setDistancePerPulse(5);
	 * encoder1.setReverseDirection(true);
	 * encoder1.setSamplesToAverage(7);
	 * 
	 * Reset Encoder:
	 * 
	 * encoder1.reset();
	 * 
	 * Getting encoder values:
	 * 
	 * int count = sampleEncoder.get();
	 * double distance = sampleEncoder.getRaw();
	 * double distance = sampleEncoder.getDistance();
	 * double period = sampleEncoder.getPeriod();
	 * double rate = sampleEncoder.getRate();
	 * boolean direction = sampleEncoder.getDirection();
	 * boolean stopped = sampleEncoder.getStopped();
	 * 
	 */
	
	 /*
	  * ANALOG INPUT
	  * 
	  * AnalogInput ai;
	  * ai = new AnalogInput(0);
	  * 
	  */
		
	Spark rearLeft = new Spark(3);		// Left Rear
	Spark rearRight = new Spark(1);		// Right Rear
	
	Victor armHeight = new Victor(4);	// Arm Height
	Victor armTilt = new Victor(5);		// Arm Tilt
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */

	public void robotInit() {
		
    	chassis = new RobotDrive(rearLeft,rearRight);//Ports 1,3 
    	controlArmHeight = new RobotDrive(armHeight,armHeight);
    	controlArmTilt = new RobotDrive(armTilt,armTilt);
    	
    	//!!!!!!ONLY FRONT LEFT AND RIGHT
    	
    	rightStick = new Joystick(0);
    	leftStick = new Joystick(1);
    	
    	/*Delete this to fix*/
    	//chassis.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
    	chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	//chassis.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    	chassis.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);    	    	
    	/*Don't delete after this*/
    	
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
    	
    	n = 3;
    	autoLoopCounter = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	SmartDashboard.putNumber("Time - " + n + " Seconds", autoLoopCounter);
    	if(autoLoopCounter < 50 * n) {//based on this, 50n = ~n second		
			chassis.drive(0.0, 0.0); 	// drive forwards half speed (- is forward, + backward) (-0.5,0)
			autoLoopCounter++;
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
     * This function is called periodically during operator control
     */
    @SuppressWarnings("deprecation")
	public void teleopPeriodic() {
    	NIVision.IMAQdxStartAcquisition(session);

        /**
         * grab an image, draw the circle, and provide it for the camera server
         * which will in turn send it to the dashboard.
         */
        NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
         while (isOperatorControl() && isEnabled()) {
        	 NIVision.IMAQdxGrab(session, frame, 1);
        	 /*NIVision.imaqDrawShapeOnImage(frame, frame, rect,
        			 DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);*/
                
        	 CameraServer.getInstance().setImage(frame);            
    	
    	//Display to dashboard
    	SmartDashboard.putBoolean("Right Stick - Button 1", rightStick.getRawButton(1));
    	SmartDashboard.putBoolean("Right Stick - Button 2", rightStick.getRawButton(2));
    	SmartDashboard.putBoolean("Right Stick - Button 3", rightStick.getRawButton(3));
    	SmartDashboard.putBoolean("Right Stick - Button 4", rightStick.getRawButton(4));
    	
    	SmartDashboard.putNumber("Right Stick - X Axis", rightStick.getRawAxis(0));
    	SmartDashboard.putNumber("Right Stick - Y Axis", rightStick.getRawAxis(1));
    	SmartDashboard.putNumber("Right Stick - Twist", rightStick.getRawAxis(2));
    	SmartDashboard.putNumber("Right Stick - Slider", rightStick.getRawAxis(3));
    	SmartDashboard.putNumber("Right Stick - POV", rightStick.getPOV());
    	
    	//arcade drive:
        chassis.arcadeDrive(rightStick);
        
        //data to dash board
        SmartDashboard.putNumber("Chassis", autoLoopCounter);
        
        //buttons
        if(rightStick.getRawButton(1)){//Forward
            //chassis.drive(-0.375, 0.0);
        	chassis.drive(-0.5, 0.0);
        }
        
        if(rightStick.getRawButton(2)){//Reverse
        	chassis.drive(0.375, 0.0);
        }
        
        if(rightStick.getRawButton(3)){//Left turn
        	chassis.drive(0.375, 1);
        	chassis.drive(-0.375, -1);
        }
        
        if(rightStick.getRawButton(4)){//Right turn
        	chassis.drive(-0.375, 1);
        	chassis.drive(0.375, -1);
        }
        
        //Camera
        //chassis.drive(rightStick.getPOV(), 0.0);        
        
        //chassis.drive(rightStick.getRawAxis(2), 1);
        //chassis.drive(rightStick.getRawAxis(2), -1);
        
        //controlArmHeight.drive(rightStick.getRawAxis(2), 0.0);
        //controlArmTilt.drive(rightStick.getRawAxis(2), 0.0);
               
        /*Tank Drive:
         * chassis.setSafetyEnabled(true);
        	while (isOperatorControl() && isEnabled()) {
        		chassis.tankDrive(stick, stick2);
            	Timer.delay(0.005);		// wait for a motor update time
        	}*/
         }         
         NIVision.IMAQdxStopAcquisition(session);        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}