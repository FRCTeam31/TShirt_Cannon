// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.CTREConverter;

public class RevolverSubsystem extends SubsystemBase {
  public class Map {
    public static final int MOTOR_CAN = 8;
    public static final double MOTOR_SPEED_COEFF = 0.5;

    public static final double MOTOR_kP = 0.0;
    public static final double MOTOR_kI = 0.0;
    public static final double MOTOR_kD = 0.0;
    public static final double MOTOR_kF = 0.0;

    public static final double MOTOR_MAGIC_ACCEL = 1000; // Sensor units per 100ms, per second
    public static final double MOTOR_MAGIC_CRUISE = 1000; // Sensor units per 100ms
    public static final int MOTOR_MAGIC_S_CURVE_STRENGTH = 2; // Range: 1-8

    public static final int SOLENOID_CHANNEL = 1;
  }

  private TalonSRX motor;
  private Solenoid fireSolenoid;

  /** Creates a new RevolverSubsytem. */
  public RevolverSubsystem() {
    motor = new TalonSRX(Map.MOTOR_CAN);
    motor.clearStickyFaults();
    motor.configFactoryDefault();
    motor.setNeutralMode(NeutralMode.Brake);

    // Configure Talon sensor
    motor.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 20);
    motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 20);

    // Configure PIDF for Motion Magic
    motor.selectProfileSlot(0, 0);
    motor.config_kP(0, Map.MOTOR_kP);
    motor.config_kI(0, Map.MOTOR_kI);
    motor.config_kD(0, Map.MOTOR_kD);
    motor.config_kF(0, Map.MOTOR_kF);

    // Configure motion magic parameters
    motor.configMotionAcceleration(Map.MOTOR_MAGIC_ACCEL);
    motor.configMotionCruiseVelocity(Map.MOTOR_MAGIC_CRUISE);
    motor.configMotionSCurveStrength(Map.MOTOR_MAGIC_S_CURVE_STRENGTH);

    motor.setSelectedSensorPosition(0);

    fireSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Map.SOLENOID_CHANNEL);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Revolver Angle Pos", getRevolverRotation().getDegrees());
  }

  public Rotation2d getRevolverRotation() {
    double currentdeg = CTREConverter.MagEncoderToDegrees(getRevolverPosition(), 9 * 10);
    return Rotation2d.fromDegrees(currentdeg);
  }

  public double getRevolverPosition() {
    return motor.getSelectedSensorPosition();
  }

  public void setRevolverSpeed(double speed) {
    motor.set(ControlMode.PercentOutput, speed * Map.MOTOR_SPEED_COEFF);
  }

  public void setRevolverPositionTarget(double target) {
    motor.set(ControlMode.MotionMagic, target);
  }

  public void setFireSolenoid(boolean open) {
    fireSolenoid.set(open);
  }

  //#region Commands

  public CommandBase fireSequenceCommand(int power) {
    return fireCommand(true)
      .andThen(Commands.waitSeconds(0.01 * power))
      .andThen(fireCommand(false));
  }

  public CommandBase fireCommand(boolean open) {
    return this.runOnce(() -> setFireSolenoid(open));
  }

  public CommandBase revolveForward(){
    return this.runOnce(()->{
      motor.setSelectedSensorPosition(0);
      setRevolverPositionTarget(4096);
    });
  }

  public CommandBase revolveBackward(){
    return this.runOnce(()->{
      motor.setSelectedSensorPosition(0);
      setRevolverPositionTarget(-4096);
    });
  }

  //#end
}
