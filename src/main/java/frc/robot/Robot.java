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
import edu.wpi.first.wpilibj.drive.*;
import frc.robot.CustomizableRobotDriveBase.DriveMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  XboxController mainController = new XboxController(0);
  CustomizableRobotDriveBase drivetrain;
  
  @Override
  public void robotInit() {
    HashMap<String, SpeedController> h = new HashMap<String, SpeedController>();
    h.put("Right Drive Motors", new Victor(0));
    h.put("Left Drive Motors", new Victor(1));
    drivetrain = new CustomizableRobotDriveBase(DifferentialDrive.class, h);
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
    drivetrain.drive(DriveMode.arcade, mainController);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
