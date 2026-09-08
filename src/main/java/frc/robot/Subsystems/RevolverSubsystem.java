// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RevolverSubsystem extends SubsystemBase {
  public class Map {
    public static final int MOTOR_CAN = 8;

    // The SparkFlex's built-in encoder sits on the MOTOR shaft, before the
    // gearbox (unlike the old external Mag Encoder, which read the OUTPUT
    // shaft directly at 1:1). 75 motor rotations = 1 revolver-output rotation.
    // If there is any external gearing stacked on top of the internal 75:1,
    // fold that ratio in here too (e.g. 75.0 * externalStageRatio).
    public static final double GEARBOX_RATIO = 75.0;

    public static final double MOTOR_SPEED_COEFF = 0.5; // TODO: re-tune - was set for the old motor/gearing
    public static final double MOTOR_REVOLVE_TIME = 0.74; // TODO: re-time on the robot - motor + ratio changed

    // Closed-loop gains for MAXMotion position control, in units of revolver
    // OUTPUT-shaft rotations. The old Talon PIDF gains do NOT carry over -
    // different motor, different controller, different native units.
    // These are conservative placeholders; tune with the REV Hardware Client.
    public static final double MOTOR_kP = 1.0; // TODO: tune
    public static final double MOTOR_kI = 0.0; // TODO: tune
    public static final double MOTOR_kD = 0.0; // TODO: tune

    // MAXMotion is REV's equivalent of CTRE's Motion Magic. It's trapezoidal
    // only - there's no direct equivalent of the old S-curve strength knob.
    public static final double MOTOR_MAXMOTION_MAX_VELOCITY = 60.0; // RPM, at the OUTPUT shaft - TODO: tune
    public static final double MOTOR_MAXMOTION_MAX_ACCEL = 60.0; // RPM/s, at the OUTPUT shaft - TODO: tune
    public static final double MOTOR_MAXMOTION_ALLOWED_ERROR = 0.02; // output-shaft rotations

    public static final double POSITION_TOLERANCE_ROTATIONS = 0.02; // used by atTarget()

    // NEOs/NEO Vortex can pull far more stall current than the old motor.
    // Strongly recommended - uncomment and set to whatever your PDH breaker allows.
    // public static final int MOTOR_CURRENT_LIMIT_AMPS = 60;

    public static final int SOLENOID_CHANNEL = 1;
  }

  private SparkFlex motor;
  private RelativeEncoder encoder;
  private SparkClosedLoopController closedLoopController;
  private double positionTargetRotations = 0.0;

  private Solenoid fireSolenoid;
  private PneumaticsControlModule pcm;

  /** Creates a new RevolverSubsytem. */
  public RevolverSubsystem() {
    motor = new SparkFlex(Map.MOTOR_CAN, MotorType.kBrushless);
    motor.clearFaults();

    SparkFlexConfig config = new SparkFlexConfig();
    config
        .inverted(false) // TODO: flip if the revolver spins opposite to what positive commands expect
        .idleMode(IdleMode.kBrake);
        // .smartCurrentLimit(Map.MOTOR_CURRENT_LIMIT_AMPS); // see note above, recommended

    config.encoder
        // Scales the built-in encoder's motor-shaft rotations down to
        // revolver OUTPUT-shaft rotations, accounting for the 75:1 gearbox.
        .positionConversionFactor(1.0 / Map.GEARBOX_RATIO)
        .velocityConversionFactor(1.0 / Map.GEARBOX_RATIO);

    config.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(Map.MOTOR_kP, Map.MOTOR_kI, Map.MOTOR_kD)
        .outputRange(-1.0, 1.0);

    config.closedLoop.maxMotion
        .maxVelocity(Map.MOTOR_MAXMOTION_MAX_VELOCITY)
        .maxAcceleration(Map.MOTOR_MAXMOTION_MAX_ACCEL)
        .allowedClosedLoopError(Map.MOTOR_MAXMOTION_ALLOWED_ERROR);

    // configure() replaces the old configFactoryDefault() + per-parameter config___()
    // calls. kResetSafeParameters resets the controller to factory defaults first,
    // then applies everything set above; kPersistParameters keeps it through a brownout.
    motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    encoder = motor.getEncoder();
    closedLoopController = motor.getClosedLoopController();
    encoder.setPosition(0);

    pcm = new PneumaticsControlModule(30);
    fireSolenoid = pcm.makeSolenoid(Map.SOLENOID_CHANNEL);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // System.out.println(getRevolverRotation().getDegrees());
    // SmartDashboard.putNumber("Revolver Angle Pos", getRevolverRotation().getDegrees());
  }

  public Rotation2d getRevolverRotation() {
    // encoder.getPosition() is already in OUTPUT-shaft rotations thanks to the
    // positionConversionFactor above, so this is just a straight unit conversion now -
    // the old CTREConverter.MagEncoderToDegrees() helper is no longer needed.
    return Rotation2d.fromDegrees(encoder.getPosition() * 360.0);
  }

  public double getRevolverPosition() {
    // Output-shaft rotations (previously: raw Talon sensor ticks).
    return encoder.getPosition();
  }

  public void setRevolverSpeed(double speed) {
    motor.set(speed * Map.MOTOR_SPEED_COEFF);
  }

  public void setRevolverPositionTarget(double targetRotations) {
    // targetRotations is in OUTPUT-shaft rotations (e.g. 1.0 = one full revolver
    // turn) - NOT raw sensor ticks like the old Motion Magic target was.
    positionTargetRotations = targetRotations;
    closedLoopController.setReference(targetRotations, ControlType.kMAXMotionPositionControl);
    // Note: newer REVLib releases (2026+) renamed setReference() to setSetpoint().
    // setReference() is correct for the 2025-generation library.
  }

  public void setFireSolenoid(boolean open) {
    fireSolenoid.set(open);
  }

  private boolean atTarget() {
    return Math.abs(encoder.getPosition() - positionTargetRotations) < Map.POSITION_TOLERANCE_ROTATIONS;
  }

  //#region Commands

  public Command fireSequenceCommand(int power) {
    return fireCommand(true)
      .andThen(Commands.waitSeconds(0.01 * power))
      .andThen(fireCommand(false));
  }

  public Command fireCommand(boolean open) {
    return this.runOnce(() -> setFireSolenoid(open));
  }

  public Command revolveForward(){
    // return this.runOnce(() -> {
    //   encoder.setPosition(0);
    //   System.out.println("Position: " + encoder.getPosition());
    //   setRevolverPositionTarget(1.0); // one full revolver rotation - adjust to your indexing geometry
    //   System.out.println("Done");
    // });
    return this
      .runOnce(() -> motor.set(Map.MOTOR_SPEED_COEFF))
      .andThen(Commands.waitSeconds(Map.MOTOR_REVOLVE_TIME))
      .andThen(() -> motor.set(0));
  }

  public Command runRevolverWhileHeld(boolean forwards) {
    double percent = forwards ? Map.MOTOR_SPEED_COEFF : -Map.MOTOR_SPEED_COEFF;

    return this
      .runOnce(() -> motor.set(percent))
      .finallyDo(() -> motor.set(0));
  }

  public Command revolveBackward(){
    // return this.runOnce(() -> {
    //   encoder.setPosition(0);
    //   setRevolverPositionTarget(-1.0);
    // });
    return this
      .runOnce(() -> motor.set(-Map.MOTOR_SPEED_COEFF))
      .andThen(Commands.waitSeconds(Map.MOTOR_REVOLVE_TIME))
      .andThen(() -> motor.set(0));
  }


  // public Command revolveForward() {
  //   return Commands.runOnce(() -> encoder.setPosition(0), this)
  //     .andThen(
  //       Commands.run(
  //         () -> setRevolverPositionTarget(1.0),
  //         this
  //       ).until(this::atTarget)
  //       .withTimeout(3)
  //     );
  // }

  // public Command revolveBackward() {
  //   return Commands.runOnce(() -> encoder.setPosition(0), this)
  //     .andThen(
  //       Commands.run(
  //         () -> setRevolverPositionTarget(-1.0),
  //         this
  //       ).until(this::atTarget)
  //       .withTimeout(3)
  //     );
  // }

  //#end
}