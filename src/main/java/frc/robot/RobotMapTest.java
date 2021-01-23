/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.EncoderType;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Chassis;
import frc.robot.utilities.GenericEncoder;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public class RobotMapTest extends RobotMap {

    

    


    public RobotMapTest (boolean isCompetition) {
        super();
        //initialization code for Infinite Recharge robot motors
        //TODO: leftFrontWheel = ... new frx(can#) ... 
        leftFrontWheel = new Talon(2);
            leftFrontWheel.setInverted(true); //inverted left wheels
        leftBackWheel = new Talon(3);
            leftBackWheel.setInverted(true);
        rightFrontWheel = new Talon(5);
        rightBackWheel = new Talon(4);

        CANSparkMax tempPewPew = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
        CANSparkMaxLowLevel.MotorType pewPew2Type = (isCompetition)
            ? CANSparkMaxLowLevel.MotorType.kBrushless //yes
            : CANSparkMaxLowLevel.MotorType.kBrushed; //no
        CANSparkMax tempPewPew2 = new CANSparkMax(6, pewPew2Type);
            tempPewPew.setInverted(true);
            tempPewPew2.setInverted(false);
            pewPewPew = new SpeedControllerGroup (tempPewPew, tempPewPew2);
            pewPewPew.setInverted(true);
            //TODO fix this ^^ what is this?? We don't know... fix whatever this pew pew pew ew
       
            conveyor = new Talon (0); //also intakes the balls
            conveyor.setInverted(true);

        intakeArm =  new WPI_VictorSPX(7);  //TODO recheck connected controller...

         
        if(isCompetition){
            WPI_TalonSRX turretTurnComp = new WPI_TalonSRX(8); 
            turretTurn = turretTurnComp;

            // TODO .. still need to assign a sensor-type as the controller's input..
            // turretTurnComp.setSensorPhase(PhaseSensor);
            //    deprecated way... turretTurnComp.setFeedbackDevice(FeedbackDevice.QuadEncoder);
            //turretTurnComp.configSelectedFeedbackSensor( FeedbackDevice.QuadEncoder ); 
            
            
            // now store sensor-access for subsystem's use (makes talon robot-indpendant!)
            turretAngle = new GenericEncoder(turretTurnComp);
        } 
        else{
            SpeedController turretTurnPractice =  new WPI_VictorSPX(8);
            turretTurn = turretTurnPractice;
            CANEncoder turretEncoder = null;
            turretEncoder = tempPewPew2.getEncoder(EncoderType.kQuadrature, 1);            
            turretAngle = new GenericEncoder(turretEncoder);
        }

        shinnyUp = (isCompetition)
            ? new Talon(9)//? new WPI_TalonFX(9)
            : new Talon(1);

        if (isCompetition){
            intakeBrake = new Solenoid(0);
            pushUp = new Solenoid(1);
        }




        //sensors
        pewPewSpeed = new GenericEncoder(tempPewPew.getEncoder()) {
            @Override
                public double getRate(){
                return super.getRate()*-1.0;
                }
            };
        intakeSpeed = new GenericEncoder(new Encoder(4, 5));
        
        intakeUpOrDown = new Encoder(0, 1, 2);
        }
    
}