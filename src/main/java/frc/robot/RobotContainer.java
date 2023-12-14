// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.DriveSubsystem;
import frc.robot.Subsystems.RevolverSubsystem;
import frc.robot.Subsystems.ShoulderSubsystem;

public class RobotContainer {
  public DriveSubsystem Drive;
  public ShoulderSubsystem Shoulder;
  public RevolverSubsystem Revolver;
  public CommandXboxController controller;

  public RobotContainer() {
    Drive = new DriveSubsystem();
    Shoulder = new ShoulderSubsystem();
    Revolver = new RevolverSubsystem();
    configureBindings();
  }

  private void configureBindings() {
    controller = new CommandXboxController(0);
    Drive.setDefaultCommand(Drive.driveTankCommand(controller));
    Shoulder.setDefaultCommand(Shoulder.controlWithTriggersCommand(controller));
    controller.rightBumper().onTrue(Revolver.revolveForward());
    controller.leftBumper().onTrue(Revolver.revolveBackward());
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
