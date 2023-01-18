package frc.robot.commands.ShootingRelatingCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;

public class AimCommand extends CommandBase {

    private final Hood m_hood;
    private final Turret m_turret;
    private final Vision m_vision;

    private double turretPrecision;
    private double turretSpeed;

    private PIDController turretPidController = new PIDController(-0.05, .005, -0.003);

    // limelight values
    private double x;
    private double y;
    private double numberOfTargets;

    private static boolean isHoodAimed = false;
    private static boolean isTurretAimed = false;
    private static double isTarget = 0;

    private static boolean doesEnd = false;
    private static boolean aimWhileMove = false;
    private static int aimWhat = 1; // 0 = nothing, 1 = both, 2 = turret only, 3 = hood only

    public AimCommand() {
        m_hood = Hood.getInstance();
        m_turret = Turret.getInstance();
        m_vision = Vision.getInstance();

        addRequirements(m_hood, m_turret);

        turretPidController.setIntegratorRange(-5, 5);
    }

    @Override
    public void execute() {
        refreshAimValues();
        refreshIsAimedValues();
        if (aimWhat == 1) {
            aimHood();
            aimTurret();
        } else if (aimWhat == 2) {
            aimTurret();
        } else if (aimWhat == 3) {
            aimHood();
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_hood.stop();
        m_turret.stop();
    }

    @Override
    public boolean isFinished() {
        if (doesEnd) { // ends as soon as it is aimed
            if (aimWhat == 1) {
                return isAimFinished();
            } else if (aimWhat == 2) {
                return isTurretAimed();
            } else if (aimWhat == 3) {
                return isHoodAimed();
            } else {
                return true;
            }
        } else { // does not end
            return false;
        }
    }

    private void refreshAimValues() {
        x = m_vision.getTx(); // left/right displacement angle
        y = m_vision.getTy(); // vertical displacement angle
        numberOfTargets = m_vision.getTv(); // 0 = no target, 1 = target
    }

    private void refreshIsAimedValues() { // have to use the static values since isAimed needs to be static to access in ShootCommand
        isHoodAimed = m_hood.isAimed();
        isTurretAimed = m_vision.isAimed(turretPrecision);
        isTarget = numberOfTargets;
    }

    private void aimHood() {
        if (numberOfTargets == 1) {
            if (y > 6.9) { // 55 rps
                m_hood.aim(3581.55 + y * -453.53 + y * y * 25.3331 + y * y * y * -0.5068); 
            } else if (y > 2.0) { // 60 rps
                m_hood.aim(16.1772 * y * y + -327.65 * y + 2500);
            } else if (y <= 2.0) { // 65 rps
                m_hood.aim(2176.33 + -84.183 * Math.pow(0.77463, y));
             }
        }
    }

    private void aimTurret() {
        if (!aimWhileMove) {
            aimTurretPrototype(x);
            // aimTurretMain(x);
        } else {
            aimTurretWhileMoving();
        }
    }

    private void aimTurretPrototype(double tx) {
        //turret precission 
        if (y > 10) {
            turretPrecision = 4;
        } else {
            turretPrecision = 2;
        }

        if (Math.abs(x) > turretPrecision) {
            double tSpeed = turretPidController.calculate(x, 0);
            double tSpeedClamped;
            if (tSpeed > 0) {
                tSpeedClamped = MathUtil.clamp(tSpeed, .15, .6);
            } else {
                tSpeedClamped = MathUtil.clamp(tSpeed, -.6, -.15);
            }
            m_turret.spin(tSpeedClamped);
        } else {
            m_turret.spin(0);
        }

    }

    private void aimTurretMain(double tx) {
        aimTurretSpeed();
        refreshTurretPrecision();

        if (aimWhileMove) {
            turretPrecision *=3;
            turretSpeed = MathUtil.clamp(Math.abs(x) / 25, .22, .6);
        }

        if (tx > turretPrecision) {
            m_turret.spin(turretSpeed);
        } else if (tx < -turretPrecision) {
            m_turret.spin(-turretSpeed);
        } else {
            m_turret.spin(0.0);
        }
    }

    private void aimTurretSpeed() {
        double speed = Math.abs(x) / 50.0; // equivilent to a PID (P only), goes proportionally slower the closer you are
        turretSpeed = MathUtil.clamp(speed, .22, .6); // min = .22, max = .6
    }

    private void refreshTurretPrecision() { // designed to get the precision based on speed (on distance)
        if (numberOfTargets > 0) {
            if (y > 10) {
                turretPrecision = 2;
            } else {
                turretPrecision = 1;
            }
        }
    }

    private void aimTurretWhileMoving() {
        double robotSpeed = Drive.getSpeed();

        double degreesOfTarget = m_vision.getThor() / 320 * 54; // degrese of target horizortally
        double kConstant = 1;
        double offset = degreesOfTarget * kConstant * robotSpeed;
        if (m_turret.getEncoder() > 125) {
            offset *= -1;
        }

        // aimTurretMain(x - offset);
        aimTurretPrototype(x-offset);
    }

    public static boolean isAimFinished() {
        return isHoodAimed && isTurretAimed && isTarget == 1;
    }

    public static boolean isHoodAimed() {
        return isHoodAimed;
    }

    public static boolean isTurretAimed() {
        return isTurretAimed;
    }

    // settings to be called by instantCommands
    public static void useCustom(boolean doesItEnd, boolean movingAiming, int aimMode) {
        doesEnd = doesItEnd;
        aimWhileMove = movingAiming;
        aimWhat = aimMode;
    }

    public static void useStandardAutoAim() {
        doesEnd = false;
        aimWhileMove = false;
        aimWhat = 1;
    }

    public static void useMovingAutoAim() {
        doesEnd = false;
        aimWhileMove = true;
        aimWhat = 1;
    }
}
