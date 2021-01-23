/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.subsystems.Turret;


/**
 * An example command that uses an example subsystem.
 */
public class TurretSpinner extends BasicCommand {
  private static final double TOLERANCE = 0.5;
  protected static Turret turretSubsystem = OI.turretTurn;
  protected double targetAngle;
  protected boolean isJoystickMode;
  private boolean isOnTarget;
  // private static final double PGAIN = -0.08; //works for autonomoose turret
  // spinning test
  private static final double PGAIN = -0.08;
  private double instanceGain = PGAIN;
  private static final double MAX_OUTPUT = 0.2;

  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurretSpinner(double angle, boolean invertTurret) {
    super(turretSubsystem, 0, 0.5); // speed is caluclated by this class. 0.5 = timer
    instanceGain = (invertTurret)
      ? -PGAIN
      : PGAIN;
    this.targetAngle = angle;
    isJoystickMode = false;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turretSubsystem);
  }
  

  public TurretSpinner() {
    super(turretSubsystem, 0); // speed is caluclated by this class. 0.5 = timer
    this.targetAngle = 0.0;
    isJoystickMode = true;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turretSubsystem);
  }

  public boolean getIsOnTarget(){
    return isOnTarget;
  }



  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
    System.out.println("starting a turret turn loop " + instanceGain);
    isOnTarget = isJoystickMode;
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute(){
    double currentAngle = turretSubsystem.getAngle();
    if(isJoystickMode){
      double joystickPowerValue = joystickAttack.getX(); 
      letsTurn(joystickPowerValue);
      //SmartDashboard.putNumber("Joystick spinning Power ", joystickPowerValue);
    }
    else{
      //potentially deprecating this logic in favor of PID logic in TurretPosition
      double angleError = targetAngle-currentAngle;
      double newPowerValue = angleError*instanceGain; 
      isOnTarget = Math.abs(angleError) < TOLERANCE; //check if within TOLERANCE
      //TODO: test for error 
      //SmartDashboard.putNumber("Turret spinning Power ", newPowerValue);
     // System.out.println("Turret spinning Power " + newPowerValue + " Turret position " + currentAngle);
      
      letsTurn(newPowerValue);
    }

    if(joystickAttack.getRawButtonPressed(10)) {
      turretSubsystem.resetTEncoder();
    }


    SmartDashboard.putNumber("Turret position ", currentAngle);
  }


  private void letsTurn(double turnPower){
    if(turnPower > MAX_OUTPUT){
      turnPower = MAX_OUTPUT;
    }
    else if (turnPower < -MAX_OUTPUT){
      turnPower = -MAX_OUTPUT;
    }
    turretSubsystem.go(turnPower);
  }




  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turretSubsystem.go(0);
    System.out.println("end TurretSpinner. Position = " + turretSubsystem.getAngle());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (isJoystickMode)
      ? false
      //next line: if difference between current and target position is within TOLERANCE>>end
      : Math.abs(turretSubsystem.getAngle() - this.targetAngle) < TOLERANCE 
        || super.isFinished(); 
  }
}
