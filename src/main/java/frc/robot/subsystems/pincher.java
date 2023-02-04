package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;

public class pincher extends SubsystemBase {
    private static final pincher m_instance = new pincher();

    private final DoubleSolenoid m_Pincher = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 3, 2);

    // private final CANSparkMax m = new CANSparkMax(0, MotorType.kBrushless);
    // private final RelativeEncoder m_ArmEncoder =
    // m_arm.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    // private static boolean runningArm = false;

    public pincher() {

        SendableRegistry.setSubsystem(m_Pincher, this.getClass().getSimpleName());
        SendableRegistry.setName(m_Pincher, "Pincher pnumatics");

    }

    public static pincher getInstance() {
        return m_instance;
    }

    public void disEngage() {
        m_Pincher.set(DoubleSolenoid.Value.kReverse);
    }

    public void engage() {
        m_Pincher.set(DoubleSolenoid.Value.kForward);
    }

    public void toBeOrNotToBe() {
        if (m_Pincher.get() == DoubleSolenoid.Value.kReverse) {
            engage();
        } else {
            disEngage();
        }
    }

}
