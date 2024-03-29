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

public class DRIVEOUT extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public DRIVEOUT() {
    Dumper m_Dumper = Dumper.getInstance();
  
    addCommands(
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(1),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.021, 3),
            new InstantCommand(m_Dumper::disEngage, m_Dumper)));
  }
}

