package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.commands.ShootingRelatingCommands.ShootCommand;

public class Kicker extends SubsystemBase {
    private static final Kicker m_instance = new Kicker();
    public static double activateShooter[] = { 0, 0 }; // bottom speed, top speed

    private final Spark m_kicker = new Spark(4);

    private final DigitalInput m_cellDetector = new DigitalInput(14);

    private final ColorSensorV3 m_color = new ColorSensorV3(I2C.Port.kOnboard);

    private final double kMinColorRatio = 1.3;
    private String colorString = "";

    private Timer timerBadCargo = new Timer();

    public Kicker() {
        m_kicker.setInverted(true);

        SendableRegistry.setSubsystem(m_kicker, this.getClass().getSimpleName());
        SendableRegistry.setName(m_kicker, "Kicker Motor");

        SendableRegistry.setSubsystem(m_cellDetector, this.getClass().getSimpleName());
        SendableRegistry.setName(m_cellDetector, "cell detector for shooter/intake");

        timerBadCargo.start();
    }

    public static Kicker getInstance() {
        return m_instance;
    }

    @Override
    public void periodic() {

        // running kicker motor
        if (Robot.activateKicker != 0) {// manual control
            setKicker(Robot.activateKicker);
        } else if (ShootCommand.activateKicker != 0) { // shooter is ready
            setKicker(ShootCommand.activateKicker);
        } else if (Intake.isRunning()) { // kicker for intake
            kickerForIntake();
        } else {
            setKicker(0);
            shoot(0, 0); // if shooter wants to run to shoot, it will override this
        }

        // color sensor things
        if (m_color.getRed() != 0 && m_color.getBlue() != 0) {
            if (m_color.getRed() / m_color.getBlue() > kMinColorRatio) {
                SmartDashboard.putString("color_detected", "red");
                colorString = "Red";
            } else if (m_color.getBlue() / m_color.getRed() > kMinColorRatio) {
                SmartDashboard.putString("color_detected", "blue");
                colorString = "Blue";
            } else {
                SmartDashboard.putString("color_detected", "None Colors there be");
                colorString = "";
            }
        } else {
            SmartDashboard.putString("color_detected", "color sensor is color blind");
            colorString = "";
        }

        isRecentlyBadCargo(); // make sure the timer is triggered
    }

    public void setKicker(double speed) {
        m_kicker.set(speed);
    }

    public boolean isBadCargo() { // returns true if wrong color
        boolean value;

        if (DriverStation.getAlliance().equals(DriverStation.Alliance.valueOf("Invalid"))) { // check to see if the driver station is actually returninng a proper team color
            value = false;
            SmartDashboard.putString("color_detected", "Purple");
        } else if (colorString.isBlank()) {
            value = false;
        } else if (!getCellDetector()) {
            value = false;
        } else {
            value = !(DriverStation.getAlliance().equals(DriverStation.Alliance.valueOf(colorString)));
        }
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

    private void shoot(double bottom, double top) {
        activateShooter[0] = bottom;
        activateShooter[1] = top;

    }

    private void kickerForIntake() {
        if (!getCellDetector()) { // if no ball is in chamber run the kicker so it goes into chanber // room for the 2nd ball in the hopper
            shoot(0, 0);
            setKicker(1.0);
        } else { // if getCellDetector()
            if (Robot.kUseColorSensorIntake) {
                if (isBadCargo() || isRecentlyBadCargo()) { // if bad cargo shoot out
                    shoot(40, -80);
                    setKicker(1.0);
                } else { // if good, then stop cargo
                    setKicker(0.0);
                    shoot(0, 0);
                }
            } else { // getCellDectector: ball in chamber
                setKicker(0.0);
                shoot(0, 0);
            }
        }

    }

}
