package frc.robot.commands.ShootingRelatingCommands.SpecificCommands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ShootingRelatingCommands.AimCommand;
import frc.robot.commands.ShootingRelatingCommands.ShootCommand;

public class AimShootMoveCommand extends SequentialCommandGroup {

  public AimShootMoveCommand() {

    addCommands(
        new InstantCommand(AimCommand::useMovingAutoAim),
        new InstantCommand(ShootCommand::useStandardAutoAim),
        new ParallelDeadlineGroup(
            new ShootCommand(),
            new AimCommand())

    );
  }
}
