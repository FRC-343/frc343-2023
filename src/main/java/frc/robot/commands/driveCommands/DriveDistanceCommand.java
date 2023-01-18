package frc.robot.commands.driveCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drive;

public class DriveDistanceCommand extends CommandBase {
    private final Drive m_drive;
    private final double m_distance;
    private final double m_speed;

    private Pose2d m_startPose = new Pose2d(0, 0, new Rotation2d(0));

    public DriveDistanceCommand(double distance, double speed) {
        m_drive = Drive.getInstance();
        addRequirements(m_drive);

        m_distance = distance;
        m_speed = speed;
    }

    @Override
    public void initialize() {
        m_startPose = m_drive.getPose();

        m_drive.drive(m_speed, 0);
    }

    @Override
    public void execute() {
        m_drive.drive(m_speed, 0);
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.drive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return m_drive.getPose().minus(m_startPose).getTranslation().getNorm() >= m_distance;

    }

}
