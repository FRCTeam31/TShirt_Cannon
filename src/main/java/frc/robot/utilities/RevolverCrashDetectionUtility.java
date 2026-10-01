package frc.robot.utilities;

import com.revrobotics.spark.SparkFlex;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.function.Supplier;

public class RevolverCrashDetectionUtility {
    private final double maxCurrent;
    private final SparkFlex motor;
    private final Trigger crashTrigger = new Trigger(this::hasCrashed);

    private static final double maxRPMToConsiderCrash = 10;

    public RevolverCrashDetectionUtility(double maxCurrent, SparkFlex motor) {
        this.maxCurrent = maxCurrent;
        this.motor = motor;
    }

    public Trigger getCrashTrigger() {
        return crashTrigger;
    }

    public boolean hasCrashed() {
        return  Math.abs(motor.getOutputCurrent()) > maxCurrent &&
                Math.abs(motor.getEncoder().getVelocity()) < maxRPMToConsiderCrash;
    }
}
