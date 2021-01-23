/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.Encoder;

/**
 *
 */
public class GenericEncoder {
  private CANEncoder canEncoderInstance;
  private Encoder dioEncoderInstance;
  private WPI_TalonSRX talonEncoderInstance;
  private WPI_TalonFX fxInstance;

  public GenericEncoder(CANEncoder canEncoderInstance) {
    this.canEncoderInstance = canEncoderInstance;
  }
  public GenericEncoder(Encoder dioEncoderInstance){
    this.dioEncoderInstance = dioEncoderInstance;
  }
  public GenericEncoder(WPI_TalonSRX talonEncoderInstance){
    this.talonEncoderInstance = talonEncoderInstance;
  }
  public GenericEncoder(WPI_TalonFX talonEncoderInstance){
    this.fxInstance = talonEncoderInstance;
  }

  //a parameter is only available as that line of code is executed

  /**
   * 
   */
  
  public double getRate(){
    double rateValue = 0;
    if(dioEncoderInstance != null){
      rateValue = dioEncoderInstance.getRate();
    } 
    else if(canEncoderInstance != null){
       rateValue = canEncoderInstance.getVelocity();
    }
    else if(fxInstance != null){
      rateValue = fxInstance.getSelectedSensorVelocity();
    }
    else{
      rateValue = talonEncoderInstance.getSelectedSensorVelocity();
    }
    return rateValue;
  }

  /**
   * 
   */
  public int getPosition(){
    //old way:
    /*return(dioEncoderInstance != null) 
      ? dioEncoderInstance.get()
      : canEncoderInstance.getPosition();*/

    //new way:
    int positionValue = 0;
    if(dioEncoderInstance != null){
      positionValue = dioEncoderInstance.get();
    } 
    else if(canEncoderInstance != null){
       positionValue = (int) canEncoderInstance.getPosition();
    }
    else{
      positionValue = - talonEncoderInstance.getSelectedSensorPosition();
    }
    return positionValue;
  }
  
}
