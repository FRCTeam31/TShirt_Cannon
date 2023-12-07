// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.RobotMap;
import frc.robot.Subsystems.ShoulderSubsystem;

/** Add your docs here. */
public class ShoulderCommands {
    public static Command changeShoulderAngleCommand(CommandXboxController controller, ShoulderSubsystem shoulderSubsystem){
        return Commands.run(()->{
            double angleSpeed = MathUtil.applyDeadband((controller.getRawAxis(2) - 
            controller.getRawAxis(3)) * RobotMap.shoulderAngleSpeedCoefficient, 0.1);
            shoulderSubsystem.changeShoulderAngle(angleSpeed);
        }, shoulderSubsystem);
    }
}
