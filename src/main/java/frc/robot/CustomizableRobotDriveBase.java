/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// Java imports
import java.lang.reflect.*;
import java.util.*;

// WPILib imports
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
* - Joysticks
*
* @author Jinhai Yan
* @version March 2019
**/
public class CustomizableRobotDriveBase {

	// Global Variables
	private static RobotDriveBase drivetrain;
	// move to main class later.
	//private static CustomizableOI c_OI = new CustomizableOI();
	
	/**
	* Constructs a CustomizableRobot object.
	* 
	* @param drivetrainType the type of drivetrain used (supports Differential, Meccanum, and Killough).
	* @param driveMotors the drive motors used, in the form of a hashmap with keys as string labels and values as speed controllers.
	**/
	public CustomizableRobotDriveBase (
		Class<? extends RobotDriveBase> drivetrainType, HashMap <String, SpeedController> driveMotors) 
		throws NoSuchMethodException, InstantiationException {
        // Determine drivetrain class.
        switch(drivetrainType.toString()) {
        case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
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
	
	/**
	 * Drives the robot using a custom-selected style.
	 *
	 * @param driveMode the style of driving (e.g. For Differential drivetrains, the drive modes are Tank, Arcade, and Curvature).
	 * */
	public void drive(String driveMode, XboxController driveController){// come up with something better than a string, like a dedicated class with method constants or something.
		// Determine drive method. The drive method code won't change (though it might get deprecated) so we can ignore NoSuchMethodException.
		Method driveMethod;
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
				System.out.println("Error: This program does not recognize \"" + driveMode + "\" as a drive mode for a differential drivetrain.");
				System.exit(1);
				break;
		}

		// Invokes the drive method. Parameters differ from method to method, so this case-by-base system is neccessary.
		switch(driveMethod.toString()){
			// Drive base: Differential 
			// Drive style: Arcade
			case "public void edu.wpi.first.wpilibj.drive.DifferentialDrive.arcadeDrive(double,double,boolean)":
				try{
					driveMethod.invoke(drivetrain, driveController.getY(Hand.kLeft), driveController.getX(Hand.kRight));
				}catch(IllegalAccessException | InvocationTargetException e){
					System.out.println(e);
					System.exit(1);
				}
				break;
			// Drive base: Differential 
			// Drive style: Tank
			case "public void edu.wpi.first.wpilibj.drive.DifferentialDrive.tankDrive(double,double,boolean)":
				try{
					driveMethod.invoke(drivetrain, );
				}catch(IllegalAccessException | InvocationTargetException e){
					System.out.println(e);
					System.exit(1);
				}
				break;
			// Drive base: Differential 
			// Drive style: Curvature
			case "public void edu.wpi.first.wpilibj.drive.DifferentialDrive.curvatureDrive(double,double,boolean)":
				try{
					driveMethod.invoke(drivetrain, );
				}catch(IllegalAccessException | InvocationTargetException e){
					System.out.println(e);
					System.exit(1);
				}
				break;
			default:
				// Should never happen.
				System.out.println("Error: This program is broken. Please contact the developers by whichever means possible to resolve this issue.");
				break;
		}
		//driveMethod.invoke(drivetrain, );

	}
}