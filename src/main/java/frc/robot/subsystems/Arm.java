package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;


public class Arm extends SubsystemBase{
    private final CANSparkMax m_arm = new CANSparkMax(0, MotorType.kBrushless);
    private final RelativeEncoder m_ArmEncoder = m_arm.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    public Arm() {
        // SendableRegistry.setSubsystem(m_arm, this.getSubsystem().getSimpleName());
        // SendableRegistry.setName(m_arm, "Arm motor");




    }
}
