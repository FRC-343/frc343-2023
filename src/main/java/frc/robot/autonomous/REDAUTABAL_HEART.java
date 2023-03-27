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

public class REDAUTABAL_HEART extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public REDAUTABAL_HEART() {
    Dumper m_Dumper = Dumper.getInstance();
  // Blue bumper tested
    addCommands(
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(2),
        new DriveDistanceCommand(0.001, 1),
        
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.009, 3.5),
            new InstantCommand(m_Dumper::disEngage, m_Dumper)),      
        new WaitCommand(.2),
        new autoBal()
        
    );
  }
}