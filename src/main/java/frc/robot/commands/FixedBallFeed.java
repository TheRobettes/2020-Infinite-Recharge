/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.BallFeeder;


/**
 * An example command that uses an example subsystem.
 */
  public class FixedBallFeed extends JoystickCommands {
  private BallFeeder conveyor = OI.conveyor;
  private final double SPEED = 1.0;
  private double previousFeederPower;

  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public FixedBallFeed() {


    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("starting a fixed speed shooting loop" );
    previousFeederPower = 0.5;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    double currentSpeed = conveyor.getRate();
    double speedError = SPEED - currentSpeed;
    double powerAdjustment = speedError * 0.001;
    double feederPower = powerAdjustment + previousFeederPower; 

// TODO the following overrides pid(P)-calculated powers,
//    ( try testing with this, after an encoder is functional on the commpetition bot ).
    //if (!Robot.isCompetition)
      feederPower = 0.9; //always 90% power

  

    if(ManualShooting.isPewPewPrepared()){
      this.conveyor.go(feederPower);
      previousFeederPower = feederPower;
    }
    else{
      this.conveyor.go(0);
      //next line should not be allowed
      //ManualShooting.onTarget = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    conveyor.go(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
