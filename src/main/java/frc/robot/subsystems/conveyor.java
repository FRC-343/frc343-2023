package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

import java.io.ObjectOutputStream.PutField;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class conveyor {
    private static final conveyor m_instance = new conveyor();
    

    private final Spark m_conveyor = new Spark(9);

    private final DigitalInput m_cellDetector = new DigitalInput(14);

    private final ColorSensorV3 m_color = new ColorSensorV3(I2C.Port.kOnboard);

    private final double kMinColorRatio = 1.3;
    private String RcolorString = "Red";
    private String GcolorString = "Green";
    private String BcolorString = "Blue";
    
  


    

    public static conveyor getInstance() {
        return m_instance;
    }
    
    public void periodic() {

        // running kicker motor
        if (Robot.activateKicker != 0) {// manual control
            setConveyor(Robot.activateKicker);
        
        } else if (Intake.isRunning()) { // kicker for intake
            conveyorForIntake();
        } else {
            setConveyor(0);
        }
    

        // color sensor things

                SmartDashboard.putNumber(GcolorString, m_color.getGreen());
                SmartDashboard.putNumber(RcolorString, m_color.getRed());
                SmartDashboard.putNumber(BcolorString, m_color.getBlue());



    }
    public void setConveyor(double speed) {
        m_conveyor.set(speed);
    }

  
    private void conveyorForIntake() {
        
            setConveyor(1.0);
     
    }


}