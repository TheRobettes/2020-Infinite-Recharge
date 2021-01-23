/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.OI;
import frc.robot.subsystems.Turret;

/**
 * An example command that uses an example subsystem.
 */
public class TurretPosition extends CommandBase {

  // by converting angles into 'raw'-sensor values, these variables capture values to 'set' directly into a Spark-or-SRX config.
  private static final double MAXINPUT = Turret.MAXROTATION / Turret.ANGLEMULTIPLIER;

  private static final double PGAIN = -.0008;      //  note from TurretSpinner... newPowerValue = angleError*-.007; //license to kill
  private static final double IGAIN = PGAIN * .001;
  private static final double DGAIN = PGAIN * 10;
  private static final PIDController pid = new PIDController(PGAIN, IGAIN, DGAIN);


  private static final double TOLERANCE = 2.0;
  protected Turret turretSubsystem = OI.turretTurn;
  protected double targetAngle;
  private boolean isOnTarget; 


  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurretPosition(double angle) {
    this.targetAngle = angle;
    System.out.println("Turret max input" + MAXINPUT);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turretSubsystem);
  }

/*  This code could  be activated if useful for a Vision-child instance.
  public TurretPosition() {
    this.targetAngle = 0.0;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turretSubsystem);
  }
*/

  public boolean getIsOnTarget(){
    return isOnTarget;
  }



  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("starting a turret POSITION loop for turret angle" + targetAngle );
    pid.enableContinuousInput( -MAXINPUT , MAXINPUT);
  }

  // Called every time the scheduler runs while the command is scheduled.

  @Override
  public void execute(){
    double currentAngle = turretSubsystem.getAngle();
    //double newPowerValue =  pid.calculate( currentAngle, targetAngle );   
    pid.setSetpoint(targetAngle);
    double newPowerValue =  pid.calculate( currentAngle);     
      this.turretSubsystem.go(newPowerValue);
    //SmartDashboard.putNumber("Turret position ", currentAngle);
    //SmartDashboard.putNumber("Turret power ", newPowerValue);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pid.disableContinuousInput();
    turretSubsystem.go(0);
    System.out.println("ending a turret POSITION loop" );
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //return Math.abs(turretSubsystem.getAngle() - this.targetAngle) < TOLERANCE; 
    return false;
  }
}
