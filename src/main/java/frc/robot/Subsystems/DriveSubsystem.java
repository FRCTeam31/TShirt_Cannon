// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class DriveSubsystem extends SubsystemBase {
  public class Map {
    // Can Ids
    public static final int leftLeadingMotor_CAN = 4;
    public static final int leftFollowingMotor1_CAN = 1;
    public static final int leftFollowingMotor2_CAN = 2;
    public static final int rightLeadingMotor_CAN = 3;
    public static final int rightFollowingMotor1_CAN = 12;
    public static final int rightFollowingMotor2_CAN = 13;
  }

  //comment
  private WPI_VictorSPX leadingLeftDriveMotor;
  private WPI_VictorSPX followingLeftDriveMotor1;
  private WPI_VictorSPX followingLeftDriveMotor2;
  private WPI_VictorSPX leadingRightDriveMotor;
  private WPI_VictorSPX followingRightDriveMotor1;
  private WPI_VictorSPX followingRightDriveMotor2;

  private DifferentialDrive drivetrain;

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    leadingLeftDriveMotor = new WPI_VictorSPX(Map.leftLeadingMotor_CAN);
    followingLeftDriveMotor1 = new WPI_VictorSPX(Map.leftFollowingMotor1_CAN);
    followingLeftDriveMotor2 = new WPI_VictorSPX(Map.leftFollowingMotor2_CAN);
    followingLeftDriveMotor1.follow(leadingLeftDriveMotor);
    followingLeftDriveMotor2.follow(leadingLeftDriveMotor);

    leadingRightDriveMotor = new WPI_VictorSPX(Map.rightLeadingMotor_CAN);
    followingRightDriveMotor1 = new WPI_VictorSPX(Map.rightFollowingMotor1_CAN);
    followingRightDriveMotor2 = new WPI_VictorSPX(Map.rightFollowingMotor2_CAN);
    followingRightDriveMotor1.follow(leadingRightDriveMotor);
    followingRightDriveMotor2.follow(leadingRightDriveMotor);

    // Uses the leading motors to control the whole drivetrain
    drivetrain = new DifferentialDrive(leadingRightDriveMotor, leadingLeftDriveMotor); /* reversed purposefully */ 
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

  public Command driveArcadeCommand(CommandXboxController controller){
    return this.run(() -> {
      double speed = -MathUtil.applyDeadband(controller.getRawAxis(0), 0.1);
      double rotation = MathUtil.applyDeadband(controller.getRawAxis(1), 0.1);
      driveArcade(speed, rotation);
    });
  }

  public Command driveTankCommand(CommandXboxController controller){
    return this.run(() -> {
      double speedLeft = MathUtil.applyDeadband(controller.getRawAxis(1), 0.1);
      double speedRight = MathUtil.applyDeadband(-controller.getRawAxis(5), 0.1);
      driveTank(speedLeft, speedRight);
    });
  }

  //#endregion
}
