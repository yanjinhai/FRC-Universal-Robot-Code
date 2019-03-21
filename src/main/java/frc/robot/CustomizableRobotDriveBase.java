/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/// Java imports
import java.lang.reflect.*;
// WPILib imports
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;
// My imports
import frc.robot.*;
/**
* Description here.
*
* Does not currently care to support:
* - Non-drive funtionality
* - Swerve drive
* - Deadband (should I ever support deadband or is it pointless?)
*
* @author Jinhai Yan
* @version March 2019
**/
public class CustomizableRobotDriveBase {
	// Global Variables
	private static RobotDriveBase drivetrain;
    private static Method driveMethod;
	private static Object[] driveParams;
	// move to main class later.
	private static CustomizableOI c_OI = new CustomizableOI(new Joystick[1], new XboxController[1]);
	/**
	* Constructs a CustomizableRobot object.
	* 
	* @param drivetrain the drive base of the robot.
	* @param
	**/
	public CustomizableRobotDriveBase (
		Class<? extends RobotDriveBase> drivetrainType, 
		String driveMode, 
		SpeedControllerGroup[] driveMotors/*A temporary measure*/) 
		throws NoSuchMethodException, 
		InstantiationException, 
		IllegalAccessException, 
		InvocationTargetException{
        // Determine drivetrain class.
        switch(drivetrainType.toString()) {
        case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
            // Determine drive method.
			switch(driveMode.toLowerCase()) {
				case "arcade":
                    driveMethod = drivetrainType.getMethod("arcadeDrive", double.class, double.class, boolean.class);
					break;
				case "tank":
					driveMethod = drivetrainType.getMethod("tankDrive", double.class, double.class, boolean.class);
					break;
				case "curvature":
					driveMethod = drivetrainType.getMethod("curvatureDrive", double.class, double.class, boolean.class);
					break;
				default:
					System.out.println("Error: Differential drivetrains do not have drive mode \"" + driveMode + "\".");
					break;
            }
            // Set drivetrain to be a new instance of DifferentialDrive.
            Constructor<? extends RobotDriveBase> co = drivetrainType.getConstructor(SpeedController.class, SpeedController.class);
            drivetrain = co.newInstance(driveMotors[0], driveMotors[1]);
			break;
		case "class edu.wpi.first.wpilibj.drive.MecanumDrive":
			// Implement later
			break;
		case "class edu.wpi.first.wpilibj.drive.KilloughDrive":
			// Implement later
			break;
        default:
            // Only executes if variable drivetrainType is not a recognized subclass of RobotDriveBase.
            System.out.println("Error: Unrecognized drivetrain class."); 
            System.exit(1);
        }
    }
	// Drive method
	public void drive(){
		// Example scenario. Replace with a logic-output map later (one with drive params depending on drive method, and with driveJoystick being in a manualy inputted port).
		Joystick driveJoystick = c_OI.getJoysticks()[0];
        driveParams[0] = driveJoystick.getX();
        driveParams[1] = driveJoystick.getY();
        driveParams[2] = driveJoystick.getZ();
		driveMethod.invoke(drivetrain, );
	}
}