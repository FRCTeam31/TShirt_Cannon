package frc.robot.Subsystems.Revolver;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Revolver.IRevolverIO.RevolverIOInputs;
import frc.robot.Subsystems.Revolver.IRevolverIO.RevolverIOOutputs;
import frc.robot.utilities.CTREConverter;

public class RevolverSubsystem extends SubsystemBase {
  public static class Map {
    public static final int MOTOR_CAN = 8;
    public static final double MOTOR_SPEED_COEFF = 0.5;

    public static final double MOTOR_kP = 1.4;
    public static final double MOTOR_kI = 0.0;
    public static final double MOTOR_kD = 0.0;
    public static final double MOTOR_kF = 0.0;

    public static final double MOTOR_MAGIC_ACCEL = 2000; // Sensor units per 100ms, per second
    public static final double MOTOR_MAGIC_CRUISE = 1500; // Sensor units per 100ms
    public static final int MOTOR_MAGIC_S_CURVE_STRENGTH = 2; // Range: 1-8

    public static final int SOLENOID_CHANNEL = 1;
  }

  private IRevolverIO revolverIo;
  private RevolverIOOutputs revolverOutputs = new RevolverIOOutputs();
  private RevolverIOInputs revolverInputs = new RevolverIOInputs();

  /** Creates a new RevolverSubsytem. 
 * @param isReal */
  public RevolverSubsystem(boolean isReal){
   if (isReal){
    revolverIo = new RevolverIOReal();
   } else {
    revolverIo = new RevolverIOSim();
   }
  }

  @Override
  public void periodic() {
    revolverIo.setOutputs(revolverOutputs);
    revolverInputs = revolverIo.getInputs();

    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Revolver Angle Pos", getRevolverRotation().getDegrees());
  }

  public Rotation2d getRevolverRotation() {
    double currentdeg = CTREConverter.MagEncoderToDegrees(getRevolverPosition(), 9 * 10);
    return Rotation2d.fromDegrees(currentdeg);
  }

  public double getRevolverPosition() {
    return revolverInputs.SensorPosition;
  }

  public void setRevolverSpeed(double speed) {
    revolverOutputs.RevolverControlMode = ControlMode.PercentOutput;
    revolverOutputs.MotorOutput = speed * Map.MOTOR_SPEED_COEFF;
  }

  public void setRevolverPositionTarget(double target) {
    revolverOutputs.RevolverControlMode = ControlMode.MotionMagic;
    revolverOutputs.MotorOutput = target;
  }

  public void setFireSolenoid(boolean open) {
    revolverOutputs.RevolverSolenoidOpen = open;
  }

  //#region Commands

  /**
   * Creates a fire sequence command which opens the solenoid for a given period of time
   * @param powerInMs The time in milliseconds to open the solenoid
   * @return
   */
  public Command fireSequenceCommand(int powerInMs) {
    return fireCommand(true)
      .andThen(Commands.waitSeconds(0.001 * powerInMs))
      .andThen(fireCommand(false));
  }

  public Command fireCommand(boolean open) {
    return this.runOnce(() -> setFireSolenoid(open));
  }

  public Command revolveForward(){
    return this.runOnce(()->{
      revolverIo.setSensorPosition(0);
      setRevolverPositionTarget(4096);
    });
  }

  public Command revolveBackward(){
    return this.runOnce(()->{
      revolverIo.setSensorPosition(0);
      setRevolverPositionTarget(-4096);
    });
  }

  public Command stopMotors() {
    return this.runOnce(() -> setRevolverSpeed(0));
  }

  //#end
}
