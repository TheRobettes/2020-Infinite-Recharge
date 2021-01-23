/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.subsystems.Chassis;


/**
 * An example command that uses an example subsystem.
 */
public class TestDrive extends JoystickCommands {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private Chassis chassis = OI.chassis;
  private final Timer testTime = new Timer();
  private static final boolean isWheelTesting = true;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TestDrive() {
    
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(chassis);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("test drive starting" );
    testTime.reset();
    testTime.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(isWheelTesting){
    //hence follows thus: a wheel by wheel test
      if(testTime.get() < 2.0){
        RobotMap.leftFrontWheel.set(0.5);
      }
      else if(testTime.get() < 4.0){

        RobotMap.leftBackWheel.set(0.5);
      }
      else if(testTime.get() < 6.0){
        RobotMap.rightFrontWheel.set(0.5);
      }
      else if(testTime.get() < 8.0) {
        RobotMap.rightBackWheel.set(0.5);
      }
    }
  else{
  //hence follows thus: a drive direction test
    if (testTime.get() < 2.0) {
          //  strafe right ...
         chassis.robotDrive(0.25, 0, 0);
    } 
    else if (testTime.get() < 4.0) {
      //  drive forward ...
      chassis.robotDrive(0, 0.25, 0);
    }
    else {
      // spin ...
      chassis.robotDrive(0, 0, 0.25);
    }
  }
  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    chassis.robotDrive(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    int timeOutDuration = isWheelTesting
    ? 12
    : 6;
    return (testTime.get() > timeOutDuration);
  }
}
