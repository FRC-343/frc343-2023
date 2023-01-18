package frc.robot.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.SimpleDriveCommand;

public class MoveAuto extends SequentialCommandGroup {
  private static final double kDriveTime = 3;
  private static final double kDriveSpeed = 1;
  private static final double kWaitTime = 10;

  public MoveAuto() {
    // commands in this autonomous
    addCommands(new SimpleDriveCommand(kDriveSpeed, kDriveTime, kWaitTime)); //wait 10 seconds, then drive forward at 1m/s for 2 seconds

  }
}
