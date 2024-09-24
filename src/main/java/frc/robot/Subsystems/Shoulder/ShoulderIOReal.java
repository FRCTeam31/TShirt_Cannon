package frc.robot.subsystems.Shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.geometry.Rotation2d;

public class ShoulderIOReal implements IShoulderIO {
    
  private WPI_VictorSPX shoulderAngle1;
  private WPI_VictorSPX shoulderAngle2;
  private CANcoder shoulderCoder;

  private ShoulderSubsystem shoulderSubsystem;

    public ShoulderIOReal() {
    shoulderAngle1 = new WPI_VictorSPX(ShoulderSubsystem.Map.MOTOR1_CAN);
    shoulderAngle2 = new WPI_VictorSPX(ShoulderSubsystem.Map.MOTOR2_CAN);
    shoulderAngle1.setNeutralMode(NeutralMode.Brake);
    shoulderAngle2.setNeutralMode(NeutralMode.Brake);

    shoulderCoder = new CANcoder(ShoulderSubsystem.Map.SHOULDERCODER_CAN);
    var cancoderConfig = new CANcoderConfiguration();
    cancoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
    shoulderCoder.getConfigurator().apply(cancoderConfig);
  }

    @Override
    public ShoulderIOInputs getInputs() {
        var inputs = new ShoulderIOInputs();

        inputs.ShoulderRotation = Rotation2d.fromRotations(shoulderCoder.getAbsolutePosition().getValueAsDouble());

        return inputs;
    }

    @Override
    public void setOutputs(ShoulderIOOutputs outputs) {
// If the angle is oustide the acceptable bounds then set rotation to 0
if (outputs.ShoulderAngleSpeed > 0 && shoulderSubsystem.getShoulderAngle() >= ShoulderSubsystem.Map.UPPER_LIMIT)
outputs.ShoulderAngleSpeed = Math.min(0, outputs.ShoulderAngleSpeed);

if (outputs.ShoulderAngleSpeed < 0 && shoulderSubsystem.getShoulderAngle() <= ShoulderSubsystem.Map.LOWER_LIMIT)
outputs.ShoulderAngleSpeed = Math.max(0, outputs.ShoulderAngleSpeed);

        shoulderAngle1.set(outputs.ShoulderAngleSpeed);
        shoulderAngle2.set(outputs.ShoulderAngleSpeed);

    }
  
}
