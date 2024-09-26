package frc.robot.subsystems.Shoulder;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Rotation2d;

public interface IShoulderIO {
    @AutoLog
    public static class ShoulderIOInputs{
        public Rotation2d ShoulderRotation = Rotation2d.fromDegrees(0);
    }

    @AutoLog
    public static class ShoulderIOOutputs{ 
        public double ShoulderAngleSpeed = 0;
    }

    public ShoulderIOInputs getInputs();

    public void setOutputs(ShoulderIOOutputs outputs);
}
