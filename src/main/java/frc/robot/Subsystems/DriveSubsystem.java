// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class DriveSubsystem extends SubsystemBase {
  public class Map {
    // Can Ids
    public static final int leftMotor1_CAN = 4;
    public static final int leftMotor2_CAN = 1;
    public static final int leftMotor3_CAN = 2;
    public static final int rightMotor1_CAN = 3;
    public static final int rightMotor2_CAN = 12;
    public static final int rightMotor3_CAN = 13;
  }

  //comment
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
    leftDriveMotor1 = new WPI_VictorSPX(Map.leftMotor1_CAN);
    rightDriveMotor1 = new WPI_VictorSPX(Map.rightMotor1_CAN);
    rightDriveMotor2 = new WPI_VictorSPX(Map.rightMotor2_CAN); 
    leftDriveMotor2 = new WPI_VictorSPX(Map.leftMotor2_CAN); 
    rightDriveMotor3 = new WPI_VictorSPX(Map.rightMotor3_CAN); 
    leftDriveMotor3 = new WPI_VictorSPX(Map.leftMotor3_CAN); 

    


    leftSide = new MotorControllerGroup(leftDriveMotor1, leftDriveMotor2, leftDriveMotor3);
    rightSide = new MotorControllerGroup(rightDriveMotor1, rightDriveMotor2, rightDriveMotor3);

    drivetrain = new DifferentialDrive(rightSide, leftSide); /* reversed purposefully */ 
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void driveArcade(double speed, double rotation){
    drivetrain.arcadeDrive(speed, rotation);
  }

  public void driveTank(double speedLeft, double speedRight){
    drivetrain.tankDrive(speedLeft, speedRight);
  }

  //#region Commands

  public CommandBase driveArcadeCommand(CommandXboxController controller){
    return this.run(() -> {
      double speed = -MathUtil.applyDeadband(controller.getRawAxis(0), 0.1);
      double rotation = MathUtil.applyDeadband(controller.getRawAxis(1), 0.1);
      driveArcade(speed, rotation);
    });
  }

  public CommandBase driveTankCommand(CommandXboxController controller){
    return this.run(() -> {
      double speedLeft = MathUtil.applyDeadband(controller.getRawAxis(1), 0.1);
      double speedRight = MathUtil.applyDeadband(-controller.getRawAxis(5), 0.1);
      driveTank(speedLeft, speedRight);
    });
  }

  //#endregion
}
