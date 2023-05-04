package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

import frc.robot.Robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;

public class grayson extends SubsystemBase {
    private static final grayson m_instance = new grayson();
    private final Arm m_arm = Arm.getInstance();

    private final DoubleSolenoid m_Grayson = new DoubleSolenoid( PneumaticsModuleType.REVPH , 8, 9);



    // private final CANSparkMax m = new CANSparkMax(0, MotorType.kBrushless);
    // private final RelativeEncoder m_ArmEncoder =
    // m_arm.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    // private static boolean runningArm = false;

    public grayson() {

        SendableRegistry.setSubsystem(m_Grayson, this.getClass().getSimpleName());
        SendableRegistry.setName(m_Grayson, "Grayson pnumatics");

    }

    public static grayson getInstance() {
        return m_instance;
    }

    public void disEngage() {
        if (m_arm.getBottomLimit()|| m_arm.Armpos()>= 16) {
            m_Grayson.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void engage() {
        m_Grayson.set(DoubleSolenoid.Value.kForward);
    }

    public void toBeOrNotToBe() {
        if (m_Grayson.get() == DoubleSolenoid.Value.kReverse) {
            engage();
        } else {
            disEngage();
        }
    }
 

}
