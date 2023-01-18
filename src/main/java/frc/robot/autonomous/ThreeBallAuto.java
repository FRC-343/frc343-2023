package frc.robot.autonomous;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.*;
import frc.robot.commands.ShootingRelatingCommands.*;
import frc.robot.commands.ShootingRelatingCommands.SpecificCommands.*;
import frc.robot.commands.driveCommands.*;
import frc.robot.subsystems.*;

public class ThreeBallAuto extends SequentialCommandGroup {
    private static final double kDriveSpeed = 1;

    public ThreeBallAuto() {
            Intake intake = Intake.getInstance();
        // commands in this autonomous
        addCommands(
                // drop intake
                new InstantCommand(intake::lower, intake),
                // drive and intake
                new ParallelDeadlineGroup(
                        new DriveDistanceCommand(1.1, kDriveSpeed),
                        new IntakeCommand(),
                        new PresetHoodCommand(1000, true),
                        new PresetTurretCommand(65, true)),
                // rotate
                new ParallelDeadlineGroup(
                        new DriveTurnCommand(115, kDriveSpeed),
                        new IntakeCommand()),
                // rotate turret while doing all the stuff above
                // aim and shoot
                new AimShootCommandAuto(),

                // new DriveDistanceCommand(0.7, 2),

                new ParallelDeadlineGroup(
                        new DriveDistanceCommand(2.0 /*1.3*/, 1.5),
                        new IntakeCommand()),

                new ParallelDeadlineGroup(
                        new WaitCommand(2),
                        new DriveTurnCommand(15, kDriveSpeed),
                        new IntakeCommand(),
                        new AimCommand()),

                new AimShootCommandAuto());

    }
}
