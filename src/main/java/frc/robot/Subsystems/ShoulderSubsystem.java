// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
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
  private CANCoder shoulderCoder;

  /** Creates a new ShoulderSubsystem. */
  public ShoulderSubsystem() {
    shoulderAngle1 = new WPI_VictorSPX(Map.MOTOR1_CAN);
    shoulderAngle2 = new WPI_VictorSPX(Map.MOTOR2_CAN);
    shoulderAngle1.setNeutralMode(NeutralMode.Brake);
    shoulderAngle2.setNeutralMode(NeutralMode.Brake);

    shoulderCoder = new CANCoder(Map.SHOULDERCODER_CAN);
    shoulderCoder.configFactoryDefault();
    shoulderCoder.configSensorDirection(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var currentAngle = shoulderCoder.getAbsolutePosition();
    SmartDashboard.putNumber("Shoulder Angle", currentAngle);
  }

  public void changeShoulderAngle(double angleSpeed){
    // The angle speed  


    //Current angle from the encoder
    var currentAngle = shoulderCoder.getAbsolutePosition();

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

  public CommandBase controlWithTriggersCommand(CommandXboxController controller) {
    return this.run(()->{
      double angleSpeed = MathUtil.applyDeadband((controller.getRawAxis(2) - 
        controller.getRawAxis(3)) * Map.SPEED_COEFF, 0.1);
      changeShoulderAngle(angleSpeed);
    });
  }

  //#endregion
}
