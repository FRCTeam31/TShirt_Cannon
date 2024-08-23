package frc.robot.subsystems.Drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.interfaces.IDriveIO;
import frc.robot.interfaces.IDriveIO.DriveIOOutputs;

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

  public enum DriveMode {
    kArcade, 
    kTank
  }

  private IDriveIO driveIo;
  private DriveIOOutputs driveOutputs;

  /** Creates a new DriveSubsystem. 
   * @param isReal */
  public DriveSubsystem(boolean isReal) {
    if (isReal) {
      driveIo = new DriveIOReal();
    } else {
      driveIo = new DriveIOSim();
    }
  }

  @Override
  public void periodic() {
    // Each loop, update the IO
    driveIo.setOutputs(driveOutputs);

    // Each loop, get any inputs
    // var driveInputs = driveIo.getInputs();
  }

  public void driveArcade(double speed, double rotation){
    driveOutputs.driveMode = DriveMode.kArcade;
    driveOutputs.speed = speed;
    driveOutputs.rotation = rotation;
  }

  public void driveTank(double speedLeft, double speedRight){
    driveOutputs.driveMode = DriveMode.kTank;
    driveOutputs.leftSpeed = speedLeft;
    driveOutputs.rightSpeed = speedRight;
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
