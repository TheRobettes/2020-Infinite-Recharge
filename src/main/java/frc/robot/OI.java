/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.autonomoose.Autonomoose;
import frc.robot.autonomoose.DontDrive;
import frc.robot.commands.*;
import frc.robot.commands.JoystickCommands.JoystickType;
import frc.robot.subsystems.*;
import frc.robot.vision.VisionShooting;
import edu.wpi.first.wpilibj2.command.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class OI {
  // The robot's subsystems and commands are defined here...
  // old static way 
  //public static final Chassis chassis = RobotMap.getChassis(); //new Chassis();
  
  public static final Chassis chassis = Robot.map.getChassis();
  public static final BallFeeder conveyor = new BallFeeder(); // aka BasicController(RobotMap.conveyor);
  public static final BasicController shinnyUp = new BasicController(RobotMap.shinnyUp);
  public static final Shooter pewPewPew = new Shooter(); // aka BasicController(RobotMap.pewPewPew);
  public static final Turret turretTurn = new Turret(); // aka BasicController(RobotMap.turretTurn);
  public static final BasicController intakeArm = new IntakeArm(); //new BasicController(RobotMap.intakeArm);
  public static final BasicController intakeBrake = new BasicController(RobotMap.intakeBrake);
  public static final BasicController pushUp = new BasicController(RobotMap.pushUp);
  
  //private static final JoystickCommands m_autoCommand = new JoystickCommands();


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public OI() {
    chassis.setName("Chassis");
    conveyor.setName("Ball Intake");
    shinnyUp.setName("Shinny Up");
    pewPewPew.setName("Pew! Pew! Pew!");
    turretTurn.setName("Turret Turn");
    intakeArm.setName("Intake Arm");
    intakeBrake.setName("Intake Brake");
    pushUp.setName("Push Up");
  

    //completes subsystem initialization
    //intakeArm.addPositiveLimit(RobotMap.armUpLimit);
    //intakeArm.addNegativeLimit(RobotMap.armDownLimit);
    //intakeBrake.addPositiveLimit(RobotMap.brakeOpenLimit);
    //intakeBrake.addNegativeLimit(RobotMap.brakeClosedLimit); intake is a solenoid!

    // Configure the button bindings
    configureButtonBindings();
    
    chassis.setDefaultCommand(new JoystickDrive(chassis));

    turretTurn.setDefaultCommand(new TurretSpinner());

    intakeArm.setDefaultCommand(new BasicCommand(intakeArm, 0.9));
    /*this.pewPewPew.setDefaultCommand(new BasicCommand(pewPewPew, 0.5));
    this.shinnyUp.setDefaultCommand(new BasicCommand(shinnyUp, 0.5));*/

    if (Robot.map.testVision()) {
      // the old way of "if" block if(Robot.isVictoria || Robot.isAisha){

      //pewPewPew.setDefaultCommand(new VisionShooting('C'));
    }
    
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    
    System.out.println("setting button bindings"); 
    
    //pick up and shooting commands
     JoystickCommands.whileCommand(JoystickType.Attack, 1, setUpballPickUp());
     JoystickCommands.whileCommand(JoystickType.Attack, 2, setUpManualShooting(0));
     JoystickCommands.whileCommand(JoystickType.Attack,4,new VisionShooting('L')); //character
     JoystickCommands.whileCommand(JoystickType.Attack,3,new VisionShooting('C')); //value
     JoystickCommands.whileCommand(JoystickType.Attack,5,new VisionShooting('R'));
     JoystickCommands.whileCommand(JoystickType.Attack, 11, new BasicCommand(conveyor, -0.5)); //conveyor down to avoid jams
    JoystickCommands.whileCommand(JoystickType.Attack, 8, new BasicCommand(intakeArm, 0.4));

   
     //shooter tesdt commands 
     //JoystickCommands.whileCommand(JoystickType.Attack, 8, new TurretSpinner(10, false));
     //JoystickCommands.whileCommand(JoystickType.Attack, 9, new TurretSpinner(-10, false));
     // they don't work 

    //ending game commands
    JoystickCommands.whileCommand(JoystickType.Attack, 6, new BasicCommand(pushUp,0.5));
    JoystickCommands.whileCommand(JoystickType.Attack, 7, 
      CommandGroupBase.parallel(
        new BasicCommand(shinnyUp,-0.5),
        new BasicCommand(pushUp, -0.5)
      )

    //reset robot turret encoder's position
    //see turret spinning.execute
    //BUTTON 10

    );
    
  };
  
  
    /*
    REFERENCE CODE FOR HOW A METHOD ABBREVIATES CODE STATEMENTS
    
    private void configureButtonBindings() { 
     Joystick attack = new Joystick(1); 

    // Intake Commands:
    new JoystickButton(attack, 3).whileHeld(new BasicCommand(this.conveyor, 0.5));

    // Shooter Commands:
    //new JoystickButton(attack, 1).whileHeld(new BasicCommand(this.pewPewPew, 0.7));
    new JoystickButton(attack, 8).whileHeld(new ManualShooting(0.0, false)); //competition robot will need an actual speed value (rpm)
    
    // Climber Commands:
    makeWhileCommand(attack, 11, this.shinnyUp,0.5);
    
    // Turret Commands:
    makeWhileCommand(attack, 4, this.turretTurn,-0.5);
    makeWhileCommand(attack, 5, this.turretTurn, 0.5);
  }

  public static void makeWhileCommand(Joystick humanID, int buttonNumber, BasicController subsystem, double speed) {
    new JoystickButton(humanID, buttonNumber).whileHeld(new BasicCommand(subsystem, speed));

  }
  */
  
  //dead code?
  public static Command setUpTrenchShooting(double shootingPower) {
    boolean isBasicCommandShooting = true;
    Command ballFeederCommand = (isBasicCommandShooting)
      ? CommandGroupBase.sequence(
          new BasicCommand(conveyor, -0.5, 0.5), //conveyor goes down to avoid dead space
          new BasicCommand (conveyor, 0.9))
      : new FixedBallFeed(); //FIX PLZ


    CommandGroupBase cg  = CommandGroupBase.parallel(
        new BasicCommand(intakeBrake, -0.4, 0.5), //changed to negative at comp
                new ManualShooting(shootingPower),   //TODO rpms??
        ballFeederCommand,
        new DontDrive(),
        new TurretSpinner(-14, false)
        );

  
    return cg;
  
  }
  
  public static Command setUpManualShooting(double shootingPower) {
    boolean isBasicCommandShooting = true;
    Command ballFeederCommand = (isBasicCommandShooting)
      ? CommandGroupBase.sequence(
          new BasicCommand(conveyor, -0.5, 1.0), //conveyor goes down to avoid dead space
          new BasicCommand (conveyor, 0.9))
      : new FixedBallFeed(); //FIX PLZ


    CommandGroupBase cg  = CommandGroupBase.parallel(
        new BasicCommand(intakeBrake, -0.4, 0.5),
        new ManualShooting(shootingPower),   
        ballFeederCommand,
        new DontDrive(),
        new BasicCommand(turretTurn, 0)
        );

  
    return cg;
  
  }

  public static Command setUpballPickUp() {
    return CommandGroupBase.parallel(
      new BasicCommand(intakeBrake, 0.4),
      new BasicCommand(intakeArm, -0.8),
      new BasicCommand(conveyor, 0.5)
      );
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public static Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    
      return Autonomoose.setUpAutonomooseContainers();
      
  }
}
