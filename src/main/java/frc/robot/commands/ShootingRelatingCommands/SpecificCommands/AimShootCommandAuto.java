package frc.robot.commands.ShootingRelatingCommands.SpecificCommands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ShootingRelatingCommands.AimCommand;
import frc.robot.commands.ShootingRelatingCommands.ShootCommand;

public class AimShootCommandAuto extends SequentialCommandGroup {

  public AimShootCommandAuto(double time) {
    addCommands(
        new InstantCommand(AimCommand::useStandardAutoAim),
        new InstantCommand(() -> ShootCommand.useStandardAutoAimForAutonomous(time)),
        new ParallelDeadlineGroup(
            new ShootCommand(),
            new AimCommand())

    );
  }

  public AimShootCommandAuto() {
    this(4);
  }
}
