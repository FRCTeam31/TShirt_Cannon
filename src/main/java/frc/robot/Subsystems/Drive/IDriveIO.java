package frc.robot.subsystems.Drive;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.Drive.DriveSubsystem.DriveMode;

public interface IDriveIO {
    @AutoLog
    public static class DriveIOInputs {
        public double leftSpeed;
        public double rightSpeed;
    }

    @AutoLog
    public static class DriveIOOutputs {
        public DriveMode driveMode;
        
        // Arcade
        public double speed;
        public double rotation;

        // Tank
        public double leftSpeed;
        public double rightSpeed;   
    }

    public DriveIOInputs getInputs();

    public void setOutputs(DriveIOOutputs outputs);
}
