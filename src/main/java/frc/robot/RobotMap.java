/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.EncoderType;

import edu.wpi.first.wpilibj.*;
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
public class RobotMap {

    //everything in this section is a class variable
    private static final boolean isPIDDrive = true;
    private static final int maxPIDWheelCount = 2;


    //drive motors
    public static SpeedController leftFrontWheel;
    public static SpeedController leftBackWheel;
    public static SpeedController rightFrontWheel;
    public static SpeedController rightBackWheel;
    
    //actuator motors
    public static SpeedController conveyor;
    public static SpeedController pewPewPew;
    public static SpeedController shinnyUp;
    public static SpeedController turretTurn;
    public static SpeedController intakeArm; //plus (positive) is up; minus (negative) is down

    //actuator pneumatics
    public static Solenoid intakeBrake; //plus (positive) is open; minus (negative) is closed
    public static Solenoid pushUp;

    //sensors
    public static GenericEncoder pewPewSpeed;
    public static GenericEncoder turretAngle;
    public static GenericEncoder intakeSpeed;
    public static Encoder intakeUpOrDown;

    //competition only drive wheel sensors
    private static GenericEncoder pidDriveSensor;
    private static GenericEncoder nonPIDDriveSensor;

    //limit switches
    //public static DigitalInput armUpLimit; using absolute encoder instead 
    //public static DigitalInput armDown;
    //public static DigitalInput brakeOpenLimit; intake is a solenoid!
    //public static DigitalInput brakeClosedLimit;
    private static final int kTimeoutMs = 30;

    public RobotMap (boolean isCompetition) {
        //initialization code for Infinite Recharge robot motors
        //TODO: leftFrontWheel = ... new frx(can#) ... 
        //TODO: Add set Overdrive
        

        int driveWheelNumber = 0;

        leftFrontWheel = (driveWheelNumber++ > maxPIDWheelCount) ? new WPI_TalonFX(2): newDriveMotor(2);
            leftFrontWheel.setInverted(true); //inverted left wheels
        leftBackWheel = (driveWheelNumber++ > maxPIDWheelCount) ? new WPI_TalonFX(3) : newDriveMotor(3);
            leftBackWheel.setInverted(true);
        rightFrontWheel = (driveWheelNumber++ > maxPIDWheelCount) ? new WPI_TalonFX(1) : newDriveMotor(1);
        rightBackWheel = (driveWheelNumber++ > maxPIDWheelCount) ? new WPI_TalonFX(4) : newDriveMotor(4);


        //connecting CAN #5 to a brushless motor
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
        intakeArm.setInverted(isCompetition);
         
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
            ? new WPI_TalonFX(9)
            : new Talon(1);

        if (isCompetition) //TODO: NEED TO FIX
        { 
            intakeBrake = new Solenoid(1);
            pushUp = new Solenoid(2);
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

    protected RobotMap() {
        //only for Victoria use...do nothing here...
    }

    private static SpeedController newDriveMotor(int portNumber){
        if (nonPIDDriveSensor == null){
            WPI_TalonFX falcon = new WPI_TalonFX(portNumber);
            nonPIDDriveSensor = new GenericEncoder(falcon); 
            System.out.println("starting non PID encoder instance ("+portNumber+")");
            return falcon;
        }
        WPI_TalonFX falcon = new WPI_TalonFX(portNumber){
            @Override //#notlikeotherspeedcontrollers
                public void set(double motorvalue){
                    if(isPIDDrive&&true){
                    // TODO: RPM adjustment for motor
                        double feetPerSecondAdjustment = 150;
                        super.set(TalonFXControlMode.Velocity,motorvalue*feetPerSecondAdjustment);
                    }
                    else
                        super.set(TalonFXControlMode.PercentOutput,motorvalue);
                }
            };

        if (isPIDDrive) {
            System.out.println("starting PID for FX ("+portNumber+")");
            //create PID loops for SpeedController
            falcon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,0, kTimeoutMs);
            falcon.config_kP(0, 0.5, 100);
            falcon.config_kI(0, 0.0, 100);
            falcon.config_kD(0, 0.0, 100);
        }
        return falcon; 
    }

    protected Chassis getChassis(){
        return new Chassis();
    }

    protected boolean testVision(){
        return false;
    }


    protected GenericEncoder invertGeneric(Encoder basic, boolean isInverted){
        
        return isInverted
        ? new GenericEncoder(basic){
            @Override
           public double getRate(){
               return super.getRate()*-1.0;
            }
        }
        : new GenericEncoder(basic);

    }
}