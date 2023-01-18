package frc.robot.commands.driveCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drive;

public class DriveTurnCommand extends CommandBase {
    private final Drive m_drive;
    private final double m_rot;
    private final double m_speed;

    private Pose2d m_startPose = new Pose2d();

    public DriveTurnCommand(double rot, double speed) {

        m_drive = Drive.getInstance();
        addRequirements(m_drive);

        m_rot = rot;
        m_speed = speed;
    }

    @Override
    public void initialize() {
        m_startPose = m_drive.getPose();

        m_drive.drive(0, m_speed);
    }

    @Override
    public void execute() {
        m_drive.drive(0, m_speed); // this makes us turn ccw if positive
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.drive(0, 0);
    }

    @Override
    public boolean isFinished() {
        if (m_rot > 0.0) {
            return m_drive.getPose().minus(m_startPose).getRotation().getDegrees() >= m_rot; // works fine when rotating ccw, but rotates 180* when told 90 with negative speed
        } else if (m_rot < 0.0) {
            return m_drive.getPose().minus(m_startPose).getRotation().getDegrees() <= m_rot;
        } else { // m_rot == 0.0
            return true;
        }
    }

}