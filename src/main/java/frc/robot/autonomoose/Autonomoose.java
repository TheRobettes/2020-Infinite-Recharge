/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autonomoose;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.OI;
import frc.robot.commands.*;
//import sun.awt.www.content.audio.wav;
import frc.robot.vision.VisionShooting;

public class Autonomoose {
  
  private static Command setUpAutonomousShooter () {
    return CommandGroupBase.parallel(
        new ManualShooting(4500),
        new FixedBallFeed() //turns on conveyor but not until shooter @ target speed
    ); // 

  }

  public static Command setUpAutonomooseContainers() {
   System.out.println("creating autonomoose command group");
    boolean turretAccuracyTesting = false;  
    if (turretAccuracyTesting){
    return CommandGroupBase.sequence(
      new TurretSpinner(10, false),
      new WaitCommand(3),
      new TurretSpinner(-10, false),
      new WaitCommand(3),
      new TurretSpinner(-7.5, false),
      new WaitCommand(3),
      new TurretSpinner(25, false)
    );
   } 
  

  CommandGroupBase cg1NoTurn  = 
      /*SAVE - A TYPICAL SEQUENCE PROTOTYPE
      CommandGroupBase.sequence(
      new TestDrive()    // i.e. 1.
      new BasicCommand(subsystem, speed),   // i.e. 2. 
      CommandGroupBase.parallel(        // i.e. 3. 
          new BasicCommand(subsystem, speed),  // i.e. 3.a.
          new BasicCommand(subsystem, speed)   // i.e. 3.b.
          )*/     
    CommandGroupBase.sequence( 
      new DistanceDrive(),
      new VisionShooting('C', - 14) //TODO check use vision shooting eventually
      );

  Command justDrive = 
    CommandGroupBase.sequence(   
      new DistanceDrive()
    
      //new TurretSpinner(7, false), //TODO actual value will be an angle in degrees 
      /*new VisionShooting('L', 7), //use vision shooting eventually //TODO: turned off vision shooting in auto
      //new TurretSpinner(0, false)*/
    );

  Command trenchDriveNShoot = 
    CommandGroupBase.sequence(
      new DistanceDrive(),
      //new TurretSpinner(1.95, false) //not needed if distance drive goes stright line
      setUpAutonomousShooter()
    ); //TODO: is it curving as it drives... what distance... what speed?

  Command cg3wall = 
    CommandGroupBase.sequence(   
      //new TurretSpinner(-7, false), //TODO actual value will be an angle in degrees 
      new DistanceDrive(),
      
      new VisionShooting('R', -7), //use vision shooting eventually
      new TurretSpinner(0, false)

    );

    return trenchDriveNShoot;
  
  }
 
}