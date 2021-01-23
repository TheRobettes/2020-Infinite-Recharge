/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.BasicController;


/**
 * An example command that uses an example subsystem.
 */
public class BasicCommand extends JoystickCommands {
  protected BasicController basicSubsystem;
  private double speed;
  private boolean isTimed;
  private final Timer interruptTimer = new Timer();
  private double maxRuntime = 0;

  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public BasicCommand(BasicController subsystem, double speed) {
    basicSubsystem = subsystem;
    this.speed = speed;
    this.isTimed = false;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  public BasicCommand(BasicController subsystem, double speed, double timeoutInterval) {
    basicSubsystem = subsystem;
    this.speed = speed;
    this.isTimed = true;
    this.maxRuntime = timeoutInterval;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (isTimed){
      this.interruptTimer.reset();
      this.interruptTimer.start();
      System.out.println("starting a " + maxRuntime + " second " + basicSubsystem + " basic controller" );
    }
    else
      System.out.println("starting a " + basicSubsystem + " command");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.basicSubsystem.go(speed);
    //this.basicSubsystem.go(joystickXbox.getX());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    basicSubsystem.go(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (isTimed)
      return this.interruptTimer.get() > maxRuntime;
    return false;
  }
}
