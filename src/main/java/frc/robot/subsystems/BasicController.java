/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BasicController extends SubsystemBase {
  private SpeedController basicMotor = null;

  private DigitalInput positiveLimit;
  private DigitalInput negativeLimit; 

  private Solenoid basicSolenoid = null;

 private boolean previousPistonState = false; 

  /**
   * Creates a new ExampleSubsystem.
   */

  public BasicController(SpeedController basicMotor) {
    this.basicMotor = basicMotor; 

    System.out.println("BasicMotor initialized");
  }
  public BasicController(Solenoid basicSolenoid){
    this.basicSolenoid = basicSolenoid;

    System.out.println("BasicSolenoid initialized");
  }

  public void addPositiveLimit (DigitalInput limit) {
    this.positiveLimit = limit;
  }

  public void addNegativeLimit (DigitalInput limit) {
    this.negativeLimit = limit;
  }

  public void go (double speed){
    if (this.basicMotor != null) {
      moveMotor(speed);
    }   
    else if (this.basicSolenoid != null){
      movePiston(speed);
    }
    //else this must be Victoria with no pneumatic controller
  }

  protected void moveMotor (double speed) {
    double outputSpeed = speed;
    if (outputSpeed > 0 && positiveLimit != null && positiveLimit.get()) //positive speed and positive limit is present
      outputSpeed = 0;
    if (outputSpeed < 0 && negativeLimit != null && negativeLimit.get()) //negative speed and negative limit is present
      outputSpeed = 0;
    //else is to keep output speed unchanged

    basicMotor.set(outputSpeed);
  }

  private void movePiston (double speed) {
    if (speed == 0){
      return;
    }
    
    boolean isExtending = speed > 0;
      basicSolenoid.set(isExtending);

      if(isExtending != this.previousPistonState){
      System.out.println("piston changed to" + isExtending);
      }
      
      this.previousPistonState = isExtending;
      
    }

  @Override 
  public String toString(){
    return getName();
  } 
    
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
