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

public class TIMEDCONVEYORTEST extends SequentialCommandGroup {
  private static final double kDriveSpeed = 1;

  public TIMEDCONVEYORTEST() {
    
    addCommands(
     new ConveyorTime(-.8, 3),
     new WaitCommand(3),
     new ConveyorTime(-.8, 1)

    );
  }
}
