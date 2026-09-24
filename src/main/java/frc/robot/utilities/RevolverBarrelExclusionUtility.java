package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import java.util.function.Supplier;

public class RevolverBarrelExclusionUtility {
    private final int maxBarrelIndex;
    private int currentBarrelIndex = 0;
    private int autoDirection = 1;

    public RevolverBarrelExclusionUtility(int numberOfUsableBarrels) {
        maxBarrelIndex = numberOfUsableBarrels - 1;
    }

    public boolean canRevolveForwards() {
        if (currentBarrelIndex + 1 <= maxBarrelIndex) {
            currentBarrelIndex++;
            return true;
        }
        return false;
    }

    public boolean canRevolveBackwards() {
        if (currentBarrelIndex - 1 >= 0) {
            currentBarrelIndex--;
            return true;
        }
        return false;
    }

    public Command autoRevolve(Supplier<Command> forwardCommand, Supplier<Command> backwardsCommand) {
        if (autoDirection == 1) {
            if (canRevolveForwards()) return forwardCommand.get(); // If possible, go forwards

            autoDirection = -1; // Swap direction if forward is blocked
            return backwardsCommand.get(); // Then, go backwards

        } else if (autoDirection == -1) {
            if (canRevolveBackwards()) return backwardsCommand.get(); // If possible, go backwards

            autoDirection = 1; // Swap direction if backwards is blocked
            return forwardCommand.get(); // Then, go forward

        }
        return Commands.none();
    }
}
