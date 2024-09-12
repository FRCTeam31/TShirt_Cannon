package frc.robot.subsystems.Revolver;

import edu.wpi.first.wpilibj.simulation.PneumaticsBaseSim;
import edu.wpi.first.wpilibj.simulation.SolenoidSim;
import frc.robot.sim.MotorSim;

public class RevolverIOSim {
    public class Map{
        private final MotorSim revolverMotor = new MotorSim();
        private final SolenoidSim revolverSolenoid = new SolenoidSim(PneumaticsBaseSim.getForType(0, null), 0);
    }
}
