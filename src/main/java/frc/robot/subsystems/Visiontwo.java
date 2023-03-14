package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//18.2 degrees

public class Visiontwo extends SubsystemBase {
    private static final Visiontwo m_instance = new Visiontwo();

    private final NetworkTable table = NetworkTableInstance.create().getTable("limelighttwo");
    private final NetworkTableEntry tx = table.getEntry("tx");
    private final NetworkTableEntry ty = table.getEntry("ty");

    public static Visiontwo getInstance() {
        return m_instance;
    }

    public double getTx() {
        return tx.getDouble(0.0);
    }

    public double getTy() {
        return ty.getDouble(0.0);
    }

    public double getTv() {
        return table.getEntry("tv").getDouble(0.0);
    }

    public double getThor() { //possibly with a hammer
        return table.getEntry("thor").getDouble(0.0);
    }

    public boolean isAimed(double precision) {
        return Math.abs(getTx()) < precision;
    }

    public void setLEDS(boolean turnOn) {
        if (turnOn) {
            table.getEntry("ledMode").setNumber(3);
        } else {
            table.getEntry("ledMode").setNumber(1);
        }
    }

    public void killYourEnimiesViaLEDS() {
        table.getEntry("ledMode").setNumber(2);
    }

    public void setLEDSToDefault() {
        table.getEntry("ledMode").setNumber(0);
    }

    //this is for second camera plugged into limelight
    public void setCamera(double value) {//vaulue = 0 split, 1 = secondary camera is small, 2 = limelight is small
        table.getEntry("stream").setNumber(1);
    }
}