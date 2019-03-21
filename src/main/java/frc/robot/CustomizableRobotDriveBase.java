/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/// Java imports
import java.lang.reflect.*;
import java.util.*;
// WPILib imports
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;
// My imports
import frc.robot.*;
import sun.tools.asm.CatchData;
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
	* @param drivetrainType the type of drivetrain used (supports Differential, Meccanum, and Killough).
	* @param driveMode the style of driving (e.g. For Differential drivetrains, the drive modes are Tank, Arcade, and Curvature).
	* @param driveMotors the drive motors used, in the form of a hashmap with keys as string labels and values as speed controllers.
	**/
	public CustomizableRobotDriveBase (
		Class<? extends RobotDriveBase> drivetrainType, 
		String driveMode, 
		HashMap <String, SpeedController> driveMotors) 
		throws NoSuchMethodException, 
		InstantiationException{
        // Determine drivetrain class.
        switch(drivetrainType.toString()) {
        case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
            // Determine drive method.
			switch(driveMode.toLowerCase()) {
				case "arcade":
					//The drive methods won't change (though they might get deprecated) so we can ignore NoSuchMethodException.
                    driveMethod = drivetrainType.getMethod("arcadeDrive", double.class, double.class, boolean.class);
					break;
				case "tank":
					//The drive methods won't change (though they might get deprecated) so we can ignore NoSuchMethodException.
					driveMethod = drivetrainType.getMethod("tankDrive", double.class, double.class, boolean.class);
					break;
				case "curvature":
					//The drive methods won't change (though they might get deprecated) so we can ignore NoSuchMethodException.
					driveMethod = drivetrainType.getMethod("curvatureDrive", double.class, double.class, boolean.class);
					break;
				default:
					System.out.println("Error: This program does not recognize \"" + driveMode + "\" as a drive mode for a differential drivetrain.");
					break;
            }
            // Set drivetrain to be a new instance of DifferentialDrive.
			Constructor<? extends RobotDriveBase> co = drivetrainType.getConstructor(SpeedController.class, SpeedController.class);
			//Although the RobotDriveBase class is abstract, we are setting drivetrain to a non-abstract subclass, so we can ignore InstantiationException.
			try{
				drivetrain = co.newInstance(driveMotors.get("Left Drive Motors"), driveMotors.get("Right Drive Motors"));
			}catch(IllegalAccessException | InvocationTargetException e){
				System.out.println(e);
				System.exit(1);
			}
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