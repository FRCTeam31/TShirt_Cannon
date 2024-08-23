package frc.robot.sim;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class MotorSim implements MotorController, AutoCloseable {
    final static double _maxVoltage = 12;
    final static double _period = 0.020; // 20ms
    final double _maxVelocity;
    double _direction = 1;
    double _velocity = 0;
    double _distance = 0;
    List<MotorSim> _followers = new ArrayList<MotorSim>();

    public void close() {};

    public MotorSim() {
        _maxVelocity = 1;
    }

    public MotorSim(double maxVelocity) {
        _maxVelocity = maxVelocity;
    }

    public MotorSim(double maxVelocity, double startingDistance) {
        _maxVelocity = maxVelocity;
        _distance = startingDistance;
    }

    public void follow(MotorSim leader) {
        leader.addFollower(this);
    }

    public void addFollower(MotorSim follower) {
        _followers.add(follower);
    }

    /**
     * Common interface for setting the speed of a motor controller.
     *
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     */
    @Override
    public void set(double speed) {
        _velocity = speed * _maxVelocity * _direction;

        // also set any followers
        for(MotorSim follower : _followers) {
            follower.set(speed);
        }
    }

    /**
     * Sets the voltage output of the MotorController. Compensates for the current bus voltage to
     * ensure that the desired voltage is output even if the battery voltage is below 12V - highly
     * useful when the voltage outputs are "meaningful" (e.g. they come from a feedforward
     * calculation).
     *
     * <p>NOTE: This function *must* be called regularly in order for voltage compensation to work
     * properly - unlike the ordinary set function, it is not "set it and forget it."
     *
     * @param outputVolts The voltage to output.
     */
    @Override
    public void setVoltage(double outputVolts) {
        set(outputVolts / _maxVoltage);
    }

    /**
     * Common interface for getting the current set speed of a motor controller.
     *
     * @return The current set speed. Value is between -1.0 and 1.0.
     */
    @Override
    public double get() {
        return _velocity / _maxVelocity;
    }

    /**
     * Common interface for inverting direction of a motor controller.
     *
     * @param isInverted The state of inversion true is inverted.
     */
    @Override
    public void setInverted(boolean isInverted)  {
        _direction = isInverted ? -1 : 1;
    }

    /**
     * Common interface for returning if a motor controller is in the inverted state or not.
     *
     * @return isInverted The state of the inversion true is inverted.
     */
    @Override
    public boolean getInverted() {
        return _direction < 0;
    }

    /** Disable the motor controller. */
    @Override
    public void disable() {
        stopMotor();
    }

    /**
     * Stops motor movement. Motor can be moved again by calling set without having to re-enable the
     * motor.
     */
    @Override
    public void stopMotor() {
        _velocity = 0;
    }

    public void setDistance(double distance) {
        _distance = distance;
    }

    public double getDistance() {
        return _distance;
    }

    public double getVelocity() {
        return _velocity;
    }

    public void periodic() {
        _distance += _velocity * _period;
    }
}
