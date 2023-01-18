package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private static final Intake m_instance = new Intake();

    private final DoubleSolenoid m_intakeLift = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 1, 0);
    private final Spark m_intake = new Spark(6);
    private static boolean runningIntake = false;

    public Intake() {
        m_intake.setInverted(true);

        SendableRegistry.setSubsystem(m_intake, this.getClass().getSimpleName());
        SendableRegistry.setName(m_intake, "Intake Motor");

        SendableRegistry.setSubsystem(m_intakeLift, this.getClass().getSimpleName());
        SendableRegistry.setName(m_intakeLift, "Intake Lift");
    }

    public static Intake getInstance() {
        return m_instance;
    }

    public void raise() {
        m_intakeLift.set(DoubleSolenoid.Value.kReverse);
    }

    public void lower() {
        m_intakeLift.set(DoubleSolenoid.Value.kForward);
    }

    public void setIntake(double speed) {
        m_intake.set(speed);
        if (speed == 0) {
            runningIntake = false;
        } else {
            runningIntake = true;
        }
    } 

    public static boolean isRunning() {
        return runningIntake;
    }

}
