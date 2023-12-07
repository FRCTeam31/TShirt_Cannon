// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableInstance.NetworkMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class RevolverSubsystem extends SubsystemBase {
  private TalonSRX revolverSpinner;

  /** Creates a new RevolverSubsytem. */
  public RevolverSubsystem() {
    
    revolverSpinner = new TalonSRX(RobotMap.revolverSpinner_CAN);
    revolverSpinner.clearStickyFaults();
    revolverSpinner.configFactoryDefault();
    revolverSpinner.setNeutralMode(NeutralMode.Brake);

    revolverSpinner.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 20);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Revolver Angle Pos", getRevolverPosition().getDegrees());
  }

  public Rotation2d getRevolverPosition(){
    double currentdeg = ((revolverSpinner.getSelectedSensorPosition() % 4096) / 4096) * 360;
    return Rotation2d.fromDegrees(Math.abs(currentdeg));
  }

  public void spinRevolver(boolean leftBumperPressed, boolean rightBumperPressed){
    
    double revolverSpinnerSpeed = 0;

    if (leftBumperPressed) {
      revolverSpinnerSpeed = RobotMap.revolverSpinnerSpeedCoefficient * 1;
    } else if (rightBumperPressed) {
      revolverSpinnerSpeed = RobotMap.revolverSpinnerSpeedCoefficient * -1;
    } else{
      revolverSpinnerSpeed = 0;
    }

    revolverSpinner.set(ControlMode.PercentOutput, revolverSpinnerSpeed);
  }
}
