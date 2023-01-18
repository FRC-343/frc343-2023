package frc.robot.utils;

public class MiscMath {
    public static final double kDefaultDeadband = .08;

    public static double deadband(double value, double min) {
        if (Math.abs(value) > min) {
            if (value > 0.0) {
                return (value - min) / (1.0 - min);
            } else {
                return (value + min) / (1.0 - min);
            }
        } else {
            return 0.0;
        }
    }

    public static double deadband(double value) {
        return deadband(value, kDefaultDeadband);
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }
}
