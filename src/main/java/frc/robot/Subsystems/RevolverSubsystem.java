// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.Solenoid;
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

    // MAXMotion is REV's equivalent of CTRE's Motion Magic (trapezoidal only -
    // no direct equivalent of the old S-curve strength knob). As of the 2026
    // REVLib release these MAXMotion parameters were renamed to be more
    // descriptive: kMaxVelocity -> kCruiseVelocity, kAllowedClosedLoopError ->
    // kAllowedProfileError.
    public static final double MOTOR_MAXMOTION_CRUISE_VELOCITY = 60.0; // RPM, at the OUTPUT shaft - TODO: tune
    public static final double MOTOR_MAXMOTION_MAX_ACCEL = 60.0; // RPM/s, at the OUTPUT shaft - TODO: tune
    public static final double MOTOR_MAXMOTION_ALLOWED_PROFILE_ERROR = 0.02; // output-shaft rotations

    // NEOs/NEO Vortex can pull far more stall current than the old motor.
    // Strongly recommended - uncomment and set to whatever your PDH breaker allows.
    // public static final int MOTOR_CURRENT_LIMIT_AMPS = 60;

    public static final int SOLENOID_CHANNEL = 1;
  }

  private SparkFlex motor;
  private RelativeEncoder encoder;
  private SparkClosedLoopController closedLoopController;

  private Solenoid fireSolenoid;
  private PneumaticsControlModule pcm;

  /** Creates a new RevolverSubsytem. */
  public RevolverSubsystem() {
    motor = new SparkFlex(Map.MOTOR_CAN, MotorType.kBrushless);
    // As of 2026, REVLib no longer auto-clears faults when the object is created,
    // so this call is now the only thing clearing them - keep it.
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
        .feedbackSensor(com.revrobotics.spark.FeedbackSensor.kPrimaryEncoder)
        .pid(Map.MOTOR_kP, Map.MOTOR_kI, Map.MOTOR_kD)
        .outputRange(-1.0, 1.0);

    config.closedLoop.maxMotion
        .cruiseVelocity(Map.MOTOR_MAXMOTION_CRUISE_VELOCITY)
        .maxAcceleration(Map.MOTOR_MAXMOTION_MAX_ACCEL)
        .allowedProfileError(Map.MOTOR_MAXMOTION_ALLOWED_PROFILE_ERROR);

    // configureAsync() is the current (2026) recommended way to apply config - it
    // doesn't block the calling thread waiting for a CAN response the way the
    // now-deprecated configure() does. kResetSafeParameters resets the controller
    // to factory defaults first, then applies everything set above;
    // kPersistParameters keeps it through a brownout. ResetMode/PersistMode are now
    // shared enums in com.revrobotics rather than nested under SparkBase.
    motor.configureAsync(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

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
    // As of 2026, RelativeEncoder getters return a Signal<Double> instead of a
    // plain double, so status can be checked with isValid()/getTimestamp(). We
    // just pull the value out with a safe default here.
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
    // setReference() was deprecated in favor of setSetpoint() in the 2026 release.
    closedLoopController.setSetpoint(targetRotations, com.revrobotics.spark.SparkBase.ControlType.kMAXMotionPositionControl);
  }

  public void setFireSolenoid(boolean open) {
    fireSolenoid.set(open);
  }

  private boolean atTarget() {
    // 2026 REVLib added a built-in closed-loop status signal for this, so we no
    // longer need to manually track the last commanded target and compare it
    // against the encoder position ourselves.
    return closedLoopController.isAtSetpoint();
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
    //   System.out.println("Position: " + getRevolverPosition());
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