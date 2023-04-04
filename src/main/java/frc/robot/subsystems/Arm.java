package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;


public class Arm extends SubsystemBase{
   private static final Arm m_instance = new Arm();

   private final DoubleSolenoid m_mast = new DoubleSolenoid(PneumaticsModuleType.REVPH, 4, 5);


   private final DigitalInput m_isTop = new DigitalInput(1);
   private final DigitalInput m_isBottom = new DigitalInput(0);

    private final CANSparkMax m_arm = new CANSparkMax(1, MotorType.kBrushless);
    private final RelativeEncoder m_ArmEncoder = m_arm.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    private static boolean runningArm = false;

    public Arm() {
        SendableRegistry.setSubsystem(m_mast, this.getClass().getSimpleName());
        SendableRegistry.setName(m_mast, "Mast pnumatics");

        SendableRegistry.setSubsystem(m_isTop, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isTop, "isTop");

        SendableRegistry.setSubsystem(m_isBottom, this.getClass().getSimpleName());
        SendableRegistry.setName(m_isBottom, "isbottom");
        SmartDashboard.putData("top limit", m_isTop);
        SmartDashboard.putData("bottom limit", m_isBottom);
        SmartDashboard.putNumber("Encoder test", m_ArmEncoder.getPositionConversionFactor());

    }

    public static Arm getInstance() {
        return m_instance;
    }

    public void disEngage() {
        m_mast.set(DoubleSolenoid.Value.kReverse);
    }

    public void engage() {
        m_mast.set(DoubleSolenoid.Value.kForward);
    }
    public void MastMovment() {
        if (m_mast.get() == DoubleSolenoid.Value.kReverse) {
            engage();
        } else {
            disEngage();
        }
    }


    public boolean getTopLimit() {
        return m_isTop.get();
    }

    public boolean getBottomLimit() {
        return m_isBottom.get();
    }
    public void moveArm( double speed){
        speed = 1;


    }

    public void setArm(double speed) {

         if (speed < 0.0 && getBottomLimit()) {
             m_arm.set(0.0);
             m_ArmEncoder.setPosition(0);
         } else if (speed > 0 && (m_isTop.get())) {
             m_arm.set(0.0);
         } else {
           m_arm.set(speed);
         }
    }



    }






