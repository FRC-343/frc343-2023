package frc.robot.utils;
public class MiscMath {
    public static final double kDefaultDeadband = .27;
    public static final double ControllerDeadBand = .05;
    
    public static double deadband(double value, double min) {
       // return Math.abs(value) > min
            // ? value > 0.0 
            //     ? ((value - min) / (1.0 - min)) 
            //     : ((value + min) / (1.0 - min))
            // : 0.0;
         if (Math.abs(value) > kDefaultDeadband) {
              if (value > 0.0) {
                  return (value - kDefaultDeadband);/// (1.0 - kDefaultDeadband);
              } else {
                  return (value + kDefaultDeadband);// (1.0 - kDefaultDeadband);
              }
            //  return value > 0.0 
            //      ? ((value - min) / (1.0 - min)) 
            //      : ((value + min) / (1.0 - min));
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
