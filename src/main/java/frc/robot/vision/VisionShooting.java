/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import frc.robot.OI;
import frc.robot.commands.BasicCommand;
import frc.robot.commands.FixedBallFeed;
import frc.robot.commands.ManualShooting;
import frc.robot.commands.TurretPosition;
import frc.robot.commands.TurretSpinner;
import frc.robot.utilities.DistanceFinder;


/**
 * An example command that uses an example subsystem.
 */
public class VisionShooting extends ManualShooting {

private static final int MAXSHOOTERSPEED = 5600;
private static final int MINSHOOTERSPEED = 4500;

private static final double ANGLEMULTIPLIER = 0.4; 
//convert percentae (target x position) into degrees

private char fieldPosition; 
private int hintAngle = 0;  

  private TurretSpinner hintPostionCommand = null;

  private TurretSpinner pointNShoot = new TurretSpinner()
         { @Override
          public void initialize() {
            super.initialize(); 
            isJoystickMode=false;
          }
       
          @Override
          public void execute(){
            double targetXPosition = 0;
            switch(fieldPosition){ //button becomes field position value
              case 'L' : targetXPosition = TargetAnalysis.targetXLeft; 
              break;
              case 'R' : targetXPosition = TargetAnalysis.targetXRight;
              break;
              case 'C' : targetXPosition = TargetAnalysis.targetXCenter;
              break;
            }
            //calculate new turret angle position. Convert targetXposition into degrees
            this.targetAngle = 
              targetXPosition*ANGLEMULTIPLIER + turretSubsystem.getAngle();
            
              SmartDashboard.putNumber("Vision Target Angle", targetAngle);

            //old way
            //this.targetAngle += TargetAnalysis.targetXPosition*ANGLEMULTIPLIER;
            //TODO: calculate angle setting from target x position, set angle for turret to move to
            super.execute();
            
          }
          @Override
          public boolean isFinished() {
            return false;
          }
         };
  
  private CommandGroupBase feedNFire  = CommandGroupBase.sequence(
      //new BasicCommand(OI.intakeBrake, 0.4, 0.05),
      new BasicCommand(OI.conveyor, -0.5, 0.5),
      new FixedBallFeed()
     
      //new DontDrive()
  );

  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  
  
  public VisionShooting(char fieldPosition) {
    super(10); //makes isJoystickMode false (so will not use joystick speed)
    isJoystickModeSpeed = false;
    this.fieldPosition = fieldPosition;
    hintPostionCommand = new TurretSpinner(hintAngle, false);
  }
  
   public VisionShooting(char fieldPosition, int hintAngle) {
    super(10); //makes isJoystickMode false (so will not use joystick speed)
    isJoystickModeSpeed = false;
    this.fieldPosition = fieldPosition;
    this.hintAngle = hintAngle;
    hintPostionCommand = new TurretSpinner(hintAngle, false);


  }

  private void shootingSpeedCalc(){
    double filteredDistance = DistanceFinder.filterDistance(TargetAnalysis.distance);
    if(filteredDistance == 0)
      return; //collect more vision samples before calculating
    this.speed = 150* filteredDistance + 0;
    System.out.println("Shooting Speed Calc " + this.speed + " distance " + TargetAnalysis.distance);
    this.speed = 4500;
    //TODO: test and fix the equation for speed
    if(speed > MAXSHOOTERSPEED) {
      speed = MAXSHOOTERSPEED;
    }

    if (speed < MINSHOOTERSPEED) {
      speed = MINSHOOTERSPEED;
    }
  }


 

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize(); 
    CameraControl.setPipelineState(true);
    speed = 0;
    DistanceFinder.resetFinder();
    System.out.println("VISION INITIALIZED");
    startChildCommand(hintPostionCommand);      

  }

  @Override
  public void execute(){
    
    if(TargetAnalysis.isImageGood){    
      startChildCommand(pointNShoot);
      
      if (pointNShoot.getIsOnTarget()){

        startChildCommand(feedNFire);
       if(speed == 0){
        shootingSpeedCalc(); //set an acutal speed based on distance
       }
      }  
     /* else {
        feedNFire.cancel();
      }*/
    }
    /*else{
      pointNShoot.cancel();
      feedNFire.cancel(); //this causes an end
     }*/

    if (speed == 0){ // if it isn't lined up or it doesn't see contours, no shoot 
      this.pewPewSubsystem.go(0);
    }
    else{
      super.execute();
    }
  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    this.pointNShoot.cancel();
    this.feedNFire.cancel();
    CameraControl.setPipelineState(false);
    System.out.println("VISION ENDED");
  }

  private void startChildCommand(Command childCommand){
    if(!childCommand.isScheduled()){
      childCommand.schedule();
    }
  }
}