package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.RevolverSubsystem;
import frc.robot.subsystems.ShoulderSubsystem;
import frc.robot.subsystems.Drive.DriveSubsystem;

public class RobotContainer {
  public static DriveSubsystem Drive;
  public static ShoulderSubsystem Shoulder;
  public static RevolverSubsystem Revolver;
  public static CommandXboxController controller;

  public static void init(boolean isReal) {
    Drive = new DriveSubsystem(isReal);
    Shoulder = new ShoulderSubsystem(isReal);
    Revolver = new RevolverSubsystem(isReal);

    // Creates command bindings to controller actions
    configureControllerBindings();
  }

  private static void configureControllerBindings() {
    controller = new CommandXboxController(0);
    
    Drive.setDefaultCommand(Drive.driveTankCommand(controller));
    Shoulder.setDefaultCommand(Shoulder.controlWithTriggersCommand(controller));

    
    controller.rightBumper().onTrue(Revolver.revolveForward());
    controller.leftBumper().onTrue(Revolver.revolveBackward());

    // Auto-fire, turret mode
    controller.leftBumper().and(controller.rightBumper()).whileTrue(
      Commands.repeatingSequence(
        Revolver.fireSequenceCommand(20)
        .andThen(Revolver.revolveForward()
        .andThen(Commands.waitSeconds(0.75))))
        .alongWith(Drive.rotateArcadeCommand(controller))
    ).onFalse(Revolver.stopMotors().andThen(Revolver.fireCommand(false)));

    // Only allow firing when start AND a POV direction are pressed at the same time
    controller.start().and(controller.pov(0)).onTrue(Revolver.fireSequenceCommand(100));
    controller.start().and(controller.pov(90)).onTrue(Revolver.fireSequenceCommand(50));
    controller.start().and(controller.pov(270)).onTrue(Revolver.fireSequenceCommand(50));
    controller.start().and(controller.pov(180)).onTrue(Revolver.fireSequenceCommand(20));
  }
}
