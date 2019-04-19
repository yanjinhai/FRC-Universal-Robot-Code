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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	private RobotDriveBase drivetrain;
	private Object[] driveParams;
	public boolean SmartDashboardValuesEnabled = true;
	
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
		ARCADE, TANK, CURVATURE, CARTESIAN, POLAR;
	}

	/**
	* Constructs a CustomRobotDriveBase object.
	* 
	* @param drivetrainType the type of drivetrain used (supports Differential, Meccanum, and Killough).
	* @param driveMotors the drive motors used, in the form of a hashmap with keys as string labels and values as speed controllers.
	*/
	public CustomRobotDriveBase (
		DriveBase drivetrainType, HashMap <String, SpeedController> driveMotors) {
		// Initialize variables.
		String[] MotorsAndPorts = new String[driveMotors.size()];
		String raw;
		// Determine drivetrain class.
		try{
			Constructor<? extends RobotDriveBase> co;
			switch(drivetrainType) {
			case DIFFERENTIAL:
				// Set drivetrain to be a new instance of DifferentialDrive. 
				co = DifferentialDrive.class.getConstructor(SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Left"), driveMotors.get("Right"));
				// Get string value representations of each drive motor.
				raw = driveMotors.get("Left").toString();
				MotorsAndPorts[0] = raw.substring(0, raw.indexOf('@'));
				raw = driveMotors.get("Right").toString();
				MotorsAndPorts[1] = raw.substring(0, raw.indexOf('@'));
				break;
			case MECANUM:
				// Set drivetrain to be a new instance of MecanumDrive. 
				co = MecanumDrive.class.getConstructor(SpeedController.class, SpeedController.class, SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Front Left"), driveMotors.get("Rear Left"), driveMotors.get("Front Right"), driveMotors.get("Rear Right"));
				// Get string value representations of each drive motor.
				raw = driveMotors.get("Front Left").toString();
				MotorsAndPorts[0] = raw.substring(0, raw.indexOf('@'));
				raw = driveMotors.get("Rear Left").toString();
				MotorsAndPorts[1] = raw.substring(0, raw.indexOf('@'));
				raw = driveMotors.get("Front Right").toString();
				MotorsAndPorts[2] = raw.substring(0, raw.indexOf('@'));
				raw = driveMotors.get("Rear Right").toString();
				MotorsAndPorts[3] = raw.substring(0, raw.indexOf('@'));
				break;
			case KILLOUGH:
				// Set drivetrain to be a new instance of KilloughDrive.
				co = KilloughDrive.class.getConstructor(SpeedController.class, SpeedController.class, SpeedController.class);
				drivetrain = co.newInstance(driveMotors.get("Left"), driveMotors.get("Right"), driveMotors.get("Back"));
				// Get string value representations of each drive motor.				
				raw = driveMotors.get("Left").toString();
				MotorsAndPorts[0] = raw.substring(0, raw.indexOf('@'));
				raw = driveMotors.get("Right").toString();
				MotorsAndPorts[1] = raw.substring(0, raw.indexOf('@'));
				raw = driveMotors.get("Back").toString();
				MotorsAndPorts[2] = raw.substring(0, raw.indexOf('@'));
				break;
			default:
				// Only executes if input drivetrain type is not a recognized subclass of RobotDriveBase.
				printError("Error: Unrecognized drivetrain class. " + this.getClass() + " recognizes only DifferentialDrive, MeccanumDrive, and KilloughDrive"); 
			}
		} catch(IllegalAccessException | InvocationTargetException e){
			printException(e);
		}catch(NoSuchMethodException | InstantiationException e){
			// This occurs only when we cannot create a new instance of KilloughDrive, meaning that WPI has changed their library. 
			printError("Error: " + this.getClass() + " is out of date. Try using the standard WPI drive classes instead.");
		}
		// Send information to the dashboard.
		if(SmartDashboardValuesEnabled){
			SmartDashboard.putString("Drive Base Type", drivetrainType.name());
			SmartDashboard.putStringArray("Drive Motors", MotorsAndPorts);
		}
	}

	/**
	 * Drives the robot using a custom-selected style.
	 *
	 * @param driveMode the style of driving (e.g. For Differential drivetrains, the drive modes are Tank, Arcade, and Curvature).
	 * @param driveController the xbox controller used to drive the robot.
	 */
	public void drive(DriveMode driveMode, GenericHID driveController) {
		Method driveMethod = null;
		try{
			Class<? extends RobotDriveBase> drivetrainType = this.drivetrain.getClass();
			switch(drivetrainType.toString()) {
				case "class edu.wpi.first.wpilibj.drive.DifferentialDrive":
					switch(driveMode) {
						case ARCADE:
							driveMethod = drivetrainType.getMethod("arcadeDrive", double.class, double.class);
							// Set parameters
							driveParams = new Object[driveMethod.getParameterCount()];
							driveParams[0] = driveController.getX(Hand.kRight);
							driveParams[1] = driveController.getY(Hand.kLeft);
							break;
						case TANK:
							driveMethod = drivetrainType.getMethod("tankDrive", double.class, double.class);
							// Set parameters
							driveParams = new Object[driveMethod.getParameterCount()];
							driveParams[0] = driveController.getY(Hand.kRight);
							driveParams[1] = driveController.getY(Hand.kLeft);
							break;
						case CURVATURE:
							driveMethod = drivetrainType.getMethod("curvatureDrive", double.class, double.class, boolean.class);
							// Set parameters
							driveParams = new Object[driveMethod.getParameterCount()];
							driveParams[0] = driveController.getX(Hand.kRight);
							driveParams[1] = driveController.getY(Hand.kLeft);
							driveParams[2] = true;
							break;
						default:
							printError("Error: This program does not recognize \"" + driveMode + 
							"\" as a drive mode for a differential drivetrain.");
							break;
					}
					break;
				case "class edu.wpi.first.wpilibj.drive.MecanumDrive":
					switch(driveMode){
						case CARTESIAN:
							driveMethod = drivetrainType.getMethod("driveCartesian", double.class, double.class, double.class);
							// Set parameters.
							driveParams = new Object[driveMethod.getParameterCount()];
							driveParams[0] = driveController.getX(Hand.kLeft);
							driveParams[1] = driveController.getY(Hand.kLeft);
							driveParams[2] = driveController.getX(Hand.kRight);
							break;
						case POLAR:
							driveMethod = drivetrainType.getMethod("drivePolar", double.class, double.class, double.class);
							// Set parameters.
							driveParams = new Object[driveMethod.getParameterCount()];
							driveParams[0] = driveController.getY(Hand.kLeft);
							driveParams[1] = driveController.getX(Hand.kLeft);
							driveParams[2] = driveController.getX(Hand.kRight);
							break;
						default:
							printError("Error: This program does not recognize \"" + driveMode + 
							"\" as a drive mode for a mecanum drivetrain.");
							break;
					}
					break; 
				case "class edu.wpi.first.wpilibj.drive.KilloughDrive":
					// Implement later
					switch(driveMode){
						case CARTESIAN:
							driveMethod = drivetrainType.getMethod("driveCartesian", double.class, double.class, double.class);
							break;
						case POLAR:
							driveMethod = drivetrainType.getMethod("drivePolar", double.class, double.class, double.class);
							break;
						default:
							printError("Error: This program does not recognize \"" + driveMode + 
							"\" as a drive mode for a mecanum drivetrain.");
							break;
					}
					// Set parameters. These parameters are the same for both drive modes, so they are outside the switch statement.
					driveParams = new Object[driveMethod.getParameterCount()];
					driveParams[0] = driveController.getY(Hand.kLeft);
					driveParams[1] = driveController.getX(Hand.kLeft);
					driveParams[2] = driveController.getX(Hand.kRight);
					break;
			}	
		}catch(NoSuchMethodException e){
			// This occurs only when the methods of the drive class changes somehow, meaning that WPI has changed their library. 
			printError("Error: " + getClass() + " is out of date. Try using the standard WPI drive classes instead.");
		}
		// Invoke the drive method.
		try{
			driveMethod.invoke(drivetrain, driveParams);
		}catch(IllegalAccessException | InvocationTargetException e){
			printException(e);
		}
		// Send information to the dashboard.
		if(SmartDashboardValuesEnabled){
			SmartDashboard.putString("Drive Mode", driveMode.name());
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

	/**
	 * Wrapper for printing the input error as a string. Terminates the system directly afterwards
	 * 
	 * @param e the error to print
	 */
	private static void printError(String e){
		System.out.println(e);
		System.exit(1);
	}

	/**
	 * Wrapper method for printing the stacktrace of input exception. Terminates the system directly afterwards.
	 * 
	 * @param e the exception to print
	 */
	private static void printException(Exception e){
		System.out.println();
		e.printStackTrace();
		System.exit(1);
	}
}