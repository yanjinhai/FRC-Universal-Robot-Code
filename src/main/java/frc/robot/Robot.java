/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// Java imports
import java.util.*;

// WPILib imports
import edu.wpi.first.wpilibj.*;
import frc.robot.CustomRobotDriveBase.DriveBase;
import frc.robot.CustomRobotDriveBase.DriveMode;

// CTRE imports
//import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  XboxController mainController = new XboxController(0);
  CustomRobotDriveBase drivetrain;
  //DifferentialDrive a;

  @Override
  public void robotInit() {
    HashMap<String, SpeedController> driveMotors = new HashMap<String, SpeedController>();
    // driveMotors.put("Right", new Victor(0));
    // driveMotors.put("Left", new Victor(1));
    Victor motor0 = new Victor(3);
    Victor motor1 = new Victor(1);
    Victor motor2 = new Victor(0);
    Victor motor3 = new Victor(2);
    motor0.setInverted(true);
    driveMotors.put("Front Right", motor1);
    driveMotors.put("Front Left", motor2);
    driveMotors.put("Rear Right", motor0);
    driveMotors.put("Rear Left", motor3);
    drivetrain = new CustomRobotDriveBase(DriveBase.MECANUM, driveMotors);
    // System.out.println("#####" + drivetrain.driveBase());
    // System.out.println("#####" + drivetrain.driveBase().getClass());
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    drivetrain.drive(DriveMode.POLAR, mainController);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
