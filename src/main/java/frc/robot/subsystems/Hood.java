package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class Hood extends SubsystemBase {
    private static final Hood m_instance = new Hood();

    private final Encoder m_hoodEncoder = new Encoder(4, 5);
    private final DigitalInput m_hoodBack = new DigitalInput(2); // 2 = bottom = back
    private final DigitalInput m_hoodFront = new DigitalInput(3);
    private final Spark m_hoodMotor = new Spark(7);

    private boolean m_aimed = false; // if shooter is currently aimed
    private double m_target = 0.0; // where it needs to be aiming
    private double m_speed = 0.0; // manual control
    private boolean m_aiming = false; // if currently aiming (for automatic)
    private boolean m_zeroing = false; // resetting hood

    public Hood() {
        SendableRegistry.setSubsystem(m_hoodEncoder, this.getClass().getSimpleName());
        SendableRegistry.setName(m_hoodEncoder, "Hood Encoder");
        SendableRegistry.setSubsystem(m_hoodBack, this.getClass().getSimpleName());
        SendableRegistry.setName(m_hoodBack, "Hood Back Limit");
        SendableRegistry.setSubsystem(m_hoodFront, this.getClass().getSimpleName());
        SendableRegistry.setName(m_hoodFront, "Hood Front Limit");
        SendableRegistry.setSubsystem(m_hoodMotor, this.getClass().getSimpleName());
        SendableRegistry.setName(m_hoodMotor, "Hood Motor");

    }

    public static Hood getInstance() {
        return m_instance;
    }

    public void aim(double target, boolean startWithZeroing) {
        m_target = target;
        SmartDashboard.putNumber("hood_target", m_target);

        if (!m_aiming) {
            m_aiming = true;
            if (startWithZeroing) {
                m_zeroing = true;
            } else {
                m_zeroing = false;
            }
        }
    }

    public void aim(double target) {
        aim(target, false);
    }

    public void stop() {
        m_hoodMotor.set(0.0);
    }

    public boolean isAimed() {
        return m_aimed;
    }

    public void move(double speed) {
        m_aiming = false;
        m_aimed = false;
        m_speed = speed;
    }

    @Override
    public void periodic() {
        if (m_aiming) {

            if (m_hoodEncoder.getDistance() > 3300 || m_hoodEncoder.getDistance() < -200) {
                System.err.println("Hood encoder sent garbage values, zeroing again...");
                m_zeroing = true;
                m_aimed = false;
            }

            if (m_hoodBack.get()) {
                m_zeroing = false;
                m_hoodEncoder.reset();
            }

            if (m_zeroing) {
                m_hoodMotor.set(Robot.kMaxHoodSpeed);
                m_aimed = false;
            } else {
                double speed = .8;
                if (Math.abs(m_hoodEncoder.getDistance() - m_target) < 600) {
                    speed = .4;
                }

                if (m_hoodFront.get()) {
                    m_zeroing = true;
                    m_hoodMotor.set(0.0);
                } else if (m_hoodEncoder.getDistance() < m_target - 150) {
                    m_hoodMotor.set(-speed);
                    m_aimed = false;
                } else if (m_hoodEncoder.getDistance() > m_target + 150) {
                    m_hoodMotor.set(speed);
                    m_aimed = false;
                } else { // m_hoodEncoder.getDistance >m_target-50 && < m_target+50
                    m_hoodMotor.set(0.0);
                    m_aimed = true;
                }
            }
            SmartDashboard.putBoolean("hood aimed", m_aimed);

        } else {
            if (m_hoodBack.get() && m_speed > 0.0) {
                m_hoodMotor.set(0.0);
                m_hoodEncoder.reset();
            } else if (m_hoodFront.get() && m_speed < 0.0) {
                m_hoodMotor.set(0.0);
            } else {
                m_hoodMotor.set(m_speed);
            }

        }
    }
}
