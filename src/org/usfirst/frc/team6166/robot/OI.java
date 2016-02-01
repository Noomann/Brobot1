
package org.usfirst.frc.team6166.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
//import org.usfirst.frc.team6166.robot.commands.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI {
    Joystick rightStick = new Joystick(0);
    Button button1 = new JoystickButton(rightStick, 1),
    		button2 = new JoystickButton(rightStick, 2),
    		button3 = new JoystickButton(rightStick, 3),
    	    button4 = new JoystickButton(rightStick, 4),
    	    button5 = new JoystickButton(rightStick, 5),
    	    button6 = new JoystickButton(rightStick, 6),
    	    button7 = new JoystickButton(rightStick, 7),
    	    button8 = new JoystickButton(rightStick, 8);

    public OI() {
    	//button1.toggleWhenPressed(Invert);
    }
    
    public Joystick getJoystick() {
        return rightStick;
    }
}

