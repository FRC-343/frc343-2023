package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climbing extends SubsystemBase {
    private final static Climbing m_instance = new Climbing();

    private final DoubleSolenoid m_climber = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 3, 2);

    private final CANSparkMax m_climbingMotor = new CANSparkMax(3, MotorType.kBrushless);

    private final DigitalInput m_isLeftTop = new DigitalInput(17);
    private final DigitalInput m_isLeftBottom = new DigitalInput(18);
    private final DigitalInput m_isRightBottom = new DigitalInput(15);

    public Climbing() {
        SendableRegistry.setSubsystem(m_climber, this.getClass().getSimpleName());
        SendableRegistry.setName(m_climber, "climbing pnumatics");

        SendableRegistry.setSubsystem(m_isLeftBottom, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isLeftBottom, "isleftbottom");
        SendableRegistry.setSubsystem(m_isLeftTop, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isLeftTop, "isleftTop");

        SendableRegistry.setSubsystem(m_isRightBottom, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isRightBottom, "isrightbottom");

    }

    public static Climbing getInstance() {
        return m_instance;
    }

    public void disEngage() {
        m_climber.set(DoubleSolenoid.Value.kReverse);
    }

    public void engage() {
        m_climber.set(DoubleSolenoid.Value.kForward);
    }

    public void toBeOrNotToBe() {
        if (m_climber.get() == DoubleSolenoid.Value.kReverse) {
            engage();
        } else {
            disEngage();
        }
    }

    public boolean getLeftTopLimit() {
        return false; //m_isLeftTop.get();
    }

    public boolean getLeftBottomLimit() {
        return m_isLeftBottom.get();
    }

    public boolean getRightBottomLimit() {
        return m_isRightBottom.get();
    }

    public void setWinch(double speed) {
        m_climbingMotor.setInverted(true);
        if (speed < 0.0 && getLeftTopLimit()) {
            m_climbingMotor.set(0.0);
        } else if (speed > 0 && (m_isRightBottom.get() || m_isLeftBottom.get())) {
            m_climbingMotor.set(0.0);
        } else {
            m_climbingMotor.set(speed);
        }
    }

}