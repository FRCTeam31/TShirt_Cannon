package frc.robot.subsystems.Revolver;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.Solenoid;

public class RevolverIOReal implements IRevolverIO{

private TalonSRX revolverMotor;
private Solenoid fireSolenoid;
private PneumaticsControlModule pcm;
    
public RevolverIOReal() {
    revolverMotor = new TalonSRX(RevolverSubsystem.Map.MOTOR_CAN);
    revolverMotor.clearStickyFaults();
    revolverMotor.configFactoryDefault();
    revolverMotor.setNeutralMode(NeutralMode.Brake);

    // Configure Talon sensor
    revolverMotor.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 20);
    revolverMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 20);

    // Configure PIDF for Motion Magic
    revolverMotor.selectProfileSlot(0, 0);
    revolverMotor.config_kP(0, RevolverSubsystem.Map.MOTOR_kP);
    revolverMotor.config_kI(0, RevolverSubsystem.Map.MOTOR_kI);
    revolverMotor.config_kD(0, RevolverSubsystem.Map.MOTOR_kD);
    revolverMotor.config_kF(0, RevolverSubsystem.Map.MOTOR_kF);

    // Configure motion magic parameters
    revolverMotor.configMotionAcceleration(RevolverSubsystem.Map.MOTOR_MAGIC_ACCEL);
    revolverMotor.configMotionCruiseVelocity(RevolverSubsystem.Map.MOTOR_MAGIC_CRUISE);
    revolverMotor.configMotionSCurveStrength(RevolverSubsystem.Map.MOTOR_MAGIC_S_CURVE_STRENGTH);

    revolverMotor.setSelectedSensorPosition(0);

    pcm = new PneumaticsControlModule(30);
    fireSolenoid = pcm.makeSolenoid(RevolverSubsystem.Map.SOLENOID_CHANNEL);
  }

  public void setOutputs(RevolverIOOutputs outputs){
    fireSolenoid.set(outputs.RevolverSolenoidOpen);
    revolverMotor.set(outputs.RevolverControlMode, outputs.MotorOutput);
  }

  public RevolverIOInputs getInputs(){
    var inputs = new RevolverIOInputs();
    inputs.SensorPosition = revolverMotor.getSelectedSensorPosition();

    return inputs;
  }

  public void setSensorPosition(double position){
    revolverMotor.setSelectedSensorPosition(position);
  }
}
