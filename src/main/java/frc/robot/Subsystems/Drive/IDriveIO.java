package frc.robot.subsystems.Drive;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.Drive.DriveSubsystem.DriveMode;

public interface IDriveIO {
    public static class DriveIOInputs {
        public double leftSpeed;
        public double rightSpeed;
    }

    public static class DriveIOOutputs {
        public DriveMode driveMode;
        
        // Arcade
        public double speed;
        public double rotation;

        // Tank
        public double leftSpeed;
        public double rightSpeed;   
    }

    public default DriveIOInputs getInputs() {
        return null;
    }

    public default void setOutputs(DriveIOOutputs outputs) {
        // function stub
    }
}
