package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
    private static final Turret m_instance = new Turret();

    private final Spark m_turret = new Spark(5);
    private final Encoder m_turretEncoder = new Encoder(0, 1);
    private final DigitalInput m_isLeft = new DigitalInput(6); // has completely turned counter clockwise
    private final DigitalInput m_isRight = new DigitalInput(7); // has completely turned clockwise

    private double m_speed = 0.0;
    private boolean m_aiming = false; // aiming is just used for getting to a preset angle
    private boolean m_aimed = false; // same
    private boolean m_zeroing = false; // reseting to get good encoder values
    private double m_target = 0.0;

    public Turret() {

        m_turret.setInverted(true);
        m_turretEncoder.setReverseDirection(true);

        SendableRegistry.setSubsystem(m_turret, this.getClass().getSimpleName());
        SendableRegistry.setName(m_turret, "turret motor");

        SendableRegistry.setSubsystem(m_turretEncoder, this.getClass().getSimpleName());
        SendableRegistry.setName(m_turretEncoder, "Turret Encoder");

        SendableRegistry.setSubsystem(m_isLeft, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isLeft, "Turret left (ccw) limit switch");

        SendableRegistry.setSubsystem(m_isRight, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isRight, "Turret right (cw) limit switich");
    }

    public static Turret getInstance() {
        return m_instance;
    }

    public double getEncoder() {
        return m_turretEncoder.get();
    }

    public void stop() {
        m_turret.set(0.0);
    }

    public boolean isAimed() {
        return m_aimed;
    }

    public void spin(double speed) {
        m_aiming = false;
        m_aimed = false;
        m_speed = speed;
    }

    public void aim(double target, boolean startWithZeroing) {
        m_target = target;

        if (!m_aiming) {
            m_aiming = true;
            if (startWithZeroing) {
                m_zeroing = true;
            }
        }
    }

    public void aim(double target) { 
        aim(target, false);
    }

    @Override
    public void periodic() {
        if (m_aiming) { // trying to get to a certain value

            if (m_turretEncoder.getRate() > 600 || m_turretEncoder.getRate() < -600
                    || m_turretEncoder.getDistance() > 350
                    || m_turretEncoder.getDistance() < -50) {
                System.err.println("Turret encoder sent garbage values, zeroing again...");
                m_zeroing = true;
            }

            if (m_isLeft.get()) { // check if already zeroed (gone all the way left)
                m_zeroing = false;
                m_turretEncoder.reset();
            }

            if (m_zeroing) { // reseting for proper measurement
                m_turret.set(-.4);
            } else {
                if (m_isRight.get()) { // went all the way to the right, so try again
                    m_zeroing = true;
                    m_turret.set(0.0);
                } else if (m_turretEncoder.get() < m_target - 10) { // going towards target cw, previously was .getDistance()
                    m_turret.set(.4);
                } else if (m_turretEncoder.get() > m_target + 10) {
                    m_turret.set(-.4);
                } else {
                    m_turret.set(0.0);
                    m_aimed = true;
                }
            }

            SmartDashboard.putBoolean("turret reached preset", m_aimed);

        } else {
            if (m_isLeft.get() && m_speed < 0.0) {
                m_turret.set(0.0);
                m_turretEncoder.reset();
            } else if (m_isRight.get() && m_speed > 0.0) {
                m_turret.set(0.0);
            } else {
                m_turret.set(m_speed); // not touching limit switches or touching a limit switch but tring to go the
                                 // oppisite way
            }
        }

    }

}
