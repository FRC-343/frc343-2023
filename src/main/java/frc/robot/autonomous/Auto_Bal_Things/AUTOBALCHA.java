package frc.robot.autonomous.Auto_Bal_Things;

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

public class AUTOBALCHA extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public AUTOBALCHA() {
    Dumper m_Dumper = Dumper.getInstance();
  // Blue bumper tested
    addCommands(
        new InstantCommand(m_Dumper::engage, m_Dumper),
        new WaitCommand(2),
        new ParallelDeadlineGroup(
            new DriveDistanceCommand(.012, 3),
            new InstantCommand(m_Dumper::disEngage, m_Dumper)),      
        new WaitCommand(.2),
        new autoBal()
        
    );
  }
}