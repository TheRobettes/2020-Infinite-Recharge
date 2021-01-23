/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
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
public final class RobotMapVictoria extends RobotMap {

    protected RobotMapVictoria() {
        //only for Victoria use...initialize super variables here...
        leftFrontWheel = new Talon(0);
        leftBackWheel = new Talon(3);
        rightFrontWheel = new Talon(7);
        rightBackWheel = new Talon(4);
        invertMotors();
    
        

        //other non-drive train actuators (motors)
        conveyor = new Talon (15);
        pewPewPew = new Talon (1);
            pewPewPew.setInverted(true);
        shinnyUp = new Talon (5);
        //CANSparkMax turretTurn = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushed);
        
        turretTurn = new Talon(14);//turretTurn;
        intakeArm = new Talon(13);
        //this.intakeBrake = new Solenoid(0);
        //this.pushUp = new Solenoid(1);
        
        //sensors
        pewPewSpeed = new GenericEncoder(new Encoder (11, 12))
         {@Override
            public double getRate(){
                return super.getRate()*0.2;
             }
         };


        turretAngle = new GenericEncoder (new Encoder(7, 8));//(turretTurn.getEncoder(EncoderType.kQuadrature, 1));
        intakeSpeed = new GenericEncoder(new Encoder(9, 10));
        
         boolean isIntakeTesting = false;
         if(isIntakeTesting){
             //shuffle wheel motors to test as intake
             SpeedController temp = conveyor;
             conveyor = rightFrontWheel;
             rightFrontWheel = temp;

             temp = intakeArm;
             intakeArm = rightBackWheel;
             rightBackWheel = temp;
         }

        // put correct ports in
    System.out.println(" VICTORIA motors initialized");
    }
    private static void invertMotors() {
        rightBackWheel.setInverted(true);
        rightFrontWheel.setInverted(false);
        leftFrontWheel.setInverted(false);
        leftBackWheel.setInverted(false);
        }

    @Override 
    protected boolean testVision(){
        return true;
    }
    
    @Override
    protected Chassis getChassis(){
        return new Chassis() {
                
            @Override
            public void robotDrive(double strafe, double driveSpeed, double spin) {
               // driveSpeed *= 0.5; //slows speed
                // we didn't like their variable names ySpeed (strafe or our X) and xSpeed (drive or our Y)
                super.robotDrive(strafe*0.7, driveSpeed*0.3, spin*0.7); 
                  
            }
        }; 
    }

}
