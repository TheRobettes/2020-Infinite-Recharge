/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.utilities.GenericEncoder;

public class Shooter extends BasicController {
  private static final GenericEncoder pewPewSpeed = RobotMap.pewPewSpeed;
  

  /**
   * Creates a new ExampleSubsystem.
   */
  public Shooter() {
    super(RobotMap.pewPewPew);
  }

  public double getRate() {
    return pewPewSpeed.getRate();
  }
}
