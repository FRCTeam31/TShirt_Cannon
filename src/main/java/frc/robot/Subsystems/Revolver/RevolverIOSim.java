package frc.robot.subsystems.Revolver;

import edu.wpi.first.wpilibj.simulation.PneumaticsBaseSim;
import edu.wpi.first.wpilibj.simulation.SolenoidSim;
import frc.robot.sim.MotorSim;

public class RevolverIOSim implements IRevolverIO {
    public class Map{
        private final MotorSim revolverMotor = new MotorSim();
        private final SolenoidSim revolverSolenoid = new SolenoidSim(PneumaticsBaseSim.getForType(0, null), 0);
    }

    @Override
    public RevolverIOInputs getInputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInputs'");
    }

    @Override
    public void setOutputs(RevolverIOOutputs outputs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOutputs'");
    }

    @Override
    public void setSensorPosition(double position) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSensorPosition'");
    }
}
