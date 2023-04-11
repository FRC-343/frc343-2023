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

public class CENTERTEST extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public CENTERTEST() {
    grayson m_Grayson = grayson.getInstance();
    addCommands(
        new InstantCommand(m_Grayson::disEngage, m_Grayson),
        new WaitCommand(1),
        new InstantCommand(m_Grayson::engage, m_Grayson)

    );
  }
}
