package frc.robot.subsystems.Drive;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveIOReal implements IDriveIO {

    private WPI_VictorSPX leftDriveMotor1;
    private WPI_VictorSPX leftDriveMotor2;
    private WPI_VictorSPX leftDriveMotor3;
    private WPI_VictorSPX rightDriveMotor1;
    private WPI_VictorSPX rightDriveMotor2;
    private WPI_VictorSPX rightDriveMotor3;
    private DifferentialDrive drivetrain;

    public DriveIOReal() {
        leftDriveMotor1 = new WPI_VictorSPX(DriveSubsystem.Map.leftMotor1_CAN);
        leftDriveMotor2 = new WPI_VictorSPX(DriveSubsystem.Map.leftMotor2_CAN); 
        leftDriveMotor2.follow(leftDriveMotor1);
        leftDriveMotor3 = new WPI_VictorSPX(DriveSubsystem.Map.leftMotor3_CAN); 
        leftDriveMotor3.follow(leftDriveMotor1);

        rightDriveMotor1 = new WPI_VictorSPX(DriveSubsystem.Map.rightMotor1_CAN);
        rightDriveMotor2 = new WPI_VictorSPX(DriveSubsystem.Map.rightMotor2_CAN); 
        rightDriveMotor2.follow(rightDriveMotor1);
        rightDriveMotor3 = new WPI_VictorSPX(DriveSubsystem.Map.rightMotor3_CAN); 
        rightDriveMotor3.follow(rightDriveMotor1);

        drivetrain = new DifferentialDrive(leftDriveMotor1, rightDriveMotor1); /* reversed purposefully */ 
    }

    @Override
    public DriveIOInputs getInputs() {
        var inputs = new DriveIOInputs();
        inputs.leftSpeed = leftDriveMotor1.get();
        inputs.rightSpeed = rightDriveMotor1.get();

        return inputs;
    }

    @Override
    public void setOutputs(DriveIOOutputs outputs) {
        if (outputs.driveMode == DriveSubsystem.DriveMode.kArcade) {
            drivetrain.arcadeDrive(outputs.speed, outputs.rotation);
        } else if (outputs.driveMode == DriveSubsystem.DriveMode.kTank) {
            drivetrain.tankDrive(outputs.leftSpeed, outputs.rightSpeed);
        }
    }
}
