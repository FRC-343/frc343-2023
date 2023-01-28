// package frc.robot.autonomous;

// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.commands.driveCommands.SimpleDriveCommand;

// import edu.wpi.first.wpilibj.ADXRS450_Gyro;

// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;
// import frc.robot.commands.*;
// import frc.robot.commands.ShootingRelatingCommands.*;
// import frc.robot.commands.ShootingRelatingCommands.SpecificCommands.*;
// import frc.robot.commands.driveCommands.*;
// import frc.robot.subsystems.*;



// public class MoveAuto extends SequentialCommandGroup {
//   private static final double kDriveTime = 3;
//   private static final double kDriveSpeed = 1;
//   private static final double kWaitTime = 10;
//   private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();

//   public MoveAuto() {
//     // commands in this autonomous
//    if(m_gyro.getAngle()>= 10){
//     new DriveDistanceCommand(1.1, 3);
//    }
//   }
// }
