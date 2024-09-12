package frc.robot.subsystems.Revolver;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface IRevolverIO {
    
    @AutoLog
    public static class RevolverIOInputs{
        public double SensorPosition;
    }

    @AutoLog
    public static class RevolverIOOutputs{ 
        public double MotorOutput;
        public ControlMode RevolverControlMode;
        public double SensorPosition;
        public boolean RevolverSolenoidOpen;
    }

    public RevolverIOInputs getInputs();

    public void setOutputs(RevolverIOOutputs outputs);

    public void setSensorPosition(double position);
}
