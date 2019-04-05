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
* A class that can be used as any WPI drive class, all in one.
* <p>
* Does not currently support:
* <p>
* - Funtionality outside of driving 
* - Swerve drive
* - Deadband (should I support deadband at all or is it pointless?)
* - Joysticks
* - Optional Values (squared inputs, quick turn, field-oriented)
*
* @author Jinhai Yan
* @version April 2019
**/
public class CustomRobotDriveBase {

	// Global Variables
	private static RobotDriveBase drivetrain;
	private static Object[] driveParams;
	
	/**
	 * The class of the robot's drivetrain. Used in when constructing a new instance of CustomRobotDriveBase;
	 */
	public static enum DriveBase {
		DIFFERENTIAL, MECANUM, KILLOUGH;
	}

	/**
	 * The style of driving. Used in conjunction with CustomRobotDriveBase.drive().
	 */
	public static enum DriveMode {
		ARCADE, TANK, CURVATURE;
	}

	/**
	* Constructs a CustomRobotDriveBase object.
	* 
	* @param drivetrainType the type of drivetrain used (supports Differential, Meccanum, and Killough).
	* @param driveMotors the drive motors used, in the form of a hashmap with keys as string labels and values as speed controllers.
	**/
	public CustomRobotDriveBase (
		DriveBase drivetrainType, HashMap <String, SpeedController> driveMotors) {
        // Determine drivetrain class.
        switch(drivetrainType) {
        case DIFFERENTIAL:
            /* Set drivetrain to be a new instance of DifferentialDrive. 
			Although the RobotDriveBase class is abstract, we are setting drivetrain to a non-abstract subclass, 
			so we can ignore InstantiationException.*/
			try{
				Constructor<DifferentialDrive> co = DifferentialDrive.class.getConstructor(SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Left"), driveMotors.get("Right"));
			}catch(IllegalAccessException | InvocationTargetException e){
				System.out.println(e);
				System.exit(1);
			}catch(NoSuchMethodException | InstantiationException e){
				// This occurs only when we cannot create a new instance of DifferentialDrive, meaning that WPI has changed their library. 
				System.out.println("Error: " + this.getClass() + " is out of date. Try using the standard WPI drive classes instead.");
				System.exit(1);
			}
			break;
		case MECANUM:
			/* Set drivetrain to be a new instance of MecanumDrive.
			Although the RobotDriveBase class is abstract, we are setting drivetrain to a non-abstract subclass, 
			so we can ignore InstantiationException.*/
			try{
				Constructor<MecanumDrive> co = MecanumDrive.class.getConstructor(SpeedController.class, SpeedController.class, SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Front Left"), driveMotors.get("Rear Left"), driveMotors.get("Front Right"), driveMotors.get("Rear Right"));
			}catch(IllegalAccessException | InvocationTargetException e){
				System.out.println(e);
				System.exit(1);
			}catch(NoSuchMethodException | InstantiationException e){
				// This occurs only when we cannot create a new instance of MecanumDrive, meaning that WPI has changed their library. 
				System.out.println("Error: " + this.getClass() + " is out of date. Try using the standard WPI drive classes instead.");
				System.exit(1);
			}
			break;
		case KILLOUGH:
			/* Set drivetrain to be a new instance of KilloughDrive.
			Although the RobotDriveBase class is abstract, we are setting drivetrain to a non-abstract subclass, 
			so we can ignore InstantiationException.*/
			try{
				Constructor<KilloughDrive> co = KilloughDrive.class.getConstructor(SpeedController.class, SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Left"), driveMotors.get("Right"), driveMotors.get("Back"));
			}catch(IllegalAccessException | InvocationTargetException e){
				System.out.println(e);
				System.exit(1);
			}catch(NoSuchMethodException | InstantiationException e){
				// This occurs only when we cannot create a new instance of KilloughDrive, meaning that WPI has changed their library. 
				System.out.println("Error: " + this.getClass() + " is out of date. Try using the standard WPI drive classes instead.");
				System.exit(1);
			}
			break;
        default:
            // Only executes if input drivetrain type is not a recognized subclass of RobotDriveBase.
            System.out.println("Error: Unrecognized drivetrain class. " + this.getClass() + " recognizes only DifferentialDrive, MeccanumDrive, and KilloughDrive"); 
            System.exit(1);
        }
	}

	/**
	 * Drives the robot using a custom-selected style.
	 *
	 * @param driveMode the style of driving (e.g. For Differential drivetrains, the drive modes are Tank, Arcade, and Curvature).
	 * @param driveController the xbox controller used to drive the robot.
	 * */
	public void drive(DriveMode driveMode, XboxController driveController) {
		Method driveMethod = null;
		try{
			Class<? extends RobotDriveBase> drivetrainType = drivetrain.getClass();
			switch(drivetrainType.toString()) {
				case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
					switch(driveMode) {
						case ARCADE:
							driveMethod = drivetrainType.getMethod("arcadeDrive", double.class, double.class);
							// Set parameters
							driveParams = new Object[2];
							driveParams[0] = driveController.getX(Hand.kRight);
							driveParams[1] = driveController.getY(Hand.kLeft);
							break;
						case TANK:
							driveMethod = drivetrainType.getMethod("tankDrive", double.class, double.class);
							// Set parameters
							driveParams = new Object[2];
							driveParams[0] = driveController.getY(Hand.kRight);
							driveParams[1] = driveController.getY(Hand.kLeft);
							break;
						case CURVATURE:
							driveMethod = drivetrainType.getMethod("curvatureDrive", double.class, double.class, boolean.class);
							// Set parameters
							driveParams = new Object[3];
							driveParams[0] = driveController.getX(Hand.kRight);
							driveParams[1] = driveController.getY(Hand.kLeft);
							driveParams[2] = true;
							break;
						default:
							System.out.println("Error: This program does not recognize \"" + driveMode + 
							"\" as a drive mode for a differential drivetrain.");
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
			// This occurs only when the methods of the drive class changes somehow, meaning that WPI has changed their library. 
			System.out.println("Error: " + this.getClass() + " is out of date. Try using the standard WPI drive classes instead.");
			System.exit(1);
		}
		// Invoke the drive method.
		try{
			driveMethod.invoke(drivetrain, driveParams);
			System.out.println();
		}catch(IllegalAccessException | InvocationTargetException e){
			System.out.println(e);
			System.exit(1);
		}		
	}

	/**
	 * Returns the drive base.
	 * 
	 * @return the drive base.
	 */
	public RobotDriveBase driveBase(){
		return drivetrain;
	}
}