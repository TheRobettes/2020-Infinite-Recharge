/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.subsystems.Chassis;


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
public final class RobotMapAisha extends RobotMap {

    protected RobotMapAisha() {
        
        super ();

        //only for Victoria use...initialize super variables here...
        leftFrontWheel = new CANSparkMax(5,  CANSparkMaxLowLevel.MotorType.kBrushless);
        leftFrontWheel.setInverted(false);
        leftBackWheel = new Talon(11);
        rightFrontWheel = new CANSparkMax(7,  CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFrontWheel.setInverted(true);
        rightBackWheel = new Talon(13);
        invertMotors();   

        //other non-drive train actuators (motors)
        conveyor = new Talon (15);
        pewPewPew = new Talon (1);
            pewPewPew.setInverted(true);
        shinnyUp = new Talon (5);
        //CANSparkMax turretTurn = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushed);
        turretTurn = new Talon (14);
        intakeArm = new Talon(17);
        intakeBrake = new Solenoid(0);
        pushUp = new Solenoid(1);
        
        //sensors
        pewPewSpeed = invertGeneric(new Encoder(2,3), true);

        //old way
        /*pewPewSpeed = new GenericEncoder(new Encoder (2,3))
         {@Override
            public double getRate(){
                return super.getRate()*0.2;
             }
         };
         */


        turretAngle = invertGeneric(new Encoder (0,1), false);
         //new GenericEncoder(turretTurn.getEncoder(EncoderType.kQuadrature, 1));
        intakeSpeed = invertGeneric(new Encoder(4,5), false);
        
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
     System.out.println(" AISHA motors initialized");
   
    }


    private static void invertMotors() {
        rightBackWheel.setInverted(false);
        rightFrontWheel.setInverted(true);
        leftFrontWheel.setInverted(true);
        leftBackWheel.setInverted(true);
        }

    @Override 
    protected boolean testVision(){
        return true;
    }

    @Override
    protected Chassis getChassis(){
        return new Chassis() {
            private DifferentialDrive aishaDrive = 
                new DifferentialDrive(RobotMap.leftFrontWheel, RobotMap.rightFrontWheel);
                
            @Override
            public void robotDrive(double strafe, double driveSpeed, double spin) {
               // driveSpeed *= 0.5; //slows speed
                // we didn't like their variable names ySpeed (strafe or our X) and xSpeed (drive or our Y)
                aishaDrive.arcadeDrive(driveSpeed*0.7, spin); 
                  
            }
        }; 
    }
}
