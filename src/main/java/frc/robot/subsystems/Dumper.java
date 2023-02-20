package frc.robot.subsystems;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Dumper extends SubsystemBase {
    private static final Dumper m_instance = new Dumper();

    private final Solenoid m_test = new Solenoid(PneumaticsModuleType.REVPH, 6);
   
  

    // private final CANSparkMax m = new CANSparkMax(0, MotorType.kBrushless);
    // private final RelativeEncoder m_ArmEncoder =
    // m_arm.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    // private static boolean runningArm = false;

    public Dumper() {

        SendableRegistry.setSubsystem(m_test, this.getClass().getSimpleName());
        SendableRegistry.setName(m_test, "Dumper pnumatics");

    }

    public static Dumper getInstance() {
        return m_instance;
    }

    public void disEngage() {
        m_test.set(false);
    }

    public void engage() {
        m_test.set(true);
    }

    public void dump() {
        if (m_test.get() == false) {
            engage();
        } else {
            disEngage();
        }
    }

}
