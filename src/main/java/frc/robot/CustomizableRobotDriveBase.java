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
//import frc.robot.*;

/**
* Description here.
*
* Does not currently care to support:
* - Funtionality outside of driving
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
		Class<? extends RobotDriveBase> drivetrainType, HashMap <String, SpeedController> driveMotors) {
        // Determine drivetrain class.
        switch(drivetrainType.toString()) {
        case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
            /* Set drivetrain to be a new instance of DifferentialDrive.
			Although the RobotDriveBase class is abstract, we are setting drivetrain to a non-abstract subclass, 
			so we can ignore InstantiationException.*/
			try{
				Constructor<? extends RobotDriveBase> co = drivetrainType.getConstructor(SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Left Drive Motors"), driveMotors.get("Right Drive Motors"));
			}catch(IllegalAccessException | InvocationTargetException e){
				System.out.println(e);
				System.exit(1);
			}catch(NoSuchMethodException | InstantiationException e){
				// Shoudn't ever happen. Ignore.
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
	 * The style of driving. Used in conjunction with CustomizableRobotDriveBase.drive().
	 */
	public static enum DriveMode {
		arcade, tank, curvature;
	}

	/**
	 * Drives the robot using a custom-selected style.
	 *
	 * @param driveMode the style of driving (e.g. For Differential drivetrains, the drive modes are Tank, Arcade, and Curvature).
	 * */
	public void drive(DriveMode driveMode, XboxController driveController) {// come up with something better than a string for drive mode, like a dedicated class with method constants or something.
		
		// Determine drive method. The drive method code won't change (though it might get deprecated) so we can ignore NoSuchMethodException.
		Method driveMethod = null;
		try{
			Class<? extends RobotDriveBase> drivetrainType = drivetrain.getClass();
			switch(drivetrainType.toString()) {
				case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
					switch(driveMode.toString()) {
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
					break;
				case "class edu.wpi.first.wpilibj.drive.MecanumDrive":
					// Implement later
					break;
				case "class edu.wpi.first.wpilibj.drive.KilloughDrive":
					// Implement later
					break;
			}
		}catch(NoSuchMethodException e){
			// Shouldn't ever happen. ignore.
		}
		// Invokes the drive method. Parameters differ from method to method, so I used this case-by-case system.
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
					driveMethod.invoke(drivetrain, driveController.getY(Hand.kLeft), driveController.getY(Hand.kRight));
				}catch(IllegalAccessException | InvocationTargetException e){
					System.out.println(e);
					System.exit(1);
				}
				break;
			// Drive base: Differential 
			// Drive style: Curvature ("Cheesy")
			case "public void edu.wpi.first.wpilibj.drive.DifferentialDrive.curvatureDrive(double,double,boolean)":
				try{
					driveMethod.invoke(drivetrain, driveController.getY(Hand.kLeft), driveController.getX(Hand.kRight));
				}catch(IllegalAccessException | InvocationTargetException e){
					System.out.println(e);
					System.exit(1);
				}
				break;
		}
	}
}