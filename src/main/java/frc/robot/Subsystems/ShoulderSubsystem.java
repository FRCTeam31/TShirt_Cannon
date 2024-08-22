// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

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

  private WPI_VictorSPX shoulderAngle1;
  private WPI_VictorSPX shoulderAngle2;
  private CANcoder shoulderCoder;

  /** Creates a new ShoulderSubsystem. */
  public ShoulderSubsystem() {
    shoulderAngle1 = new WPI_VictorSPX(Map.MOTOR1_CAN);
    shoulderAngle2 = new WPI_VictorSPX(Map.MOTOR2_CAN);
    shoulderAngle1.setNeutralMode(NeutralMode.Brake);
    shoulderAngle2.setNeutralMode(NeutralMode.Brake);

    shoulderCoder = new CANcoder(Map.SHOULDERCODER_CAN);
    var cancoderConfig = new CANcoderConfiguration();
    cancoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
    shoulderCoder.getConfigurator().apply(cancoderConfig);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var currentAngle = shoulderCoder.getAbsolutePosition();
    SmartDashboard.putNumber("Shoulder Angle", currentAngle.getValueAsDouble());
  }

  public void changeShoulderAngle(double angleSpeed){
    // Current angle from the encoder
    var currentAngle = shoulderCoder.getAbsolutePosition().getValueAsDouble();

    // If the angle is oustide the acceptable bounds then set rotation to 0
    if (angleSpeed > 0 && currentAngle >= Map.UPPER_LIMIT)
      angleSpeed = Math.min(0, angleSpeed);

    if (angleSpeed < 0 && currentAngle <= Map.LOWER_LIMIT)
      angleSpeed = Math.max(0, angleSpeed);

    //Set motor speed
    shoulderAngle1.set(angleSpeed);
    shoulderAngle2.set(angleSpeed);
  }

  //#region Commands

  public Command controlWithTriggersCommand(CommandXboxController controller) {
    return this.run(()->{
      double angleSpeed = MathUtil.applyDeadband((controller.getRawAxis(2) - 
        controller.getRawAxis(3)) * Map.SPEED_COEFF, 0.1);
      changeShoulderAngle(angleSpeed);
    });
  }

  //#endregion
}
