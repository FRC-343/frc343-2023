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

public class BLUE_BALTWOCUBE extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public BLUE_BALTWOCUBE() {
    Dumper m_Dumper = Dumper.getInstance();
  // Needs test 
    addCommands(
        new InstantCommand(m_Dumper::engage, m_Dumper), 
        new WaitCommand(.1),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.012, 3),
            new InstantCommand(m_Dumper::disEngage, m_Dumper)), 
        new DriveDistanceCommand(.01, 1.5), //Speed increase
        new WaitCommand(.1),
        new DriveTurnCommand(15, 4),   // Speed increase 
        new WaitCommand(.00001),  // way shorter wait time
        new ParallelDeadlineGroup(
          new DriveDistanceCommand(.008, 1.5),
           new IntakeCommand(),
           new ConveyorCommand(-.8)),
        new ParallelDeadlineGroup(
          new DriveDistanceCommand(.006, -1.5),
           new IntakeCommand(),
           new ConveyorCommand(-.8)),
        new WaitCommand(.1),
        new DriveTurnCommand(-15, 4),   // Faster turn
        new WaitCommand(.1),
        new DriveDistanceCommand(.007, -2),
        new WaitCommand(.1),
        new autobalTwo(),               // Next lines were added this one was reg autobal
        new WaitCommand(.0001),
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(.00001),
        new autoBal()
        
    );
  }
}
