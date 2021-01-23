/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.subsystems.Shooter;


/**
 * An example command that uses an example subsystem.
 */
public class ManualShooting extends JoystickCommands {
  protected Shooter pewPewSubsystem = OI.pewPewPew;
  protected double speed;
  protected boolean isJoystickModeSpeed = true;
  private static boolean onTarget;
  private double previousPower;

  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualShooting(double speed) {
    this.speed = speed;
    isJoystickModeSpeed = (speed == 0); 

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(pewPewSubsystem);
  }

  public static boolean isPewPewPrepared(){
    return onTarget;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("starting a fixed speed shooting loop" );
    onTarget = false;
    previousPower = 0.9;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    double currentSpeed = pewPewSubsystem.getRate();
    double targetSpeed = speed;
    if(isJoystickModeSpeed){
      targetSpeed = -500*joystickAttack.getTwist() + 5000;
    }
    onTarget = Math.abs(targetSpeed - currentSpeed) < 100;
    SmartDashboard.putNumber("Target shooter speed  ", targetSpeed);

    SmartDashboard.putNumber("Shooter speed  ", currentSpeed);

   
    double speedError = targetSpeed-currentSpeed;
    double powerAdjustment = speedError * 0.001; //originally was * 0.0001
    double targetPower = previousPower + powerAdjustment;
    SmartDashboard.putNumber("power adjustment", powerAdjustment);
      //if (targetPower > 0.9) 
    //targetPower = 0.9; //TODO changed here!

    if (targetPower < 0.75){
      targetPower = 0.75;
    }
    /*System.out.println("Shooter speed " + currentSpeed + ", " + "Target speed " + targetSpeed
          + ", onTarget " + onTarget);*/
    this.pewPewSubsystem.go(targetPower);
    previousPower = targetPower;
    //SmartDashboard.putNumber("Shooter Power  ", targetPower);
    

    //this.basicSubsystem.go(joystickXbox.getX());
    //TODO adjust power for known rate
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pewPewSubsystem.go(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    /*return (rampUpInstance)
      ? onTarget
      : false;
      */
      return false;

  }
}
