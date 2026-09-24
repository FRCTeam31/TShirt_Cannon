package frc.robot.utilities;

import com.revrobotics.spark.SparkFlex;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.function.Supplier;

public class RevolverCrashDetectionUtility {
    private final double maxCurrent;
    private final SparkFlex motor;
    private final Trigger crashTrigger = new Trigger(this::hasCrashed);

    public RevolverCrashDetectionUtility(double maxCurrent, SparkFlex motor, Command crashCommand) {
        this.maxCurrent = maxCurrent;
        this.motor = motor;
        crashTrigger.onTrue(crashCommand);
    }

    public boolean hasCrashed() {
        return motor.getOutputCurrent() > maxCurrent;
    }

    // Still WIP, ran out of time but this is pretty much it
}
