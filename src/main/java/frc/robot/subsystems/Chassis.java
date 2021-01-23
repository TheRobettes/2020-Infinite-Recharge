/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;


public class Chassis extends SubsystemBase {
  private static final MecanumDrive driveChassis = new MecanumDrive(
    RobotMap.leftFrontWheel, 
    RobotMap.leftBackWheel,
    RobotMap.rightFrontWheel, 
    RobotMap.rightBackWheel);

  static double SPEEDMULTIPLIER = 0.5; //August go here
  //Assigning wheels to mecanumDrive variable (using power)
  //RobotMap: we want a PID loop using *speed*



  /**
   * Creates a new ExampleSubsystem.
   */
  public Chassis() {
    System.out.println("chassis initialized");
    driveChassis.setRightSideInverted(false);
  }


  
  public void robotDrive(double strafe, double driveSpeed, double spin) {
    // we didn't like their variable names ySpeed (strafe or our X) and xSpeed (drive or our Y)
    double strafeMultiplier = (strafe < 0)
      ? 0.5 //left
      : 0.1; //right
    //strafe += strafeMultiplier; //originally was spin... correct to strafe
    //SmartDashboard.putNumber("strafeMultiplier", strafeMultiplier);
    
    double curveMultiplier =  (driveSpeed > 0)
      ? 0.1
      : -0.1;
    //spin += curveMultiplier*driveSpeed;
    driveChassis.driveCartesian(strafe, SPEEDMULTIPLIER*driveSpeed, spin); 
    //SmartDashboard.putNumber("spin", spin);
    
  
  }
  @Override 
  public String toString(){
    return getName();
  } 

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
