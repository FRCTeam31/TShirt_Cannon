// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class DriveSubsystem extends SubsystemBase {
  private WPI_VictorSPX leftDriveMotor1;
  private WPI_VictorSPX leftDriveMotor2;
  private WPI_VictorSPX leftDriveMotor3;
  private WPI_VictorSPX rightDriveMotor1;
  private WPI_VictorSPX rightDriveMotor2;
  private WPI_VictorSPX rightDriveMotor3;

  private DifferentialDrive drivetrain;
  private MotorControllerGroup leftSide;
  private MotorControllerGroup rightSide;

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    leftDriveMotor1 = new WPI_VictorSPX(RobotMap.leftDriveMotor1_CAN);
    rightDriveMotor1 = new WPI_VictorSPX(RobotMap.rightDriveMotor1_CAN);
    rightDriveMotor2 = new WPI_VictorSPX(RobotMap.rightDriveMotor2_CAN); 
    leftDriveMotor2 = new WPI_VictorSPX(RobotMap.leftDriveMotor2_CAN); 
    rightDriveMotor3 = new WPI_VictorSPX(RobotMap.rightDriveMotor3_CAN); 
    leftDriveMotor3 = new WPI_VictorSPX(RobotMap.leftDriveMotor3_CAN); 

    


    leftSide = new MotorControllerGroup(leftDriveMotor1, leftDriveMotor2, leftDriveMotor3);
    rightSide = new MotorControllerGroup(rightDriveMotor1, rightDriveMotor2, rightDriveMotor3);

    drivetrain = new DifferentialDrive(leftSide, rightSide);
    
   
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void drive(double speed, double rotation){
    drivetrain.arcadeDrive(speed, rotation);

  }
}
