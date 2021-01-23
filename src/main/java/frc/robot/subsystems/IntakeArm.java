/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class IntakeArm extends BasicController {
  private static Encoder intakeUpOrDown = RobotMap.intakeUpOrDown;
  private static final double MAX_HEIGHT = (Robot.isCompetition)
    ? Robot.intakeStartupEncoderValue - 50 // -50 so we don't crash into the bar and stall the motor
    : 450;
  private static final double MIN_HEIGHT = (Robot.isCompetition)
    ? Robot.intakeStartupEncoderValue - 1300 //-1300 //yes
    : -930; //TODO: test this


  /**
   * Creates a new ExampleSubsystem.
   */
  public IntakeArm() {
    super(RobotMap.intakeArm);
  }

  @Override
  public void go(double outputSpeed){
    double position = 0;
      if (intakeUpOrDown != null) {
        position = intakeUpOrDown.getDistance();
      }
  if (outputSpeed > 0 && position > MAX_HEIGHT ) //positive speed and positive limit is present
    outputSpeed = 0;
  if (outputSpeed < 0 && position < MIN_HEIGHT ) //negative speed and negative limit is present
    outputSpeed = 0;
    super.go(outputSpeed);
    SmartDashboard.putNumber("intake arm position ", position);
  }

}
