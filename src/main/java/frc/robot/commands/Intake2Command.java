package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class Intake2Command extends CommandBase {
    private final Intake m_intake;
    private final Timer t;

    private final double time;
    private final double kIntakeSpeed;

    public Intake2Command(double intakeSpeed, double time) {
        m_intake = Intake.getInstance();
        addRequirements(m_intake);

        kIntakeSpeed = intakeSpeed;
        t = new Timer();
        this.time = time; // time to run the intake 
    }

    public Intake2Command(double intakeSpeed) {
        this(intakeSpeed, Math.sqrt(2) / 2); //defaults time to this
    }

    public Intake2Command() {
        this(0.8); // defaults to .8 speed
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        t.reset();
        t.start();
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
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (t.get() >= time) {
            return true;
        } else {
            return false;
        }

    }
}