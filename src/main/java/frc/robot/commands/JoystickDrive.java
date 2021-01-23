/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Chassis;


/**
 * An example command that uses an example subsystem.
 */
public class JoystickDrive extends JoystickCommands {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Chassis chassisSubsystem;

//private static double halfSpeed = 0.5;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public JoystickDrive(Chassis subsystem) {
    chassisSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("joystick drive starting " + joystickXbox.getX());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double adjustedSpeed = (joystickAttack.getTwist() * -.25) + .75;
    double currentTwist = joystickAttack.getTwist();
    //math adjusment if twist
    double strafe = joystickXbox.getX();
    double driveSpeed = -joystickXbox.getY();
    driveSpeed = driveSpeed * Math.abs(driveSpeed) * adjustedSpeed;
    double spin = joystickXbox.getRawAxis(4) * 0.7; //rotation adjustment
    chassisSubsystem.robotDrive (strafe, driveSpeed, spin);
    SmartDashboard.putNumber("adjustedSpeed", adjustedSpeed);
    SmartDashboard.putNumber("currentTwist", currentTwist);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}