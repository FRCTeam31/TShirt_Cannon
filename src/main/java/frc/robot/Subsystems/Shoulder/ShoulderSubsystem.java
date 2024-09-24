package frc.robot.subsystems.Shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shoulder.IShoulderIO.ShoulderIOInputs;
import frc.robot.subsystems.Shoulder.IShoulderIO.ShoulderIOOutputs;

public class ShoulderSubsystem extends SubsystemBase {
  public class Map {
    public static final int MOTOR1_CAN = 22;
    public static final int MOTOR2_CAN = 21;
    public static final int SHOULDERCODER_CAN = 20;
    public static final double UPPER_LIMIT = 271.05;
    public static final double LOWER_LIMIT = 224.47;

    //Speed Coefficents
    public static final double SPEED_COEFF = 0.5;
  }
 private IShoulderIO shoulderIo;
  private ShoulderIOInputs shoulderInputs = new ShoulderIOInputs(); 
  private ShoulderIOOutputs shoulderOutputs = new ShoulderIOOutputs();

  /** Creates a new ShoulderSubsystem. 
 * @param isReal */
  public ShoulderSubsystem(boolean isReal) {
    if (isReal){
      shoulderIo = new ShoulderIOReal();
    } else{
      // This is where the simulated version will be
    }
  }

  @Override
  public void periodic() {
    shoulderIo.setOutputs(shoulderOutputs);
    shoulderInputs = shoulderIo.getInputs();
    // This method will be called once per scheduler run
    var currentAngle = shoulderInputs.ShoulderRotation;
    SmartDashboard.putNumber("Shoulder Angle", currentAngle.getDegrees());
  }

  public double getShoulderAngle() {
    return shoulderInputs.ShoulderRotation.getDegrees();
  }

  // public void changeShoulderAngle(double angleSpeed){
  //   // Current angle from the encoder
  //   var currentAngle = shoulderInputs.ShoulderRotation.getDegrees();

    
    
    
  // }

  //#region Commands

  public Command controlWithTriggersCommand(CommandXboxController controller) {
    return this.run(()->{
      double angleSpeed = MathUtil.applyDeadband((controller.getRawAxis(2) - 
        controller.getRawAxis(3)) * Map.SPEED_COEFF, 0.1);
      shoulderOutputs.ShoulderAngleSpeed = angleSpeed;
    });
  }

  //#endregion
}
