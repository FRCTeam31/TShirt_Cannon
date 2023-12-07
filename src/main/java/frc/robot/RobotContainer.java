// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Commands.DriveCommands;
import frc.robot.Commands.RevolverCommands;
import frc.robot.Commands.ShoulderCommands;
import frc.robot.Subsystems.DriveSubsystem;
import frc.robot.Subsystems.RevolverSubsystem;
import frc.robot.Subsystems.ShoulderSubsystem;

public class RobotContainer {

  public DriveSubsystem driveSubsystem;
  public ShoulderSubsystem shoulderSubsystem;
  public RevolverSubsystem revolverSubsystem;
  public WPI_VictorSPX leftMotor1, rightMotor1, leftMotor2, rightMotor2, leftMotor3, rightMotor3;
  public CommandXboxController controller;

  public RobotContainer() {
    driveSubsystem = new DriveSubsystem();
    shoulderSubsystem = new ShoulderSubsystem();
    revolverSubsystem = new RevolverSubsystem();
    configureBindings();
  }

  private void configureBindings() {
    controller = new CommandXboxController(0);
    driveSubsystem.setDefaultCommand(DriveCommands.defaultDriveCommand(controller, driveSubsystem));
    shoulderSubsystem.setDefaultCommand(ShoulderCommands.changeShoulderAngleCommand(controller, shoulderSubsystem));
    controller.rightBumper().onTrue(RevolverCommands.revolveForward(revolverSubsystem));
    controller.leftBumper().onTrue(RevolverCommands.revolveBackwards(revolverSubsystem));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
