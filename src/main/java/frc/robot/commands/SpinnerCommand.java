package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;


import frc.robot.subsystems.Spinner;

public class SpinnerCommand extends CommandBase {

    private double m_speed;
    private final Spinner m_Spinner;

    public SpinnerCommand(double speed) {
        m_Spinner = Spinner.getInstance();
        m_speed = speed;
        addRequirements(m_Spinner);
    }
    

    @Override
    public void execute() {
        m_Spinner.setSpinner(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_Spinner.setSpinner(0.0);
    }

    @Override
    public boolean isFinished() {
        boolean value = false;
        
    //    if (m_speed == 0) {
    //          value = true;
    //      }

        return value;
    }
}

