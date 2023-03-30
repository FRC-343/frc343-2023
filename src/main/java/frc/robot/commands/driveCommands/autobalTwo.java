package frc.robot.commands.driveCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drive;

public class autobalTwo extends CommandBase implements Runnable{
    private final Drive m_drive;
   
    public double leftSpeedvar = 0.0;
    public double rightSpeedvar= 0.0;
    public boolean chargestationbalance = false;
    double setpoint = 0;
    double errorSum = 0;
    double lastTimestamp = 0;
    double lastError = 0;
    double berror = 0;
    double errorRate = 0;
    double biLimit = 3 ;
   

    private Pose2d m_startPose = new Pose2d(0, 0, new Rotation2d(0));


    public autobalTwo() {
        m_drive = Drive.getInstance();
        addRequirements(m_drive);
  
    }

    @Override
    public void initialize() {
        m_startPose = m_drive.getPose();
        m_drive.drive(0, 0);

    }

    @Override
    public void execute() {
        double pitch = m_drive.getPitch();
       
        if (pitch <3 && pitch>-3){
            chargestationbalance=true;
            m_drive.brake();
            leftSpeedvar = 0;
            rightSpeedvar = 0;
           }
       
            else {
                setpoint = 0;
       
                // get sensor position
                Double sensorPosition = pitch;
       
                // calculations
                berror = setpoint - sensorPosition;
                double dt = Timer.getFPGATimestamp()- lastTimestamp;
       
                
               if (Math.abs(berror) < biLimit) {
                errorSum += berror * dt;
                }
      
                errorRate = (berror - lastError) / dt;
     
                Double leftoutputSpeed = (m_drive.getleftP()*2) * berror + m_drive.getleftI() * errorSum + m_drive.getleftD() * errorRate;
                //Double RightoutputSpeed = m_drive.getRightP() * berror + m_drive.getRightI() * errorSum + m_drive.getRightD() * errorRate;
            
                // output to motors
                leftSpeedvar=(leftoutputSpeed);
                //rightSpeedvar = (RightoutputSpeed);
                 
                
                // update last- variables
                lastTimestamp = Timer.getFPGATimestamp();
                lastError = berror;
        }
                // if (leftSpeedvar > .2 && rightSpeedvar > .2){
                //     leftSpeedvar = .2;
                //     rightSpeedvar = .2;
                // }
               
                // if (leftSpeedvar < -.2 && rightSpeedvar < -.2){
                //         leftSpeedvar = -.2;
                //         rightSpeedvar = -.2;
                // }
     
                m_drive.drive(-leftSpeedvar, 0);
                m_drive.testLeftValue(leftSpeedvar);
                m_drive.testRightValue(rightSpeedvar);
            }
     
    

    @Override
    public void end(boolean interrupted) {
        m_drive.drive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return chargestationbalance;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

}