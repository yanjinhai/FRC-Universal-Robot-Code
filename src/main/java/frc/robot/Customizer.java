/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.*;

import edu.wpi.first.wpilibj.*;

/**
 * Currently features:
 * 
 * -Custom drive motor ports
 */
public class Customizer {

    static {
        System.out.println("****Customizing...****");
        customizeDriveMotorPorts();
    }

    private static void customizeDriveMotorPorts(){
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("How many speedcontrollers do you use for your drive train?");
        String userInput = consoleScanner.nextLine();
        int numberOfDriveMotors;
        while(true){
            try {
                numberOfDriveMotors = Integer.parseInt(userInput);
                break;
            } catch (Exception e) {
                System.out.println("Error: Please enter an integer expression. Try again.");
            }
        }
        SpeedController[] driveMotors = new SpeedController[numberOfDriveMotors];
        for(int i = 0; i < numberOfDriveMotors; i++){
            System.out.println("What is the PWM Channel/CAN ID.");
            // switch(){

            // }
            // driveMotors[i] = new;
        }
        consoleScanner.close();
    }
}
