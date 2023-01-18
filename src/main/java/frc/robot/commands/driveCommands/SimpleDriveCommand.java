package frc.robot.commands.driveCommands;

import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SimpleDriveCommand extends CommandBase {
    private final Drive m_drive;

    private final double m_speed;
    private final double m_driveTime; //The amount of time you want to drive 
    private final double m_waitTime; //The amount of time you want to wait before driving forward

    private Timer t;

    public SimpleDriveCommand(double speed, double driveTime, double waitTime) {
        m_drive = Drive.getInstance();
        addRequirements(m_drive);

        m_speed = speed;
        m_driveTime = driveTime;
        m_waitTime = waitTime;
        t = new Timer();
    }

    @Override
    public void initialize() {
        t.reset();
        t.start();
    }

    @Override
    public void execute() {
        if (t.get() >= m_waitTime) {
            m_drive.drive(m_speed, 0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.drive(0, 0);
        t.reset();
    }

    @Override
    public boolean isFinished() {
        return t.get() >= (m_driveTime + m_waitTime); 
    }

}
