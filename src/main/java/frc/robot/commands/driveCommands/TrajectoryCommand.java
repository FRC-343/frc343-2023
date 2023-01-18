package frc.robot.commands.driveCommands;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

import frc.robot.subsystems.Drive;

public class TrajectoryCommand extends RamseteCommand {
    public static final double kRamseteB = 2.0;
    public static final double kRamseteZeta = 0.7;
    private static final Drive drive = Drive.getInstance();

    public TrajectoryCommand(Trajectory trajectory) {
        super(trajectory, drive::getPose, new RamseteController(kRamseteB, kRamseteZeta), drive.getLeftFeedforward(),
                drive.getKinematics(), drive::getWheelSpeeds, drive.getLeftPIDController(),
                drive.getRightPIDController(), drive::setVoltages, drive);
    }

}
