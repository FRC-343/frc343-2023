package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Arm;

public class ArmCommand extends CommandBase {

    private double m_speed;
    private final Arm m_arm;

    public ArmCommand(double speed) {
        m_arm = Arm.getInstance();
        m_speed = speed;
        addRequirements(m_arm);
    }

    @Override
    public void execute() {
        m_arm.setArm(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_arm.setArm(0.0);
    }

    @Override
    public boolean isFinished() {
        boolean value = false;
        if (m_speed > 0) { // positive speed is going down
            if (m_arm.getBottomLimit()) {
                value = true;
            }
        } else if (m_speed < 0) {
            if (m_arm.getTopLimit()) {
                value = true;
            }
        } else if (m_speed == 0) {
            value = true;
        }

        return value;
    }
}
