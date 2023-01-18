package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeCommand extends CommandBase {
    private final Intake m_intake;

    private double kIntakeSpeed;
    private boolean m_raise;

    public IntakeCommand(double intakeSpeed, boolean raise) {
        m_intake = Intake.getInstance();
        kIntakeSpeed = intakeSpeed;
        m_raise = raise;
        addRequirements(m_intake);
    }

    public IntakeCommand() {
        this(0.8, true); // defaults to .8 speed
    }

    public IntakeCommand(double intakeSpeed) {
        this(intakeSpeed, true);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_intake.lower();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_intake.setIntake(kIntakeSpeed);

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_intake.setIntake(0);
        if (m_raise) {
            m_intake.raise();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
