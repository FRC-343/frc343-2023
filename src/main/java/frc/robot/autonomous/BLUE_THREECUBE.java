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

public class BLUE_THREECUBE extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public BLUE_THREECUBE() {
    Dumper m_Dumper = Dumper.getInstance();
    pincher m_pincher = pincher.getInstance();
    addCommands(
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(.3),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.023, 3),
            new InstantCommand(m_Dumper::disEngage, m_Dumper)),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.005, .7),
            new IntakeCommand(),
            new ConveyorCommand(-.8)),
        new WaitCommand(.1),
        new ParallelDeadlineGroup(
          new DriveDistanceCommand(.004, -.5),
          new IntakeCommand(),
          new ConveyorCommand(-.8)),
        new ParallelDeadlineGroup(
          new DriveDistanceCommand(.018, -3),//0234
          new WaitCommand(.2),
          new ConveyorCommand(-.8)),
        new WaitCommand(.55),
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(.1),
        new InstantCommand(m_Dumper::disEngage, m_Dumper),
        new WaitCommand(.1),
        new DriveDistanceCommand(.016, 3),
        new WaitCommand(.2),
        new DriveTurnCommand(35, 3),
        new WaitCommand(.1),
        new ParallelDeadlineGroup(
        new DriveDistanceCommand(.01, 1.5),
        new IntakeCommand(),
        new ConveyorCommand(-.8))


    );
  }
}
