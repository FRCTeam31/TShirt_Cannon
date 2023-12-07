// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Subsystems.RevolverSubsystem;

/** Add your docs here. */
public class RevolverCommands {
    public static Command spinRevolver(XboxController controller, RevolverSubsystem revolverSubsystem){
        return Commands.run(()->{
            var leftBumperPressed = controller.getLeftBumper();
            var rightBumperPressed = controller.getRightBumper();
            revolverSubsystem.spinRevolver(leftBumperPressed, rightBumperPressed);
        }, revolverSubsystem);
    }

    public static Command revolveForward(RevolverSubsystem revolverSubsystem){
        return Commands.runOnce(()->{
            var startPosition = revolverSubsystem.getRevolverPosition();
            var degreeThreshold = 15;
            var upperThreshold = startPosition.getDegrees() + degreeThreshold;
            var lowerThreshold = startPosition.getDegrees() - degreeThreshold;
            revolverSubsystem.spinRevolver(false, true);

            SmartDashboard.putNumber("Start Revolver Position Degrees", startPosition.getDegrees());
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){

            }
            Rotation2d currentRotation2d = revolverSubsystem.getRevolverPosition();
            while(currentRotation2d.getDegrees() >= upperThreshold || currentRotation2d.getDegrees() <= lowerThreshold){
                SmartDashboard.putNumber("Current Revolver Degrees", currentRotation2d.getDegrees());
                try{
                    Thread.sleep(1);
                }catch (InterruptedException ex){

                }
                 currentRotation2d = revolverSubsystem.getRevolverPosition();

            }
            revolverSubsystem.spinRevolver(false, false);
        });
    }

    public static Command revolveBackwards(RevolverSubsystem revolverSubsystem){
        return Commands.runOnce(()->{
            var startPosition = revolverSubsystem.getRevolverPosition();
            var degreeThreshold = 1;
            var upperThreshold = startPosition.getDegrees() + degreeThreshold;
            var lowerThreshold = startPosition.getDegrees() - degreeThreshold;
            revolverSubsystem.spinRevolver(true, false);
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){

            }
            Rotation2d currentRotation2d = revolverSubsystem.getRevolverPosition();
            while(currentRotation2d.getDegrees() >= upperThreshold || currentRotation2d.getDegrees() <= lowerThreshold){
                
                try{
                    Thread.sleep(1);
                }catch (InterruptedException ex){

                }
                currentRotation2d = revolverSubsystem.getRevolverPosition();
            }
            revolverSubsystem.spinRevolver(false, false);
        });
    }
}
