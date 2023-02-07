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
    

    private final Spark m_conveyor = new Spark(4);

    private final DigitalInput m_cellDetector = new DigitalInput(14);

    private final ColorSensorV3 m_color = new ColorSensorV3(I2C.Port.kOnboard);

    private final double kMinColorRatio = 1.3;
    private String RcolorString = "Red";
    private String GcolorString = "Green";
    private String BcolorString = "Blue";
    
    private Timer timerBadCargo = new Timer();
    public conveyor() {
        m_conveyor.setInverted(true);

        SendableRegistry.setSubsystem(m_conveyor, this.getClass().getSimpleName());
        SendableRegistry.setName(m_conveyor, "Kicker Motor");

        SendableRegistry.setSubsystem(m_cellDetector, this.getClass().getSimpleName());
        SendableRegistry.setName(m_cellDetector, "cell detector for shooter/intake");
        timerBadCargo.start();
    }



    

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



        isRecentlyBadCargo(); // make sure the timer is triggered
    }
    public void setConveyor(double speed) {
        m_conveyor.set(speed);
    }
public boolean isBadCargo() {
boolean value;
value=false;

return value;
}
    
    public boolean isRecentlyBadCargo(double time) {
        if (isBadCargo()) {
            timerBadCargo.start();
            timerBadCargo.reset();
        }
        if (timerBadCargo.get() < time) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isRecentlyBadCargo() {
        return isRecentlyBadCargo(.5);
    }

    public boolean getCellDetector() {
        return m_cellDetector.get(); // true = ball in chamber
    }

    private void conveyorForIntake() {
        if (!getCellDetector()) { // if no ball is in chamber run the kicker so it goes into chanber // room for
                                  // the 2nd ball in the hopper

            setConveyor(1.0);
        } else { // getCellDectector: ball in chamber
            setConveyor(0.0);

    
        }
    
    }


}