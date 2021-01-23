/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.utilities.GenericEncoder;

public class Turret extends BasicController {
  private static final GenericEncoder turretAngle = RobotMap.turretAngle;
  public static final int MAXROTATION = 20;
  public static final double ANGLEMULTIPLIER = 0.075;  
  private static int correction_offset = 0;

  /**
   * Creates a new ExampleSubsystem.
   */
  public Turret() {
    super(RobotMap.turretTurn);
  }

  public void resetTEncoder () {
    correction_offset = -turretAngle.getPosition();    
    System.out.println("new correction_offset is " + correction_offset);
    System.out.println("current angle is " + this.getAngle());
  }
  
  public double getAngle() {
    //TODO convert pulses into degrees
    return ANGLEMULTIPLIER * (turretAngle.getPosition() + correction_offset);
  }

  @Override
  public void moveMotor(double speed){
    //if the angle is greater than 43 and moving right, the turret can't spin
    if(getAngle() > MAXROTATION && speed < 0){
      speed = 0; //don't move
    }
    else if(getAngle() < -MAXROTATION && speed > 0){
      speed = 0;
    }
    super.moveMotor(speed);

  }  
}