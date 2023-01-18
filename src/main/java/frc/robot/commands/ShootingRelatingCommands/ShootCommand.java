package frc.robot.commands.ShootingRelatingCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.*;

public class ShootCommand extends CommandBase {
    // in Rev/Sec
    private double kTopShootSpeed;
    private double kBottomShootSpeed;

    private final Shooter m_shooter;
    private final Vision m_vision;
    private final Kicker m_kicker;

    private static final double lowGoalSpeed[] = { 30, 30 }; // bottom wheel, top wheel

    private double y; // ty from limelight
    private double v; // tv from limelight, # = # of targets

    private Timer endTimer = new Timer(); // for ending shooting
    private static boolean stopShooterAfterTime; // for auto
    private static double time;

    private Timer isGoodSpeed = new Timer(); // used for when shooting to check if speed has been good for x time

    private static int waitForAim; // 0 = false, 1 = true, 2 = only turret, 3 = only hood

    private static boolean useVariableSpeed;
    private static double shooterDesiredSpeed[] = { 0, 0 };

    private double shooterSpeed;

    public static double activateKicker = 0;

    public static double activateShooter[] = { 0, 0 }; // bottom speed, top speed

    public ShootCommand() {

        m_shooter = Shooter.getInstance();
        m_kicker = Kicker.getInstance();
        m_vision = Vision.getInstance();

        addRequirements(); // vision, shooter, and kicker don't run any motors, they just grabs values

        refreshAimValues();

        time = 4.0;
        shooterSpeed = 70;

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        endTimer.start();
        endTimer.reset();
        isGoodSpeed.start();
        isGoodSpeed.reset();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        refreshAimValues();
        isShooterSpeedReady_v2();

        if (Robot.kUseColorSensor && (m_kicker.isBadCargo() || m_kicker.isRecentlyBadCargo(.3))) { // if bad cargo
            ejectBadCargo();
        } else { // good carg
            if (useVariableSpeed) {
                setShooterSpeed(getShooterSpeed());
            } else {
                setShooterSpeed(shooterDesiredSpeed[0], shooterDesiredSpeed[1]);
            }
            shootShooter();
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shoot(0, 0);
        activateKicker = 0;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (stopShooterAfterTime) {
            return endTimer.get() >= time;
        } else {
            return false;
        }

    }

    private void shootShooter() {
        shoot(kBottomShootSpeed, kTopShootSpeed);

        int w = waitForAim;
        boolean a = AimCommand.isAimFinished(); //both turret and hood
        boolean t = AimCommand.isTurretAimed();
        boolean h = AimCommand.isHoodAimed();

        if (((w == 0) || (w == 1 && a) || (w == 2 && t) || (w == 3 && h)) && isShooterSpeedReady_v2()) {
            activateKicker = 1; //if aimed mode matches what needs to be aimed and shooter speed is ready then turn on kicker
        } else {
            activateKicker = 0;
        }
    }

    private void shoot(double bottomSpeed, double topSpeed) {
        activateShooter[0] = bottomSpeed;
        activateShooter[1] = topSpeed;
    }

    private boolean isShooterSpeedReady() {
        double marginOfSpeedError = 3.0;
        return (m_shooter.getBottomShooterRPS() > kBottomShootSpeed - marginOfSpeedError && m_shooter.getBottomShooterRPS() < kBottomShootSpeed + marginOfSpeedError 
                && m_shooter.getTopShooterRPS() > kTopShootSpeed - marginOfSpeedError && m_shooter.getTopShooterRPS() < kTopShootSpeed + marginOfSpeedError);
                // if speed is within the margin of error it will return true
    }

    private boolean isShooterSpeedReady_v2(double time) {
        if (!isShooterSpeedReady()) {
            isGoodSpeed.reset();
        }

        return (isShooterSpeedReady() && isGoodSpeed.get() > time);
    }

    private boolean isShooterSpeedReady_v2() {
        return isShooterSpeedReady_v2(0.5);
    }

    private void setShooterSpeed(double bottomspeed, double topSpeed) {
        kBottomShootSpeed = bottomspeed;
        kTopShootSpeed = topSpeed;
    }

    private void setShooterSpeed(double speed) {
        setShooterSpeed(speed, speed); // set topSpeed higher so less rotation
    }

    private double getShooterSpeed() {
        if (v == 1) {
            if (y > 6.9) {
                shooterSpeed = 55;
            } else if (y > 2.0) {
                shooterSpeed = 60;
            } else if (y <= 2.0 ) {
                shooterSpeed = 65;
             } 
        }

        return shooterSpeed;
    }

    private void refreshAimValues() {
        v = m_vision.getTv();
        y = m_vision.getTy();
    }

    public void ejectBadCargo() {
        if (shooterDesiredSpeed == lowGoalSpeed) { // low goal
            shoot(40, 40); // give it more power
            if (m_shooter.getBottomShooterRPS() > 30.0 && m_shooter.getTopShooterRPS() > 30.0) {
                activateKicker = 1;
            }
        } else { //not low goal
            shoot(20, 20); // less power
            if (m_shooter.getBottomShooterRPS() < 40.0 && m_shooter.getTopShooterRPS() < 40.0) {
                activateKicker = 1;
            }
        }
    }

    // Settings called before calling shootCommand
    public static void useStandardAutoAim() {
        useVariableSpeed = true;
        waitForAim = 1; // true
        stopShooterAfterTime = false;
    }

    public static void useStandardAutoAimForAutonomous(double seconds) {
        useStandardAutoAim();
        stopShooterAfterTime = true;
        time = seconds;
    }

    public static void useLowGoal() {
        useVariableSpeed = false;
        shooterDesiredSpeed = lowGoalSpeed;
        waitForAim = 0;
        stopShooterAfterTime = false;

    }

    public static void useCustom(boolean useVarSpeed, double bottomSpeed, double topSpeed, int waitAim,
            double seconds) {

        useVariableSpeed = useVarSpeed;
        waitForAim = waitAim;

        if (!useVariableSpeed) {
            shooterDesiredSpeed = new double[] { bottomSpeed, topSpeed };
        }

        if (seconds > 0) {
            stopShooterAfterTime = true;
            time = seconds;
        } else {
            stopShooterAfterTime = false;
        }

    }

}
