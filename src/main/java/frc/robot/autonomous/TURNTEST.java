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

public class TURNTEST extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public TURNTEST() {
    Dumper m_Dumper = Dumper.getInstance();
  // Blue bumper tested
    addCommands(
        new DriveTurnCommand(-20, 3),
        new WaitCommand(1),
        new DriveTurnCommand(20, -3),
        new WaitCommand(.1)
      
 
        
    );
  }
}
