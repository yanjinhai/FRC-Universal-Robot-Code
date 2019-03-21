/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Add your docs here.
 */
public class CustomizableOI {

    private Joystick[] joysticks;
    private XboxController[] xboxControllers;

    public CustomizableOI(Joystick[] joysticks, XboxController[] xboxControllers){
        this.joysticks = joysticks;
        this.xboxControllers = xboxControllers;
    }

    /**
     * Returns the available joysticks as a Joystick array. 
     * @return joysticks the total joysticks available for use.
     */
    public Joystick[] getJoysticks(){
        return joysticks;
    }

    /**
     * Returns the available xboxControllers as a XboxController array. 
     * @return xboxControllers the total xboxControllers available for use.
     */
    public XboxController[] getXboxControllers(){
        return xboxControllers;
    }


}
