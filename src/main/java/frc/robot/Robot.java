/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/* Major todo agenda
---WPI Update!!!!!!!!!!!!!!
- complete chooser code- for auto selection 
- get code deployed on comp
- put can ID's on comp (falcon and sparkmax)
- test inversions on motors
- test turret encoder for values - conversion into degrees
- test encoder on intake arm if absolute or not
- test pneumatic solenoid ID's 
- test climber on comp
- test shooting speed on comp
- test auto shoot on comp
- test vision shoot on comp
- test conveyor to shooter on comp
- test intaking balls on comp
- test turret turn on comp
- test limit switches 
- new code for automatic vision encoder reset

/* minor todo agenda
- distance calc
- auto shoot from sides 
- move image rect calc from target analysis to distance finder
- use github

/*----------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomoose.DontDrive;
import frc.robot.commands.TestDrive;
import frc.robot.vision.CameraControl;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot { 
  private static boolean isVictoria= false;
  private static boolean isAisha = false;
  public static boolean isCompetition = true;
  //TODO: optional another static boolean could define two infinite recharge robots
  private Command m_autonomousCommand;
  public static RobotMap map = null;

  public static int intakeStartupEncoderValue; 

//TODO turret angle absulute limit switch 
//TODO fix strafing (investigate weight imbalance, speed encoder vs power)

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    if (isVictoria){
      map = new RobotMapVictoria();
    } 
    else if(isAisha){
      map = new RobotMapAisha();
    }
    else{
      map = new RobotMap(isCompetition);
    }
    new OI();

    CameraControl.cameraInit();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = OI.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    //we don't have an absolute encoder for intake so this will get the initial value and set it to 0
    intakeStartupEncoderValue =  RobotMap.intakeUpOrDown.get();

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandGroupBase.sequence(
      new TestDrive(),
      new DontDrive()
      /*new BasicCommand(OI.intakeArm, 0.5, 1.0),
      new BasicCommand(OI.ballIntake, 0.5, 1.0),
      new BasicCommand(OI.intakeBrake, 0.5, 1.0),
      new BasicCommand(OI.turretTurn, 0.5,0.5),
      new PrintCommand("Beginning turret return..."),
      new TurretSpinner(0.0),
      new BasicCommand(OI.pewPewPew, 0.5, 1.0),
      new PrintCommand("pewPew performed!"),
      new BasicCommand(OI.pushUp, 0.5, 0.5),
      CommandGroupBase.parallel(
        new BasicCommand(OI.shinnyUp,-0.5),
        new BasicCommand(OI.pushUp, -0.5)
      )*/
    ).schedule(); //excute the newly-created command
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

}
