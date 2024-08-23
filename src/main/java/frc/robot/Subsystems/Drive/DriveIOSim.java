package frc.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.interfaces.IDriveIO;
import frc.robot.sim.MotorSim;

public class DriveIOSim implements IDriveIO {
    private final MotorSim leftMotor = new MotorSim();
    private final MotorSim rightMotor = new MotorSim();
    private final DifferentialDrive drivetrain = new DifferentialDrive(leftMotor, rightMotor);

    public DriveIOInputs getInputs() {
        DriveIOInputs inputs = new DriveIOInputs();
        inputs.leftSpeed = leftMotor.get();
        inputs.rightSpeed = rightMotor.get();

        return inputs;
    }

    public void setOutputs(DriveIOOutputs outputs) {
        if (outputs.driveMode == DriveSubsystem.DriveMode.kArcade) {
            drivetrain.arcadeDrive(outputs.speed, outputs.rotation);
        } else if (outputs.driveMode == DriveSubsystem.DriveMode.kTank) {
            drivetrain.tankDrive(outputs.leftSpeed, outputs.rightSpeed);
        }
    }
}
