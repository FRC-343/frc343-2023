package frc.robot.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.*;

import frc.robot.commands.driveCommands.*;
import frc.robot.subsystems.*;
import frc.robot.commands.*;

public class NOCGCUBE extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public NOCGCUBE() {
    Dumper m_Dumper = Dumper.getInstance();
    pincher m_pincher = pincher.getInstance();
    addCommands(
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(1),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.025, 2),
            new InstantCommand(m_Dumper::disEngage, m_Dumper)),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.005, .5),
            new IntakeCommand(),
            new ConveyorCommand(-.8)),
        new WaitCommand(1),
        new ParallelDeadlineGroup(
          new DriveDistanceCommand(.028, -2),
          new InstantCommand(m_pincher::engage,m_pincher)),
        new WaitCommand(1),
        new InstantCommand(m_Dumper::engage, m_Dumper)
    );
  }
}
