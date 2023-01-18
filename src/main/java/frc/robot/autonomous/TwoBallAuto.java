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

public class TwoBallAuto extends SequentialCommandGroup {

    public TwoBallAuto() {
        Intake intake = Intake.getInstance();
        
        // commands in this autonomous
        addCommands(
                // drop intake
                new InstantCommand(intake::lower, intake),
                // drive and intake
                new ParallelDeadlineGroup(
                        new DriveDistanceCommand(1.1, 3),
                        new IntakeCommand(),
                        new PresetHoodCommand(1100, true),
                        new PresetTurretCommand(0, true)),
                // rotate
                new ParallelDeadlineGroup(
                        new DriveTurnCommand(90, 1),
                        // new AimShootCommandAuto(),
                        new IntakeCommand()),

                new WaitCommand(1),
                new ParallelDeadlineGroup(
                        new AimShootCommandAuto(),
                        new IntakeCommand())

        );
    }
}
