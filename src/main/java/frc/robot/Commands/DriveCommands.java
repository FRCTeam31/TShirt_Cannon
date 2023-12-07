// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.DriveSubsystem;

public class DriveCommands{
  public static Command defaultDriveCommand(CommandXboxController controller, DriveSubsystem driveSubsystem){
    return Commands.run(() -> {
      double speed = -MathUtil.applyDeadband(controller.getRawAxis(0), 0.1);
      double rotation = MathUtil.applyDeadband(controller.getRawAxis(1), 0.1);
      driveSubsystem.drive(speed, rotation);
    }, driveSubsystem);
  }
}