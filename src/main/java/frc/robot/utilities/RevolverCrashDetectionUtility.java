package frc.robot.utilities;

import com.revrobotics.spark.SparkFlex;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RevolverCrashDetectionUtility {
    private final double maxStationaryCurrent;
    private final SparkFlex motor;
    private final Trigger crashTrigger = new Trigger(this::hasCrashed);

    private static final double maxRPMToConsiderCrash = 10;

    public RevolverCrashDetectionUtility(double maxStationaryCurrent, SparkFlex motor) {
        this.maxStationaryCurrent = maxStationaryCurrent;
        this.motor = motor;
    }

    public Trigger getCrashTrigger() {
        return crashTrigger;
    }

    public boolean hasCrashed() {
        return  Math.abs(motor.getOutputCurrent()) > maxStationaryCurrent &&
                Math.abs(motor.getEncoder().getVelocity()) < maxRPMToConsiderCrash;
    }
}
