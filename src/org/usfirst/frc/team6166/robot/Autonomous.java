package org.usfirst.frc.team6166.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	public static void autonomousStraight() {

		double angle = Robot.gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " +Robot.n+ " Seconds", Robot.X);
		if(Robot.X < 50 *Robot.n* 1.5) {//based on this, 50n = ~n second    		
			Robot.chassis.drive(-0.4, angle*Robot.Kp);
			Robot.X++;
		}
	}
	
	public static void autonomousMoat() {
		double angle = Robot.gyro.getAngle();
		
		if(Robot.X < 50 *Robot.n* 2) {
			Robot.chassis.drive(-.5, angle*Robot.Kp);
			Robot.X++;
		}
	}
	
	public static void PortCullis() {
		double angle = Robot.gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " +Robot.n+ " Seconds", Robot.X);		
		if(Robot.X < 50 *Robot.n* .5) {//based on this, 50n = ~n second
			Robot.arm.set(.3);
			//chassis.drive(-0.25, angle*Kp);
			Robot.X++;
		} else if (Robot.Y < 50 *Robot.n* 3){
			Robot.chassis.drive(-.35, angle*Robot.Kp);
			Robot.Y++;
		} else if (Robot.Z < 50 *Robot.n* .5){
			Robot.arm.set(-.3);
			Robot.Z++;
		}
	}

	public static void LowBar() {

		double angle = Robot.gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " + Robot.n+ " Seconds", Robot.X);
		if(Robot.X < 50 *Robot.n* 2) {//based on this, 50n = ~n second    		
			Robot.chassis.drive(-0.4, angle*Robot.Kp);
			Robot.X++;
		}/* else if (Y < 50 *Robot.n* 2) {
			chassis.drive(0.4, angle*Kp);
			Y++;
		} */
	}

	public static void RoughTerrain() {

		double angle = Robot.gyro.getAngle();
		

		SmartDashboard.putNumber("Time - " +Robot.n+ " Seconds", Robot.X);
		if(Robot.X < 50 *Robot.n* 2.5) {//based on this, 50n = ~n second    		
			Robot.chassis.drive(-0.4, angle*Robot.Kp);
			Robot.X++;
		}
	}
	
	public static void Ramparts() {

		double angle = Robot.gyro.getAngle();

		if(Robot.X < 50 *Robot.n* 5) {//based on this, 50n = ~n second    		
			Robot.chassis.drive(-0.25, angle*Robot.Kp);
			//armTilt.set(-0.5);
			Robot.X++;
    	}
    }
	
	public static void ShovelTheFries() {
		double angle = Robot.gyro.getAngle();
		
		if (Robot.X < 50 *Robot.n* 1.5) {
			Robot.chassis.drive(-.25, angle * Robot.Kp);
			Robot.X++;
		} else if(Robot.Y < 50 *Robot.n* .2) {
			Robot.chassis.drive(0.0, angle * Robot.Kp);
			Robot.arm.set(0.25);
			Robot.Y++;
		} else if (Robot.V < 50 *Robot.n* .5) {
			Robot.chassis.drive(0.0, angle * Robot.Kp);	
			Robot.V++;
		} else if (Robot.Z < 50 *Robot.n* .2) {
			Robot.chassis.drive(-0.25, angle * Robot.Kp);
			Robot.Z++;
		} else if (Robot.Q < 50 *Robot.n* .2) {
			Robot.chassis.drive(-0.35, angle * Robot.Kp);
			Robot.Q++;
		} else if (Robot.W < 50 *Robot.n* 1) {
			Robot.chassis.drive(-0.35, angle * Robot.Kp);
			Robot.W++;
		} else if (Robot.I < 50 *Robot.n* .1) {
			Robot.arm.set(-0.25);
			Robot.I++;
		}
		/*if(rightStick.getRawButton(3) && armUp != true) {
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
		}*/		 
		
	}
    
    public static void Turn90Left() {
    	double angle = Robot.gyro.getAngle();
    	
    	SmartDashboard.putNumber("Time - " +Robot.n+ " Seconds", Robot.X);
    	if( angle == 270) {
    		
    	}
    	if(angle > 270 && angle <= 360 ) {//based on this, 50n = ~n second    		
    		Robot.chassis.tankDrive(0.15, -0.15);
			//armTilt.set(-0.5);
			Robot.X++;
    	}
    	else if(angle >= 0 && angle <=90){
    		
    		Robot.chassis.tankDrive(0.15, -0.15);
    	}
    	else if(angle > 90 && angle < 270) {
    		
    		Robot.chassis.tankDrive(-0.15, 0.15);
    	}
    	else {
    		
    		Robot.chassis.drive(0.0, 0.0);
    	}
    }
    
    public static void Turn90Right() {
    	
    	
    	SmartDashboard.putNumber("Time - " + Robot.n + " Seconds", Robot.X);
    	double angle = Robot.gyro.getAngle();
    	Robot.gyro.reset();
    	if(angle == 90) {
    		
    	}
    	if(angle > 270 && angle <= 360 ) {//based on this, 50n = ~n second    		
    		//chassis.tankDrive(-0.15, 0.15);
    		Robot.chassis.drive(-0.375, 1);
    		Robot.chassis.drive(0.375, -1);
    	}    	
    	SmartDashboard.putDouble("Angle", angle);
    	SmartDashboard.putNumber("Time - " + Robot.n + " Seconds", Robot.X);
    	if(Robot.X < 50 * Robot.n * 5) {//based on this, 50n = ~n second    		
			Robot.chassis.drive(-0.25, 1);
			//armTilt.set(-0.5);
			Robot.X++;
    	}
    	if(Robot.X >= 50 *Robot.n* 5 &&Robot.X<= 50 *Robot.n * 10)
    	{
    		Robot.chassis.drive(-0.25, -1);
    		Robot.X++;
    	}
    	/**if(angle >= 85 && angle <= 95) {
    		turnComplete = true;
    	}
    	else if(angle > 270 && angle <= 360 && turnComplete == false ) {//based on this, 50n = ~n second    		
    		//chassis.tankDrive(-0.15, 0.15);
    		//chassis.drive(-.375, 1);
    		chassis.drive(-.275, -1);
			//armTilt.set(-0.5);
			X++;
    	}
    	else if(angle >= 0 && angle <85 && turnComplete == false){
    		
    		//chassis.tankDrive(-0.15, 0.15);
    		chassis.drive(-0.375, 1);
    		chassis.drive(0.375, -1);
    		chassis.drive(-.275, -1);
    		//chassis.drive(.375, -1);
    	}
    	else if(angle > 95 && angle < 270 && turnComplete == false) {
    		
    		//chassis.tankDrive(0.15, -0.15);
    		chassis.drive(-0.375, -1);
    		chassis.drive(0.375, 1);
    	} else {
    		chassis.drive(.275, 1);
    		//chassis.drive(-.375, -1);
    	}
    	else {
    		
    		chassis.drive(0.0, 0.0);
    	}*/
    }
    
    public static void autonomousTurn180() {
    	
    	double angle = Robot.gyro.getAngle();
    	
    	SmartDashboard.putNumber("Time - " + Robot.n + " Seconds", Robot.X);
    	if( angle == 180) {
    		
    	}
    	if(angle > 180 && angle <= 360 ) {//based on this, 50n = ~n second    		
    		Robot.chassis.tankDrive(0.15, -0.15);
			//armTilt.set(-0.5);
			Robot.X++;
    	}
    	else if(angle >= 0 && angle < 180){
    		
    		Robot.chassis.tankDrive(-0.15, 0.15);
    	}
    	else {
    		
    		Robot.chassis.drive(0.0, 0.0);
    	}
	}
	
}
