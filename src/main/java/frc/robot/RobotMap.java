package frc.robot;

public class RobotMap {
    // Can Ids
    public static final int leftDriveMotor1_CAN = 4;
    public static final int leftDriveMotor2_CAN = 1;
    public static final int leftDriveMotor3_CAN = 2;
    public static final int rightDriveMotor1_CAN = 3;
    public static final int rightDriveMotor2_CAN = 12;
    public static final int rightDriveMotor3_CAN = 13;
 
    public static final int shoulderAngle1_CAN = 22;
    public static final int shoulderAngle2_CAN = 21;
    public static final int shoulderCoder_CAN = 20;
    public static final double shoulder_UpperLimit = 271.05;
    public static final double shoulder_LowerLimit = 224.47;


    public static final int revolverSpinner_CAN = 8;

 

    //Speed Coefficents
    public static final double shoulderAngleSpeedCoefficient = 0.5;

    public static final double revolverSpinnerSpeedCoefficient = 0.5;

    public static final int fireSolenoidChannel = 1;


    //Joystick Constants

    public static final int buttonY = 0;

    // Pneumatic Constants
    public static final double shotDuration = 0.001;
}