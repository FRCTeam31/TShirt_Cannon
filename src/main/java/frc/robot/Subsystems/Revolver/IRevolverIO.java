package frc.robot.Subsystems.Revolver;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface IRevolverIO {
    
    @AutoLog
    public static class RevolverIOInputs{
        public double SensorPosition = 0;
    }

    @AutoLog
    public static class RevolverIOOutputs{ 
        public double MotorOutput = 0;
        public ControlMode RevolverControlMode = ControlMode.PercentOutput;
        public double SensorPosition = 0;
        public boolean RevolverSolenoidOpen = false;
    }

    public RevolverIOInputs getInputs();

    public void setOutputs(RevolverIOOutputs outputs);

    public void setSensorPosition(double position);
}
