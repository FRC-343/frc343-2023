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

public class CCW5ball2022 extends SequentialCommandGroup {

    public CCW5ball2022() {
            Intake intake = Intake.getInstance();

        // commands in this autonomous
        addCommands(
                // drop intake
                new InstantCommand(intake::lower, intake),
                // drive and intake
                new ParallelDeadlineGroup(
                        new DriveDistanceCommand(1.1, 3),
                        new IntakeCommand(),
                        new PresetHoodCommand(1000, true),
                        new PresetTurretCommand(0, true)),
                // rotate
                new ParallelDeadlineGroup(
                        new DriveTurnCommand(68, 2), //was 56
                        new IntakeCommand(),
                        new PresetTurretCommand(10, true)),

                // aim
                new ParallelDeadlineGroup(
                        new AimShootCommandAuto() ,
                        new IntakeCommand()),
                new DriveDistanceCommand(4.0, 3),
                new ParallelDeadlineGroup(
                        new DriveDistanceCommand(1.6, 1),
                        new IntakeCommand()),
                new ParallelDeadlineGroup(
                        new WaitCommand(1.0),
                        new IntakeCommand()),

                new ParallelDeadlineGroup(
                        new DriveTurnCommand(100, 2),
                        new PresetTurretCommand(60, true),
                        new IntakeCommand()),
                new PresetTurretCommand(80, true),
                new ParallelDeadlineGroup(
                        new DriveDistanceCommand(3.5, 3),
                        new IntakeCommand(),
                        new AimCommand()),

                new ParallelDeadlineGroup(
                        new AimShootCommandAuto(),
                        new IntakeCommand())

        );
    }
}
