package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climbing;

public class ClimbArmCommand extends CommandBase {

    private final Climbing m_climbing;

    private double m_speed;

    public ClimbArmCommand(double speed) {
        m_climbing = Climbing.getInstance();
        m_speed = speed;
        addRequirements(m_climbing);
    }

    @Override
    public void execute() {
        m_climbing.setWinch(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_climbing.setWinch(0.0);
    }

    @Override
    public boolean isFinished() {
        boolean value = false;
        if (m_speed > 0) { //positive speed is going down
            if (m_climbing.getLeftBottomLimit() || m_climbing.getRightBottomLimit()) {
                value = true;
            }
        } else if (m_speed < 0) {
            if (m_climbing.getLeftTopLimit() ) {
                value = true;
            }
        } else if (m_speed == 0) {
            value = true;
        }

        return value;
    }

}
