package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ShootingRelatingCommands.ShootCommand;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase {
    private static final Shooter m_instance = new Shooter();

    private final CANSparkMax m_bottomShooter = new CANSparkMax(1, MotorType.kBrushless);
    private final CANSparkMax m_topShooter = new CANSparkMax(2, MotorType.kBrushless);

    private final RelativeEncoder m_bottomShooterEncoder = m_bottomShooter
            .getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    private final RelativeEncoder m_topShooterEncoder = m_topShooter
            .getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    private final PIDController m_bottomShooterPIDController = new PIDController(0.167, 0.0, 0.0);
    private final SimpleMotorFeedforward m_bottomShooterFeedforward = new SimpleMotorFeedforward(0.1113, 0.12233, 0.004487);
    private final PIDController m_topShooterPIDController = new PIDController(0.112, 0.0, 0.0);
    private final SimpleMotorFeedforward m_topShooterFeedforward = new SimpleMotorFeedforward(.1117, 0.1219, 0.003317);

    private double m_bottomSpeed = 0.0;
    private double m_topSpeed = 0.0;

    public Shooter() {
        m_bottomShooter.setInverted(false);
        m_bottomShooterEncoder.setVelocityConversionFactor(0.01666); // vel deffaults to RPM, this turns it to Rev/sec

        m_topShooter.setInverted(true);
        m_topShooterEncoder.setVelocityConversionFactor(0.01666);

        SendableRegistry.setSubsystem(m_topShooterPIDController, this.getClass().getSimpleName());
        SendableRegistry.setName(m_topShooterPIDController, "Shooter PIDController");
        SendableRegistry.setSubsystem(m_bottomShooterPIDController, this.getClass().getSimpleName());
        SendableRegistry.setName(m_bottomShooterPIDController, "Shooter PIDController");
    }

    public static Shooter getInstance() {
        return m_instance;
    }

    public double getBottomShooterRPS() {
        return m_bottomShooterEncoder.getVelocity();
    }

    public double getTopShooterRPS() {
        return m_topShooterEncoder.getVelocity();
    }

    public void shoot(double bottomSpeed, double topSpeed) {
        m_bottomSpeed = bottomSpeed;
        m_topSpeed = topSpeed;
    }

    public void shoot(double bothShooterSpeed) {
        shoot(bothShooterSpeed, bothShooterSpeed);
    }

    @Override
    public void periodic() {
        
        //determiming when to run shooter

        if (ShootCommand.activateShooter[0] != 0 || ShootCommand.activateShooter[1] != 0) {
            shoot(ShootCommand.activateShooter[0], ShootCommand.activateShooter[1]);
        } else if (Kicker.activateShooter[0] != 0 || Kicker.activateShooter[1] != 0) { // eject while intaking
            shoot(Kicker.activateShooter[0], Kicker.activateShooter[1]);
        } else {
            shoot(0);
        }

        //PID and feed forward when running shooter

        if (m_bottomSpeed > 0.01 || m_bottomSpeed < -0.01) {
            double shooterFeedforward = m_bottomShooterFeedforward.calculate(m_bottomSpeed);
            double shooterPIDOutput = m_bottomShooterPIDController.calculate(getBottomShooterRPS(), m_bottomSpeed);
            double shooterOutput = shooterFeedforward + shooterPIDOutput;

            m_bottomShooter.setVoltage(shooterOutput);
        } else {
            m_bottomShooter.setVoltage(0.0);
        }

        if (m_topSpeed > 0.01 || m_topSpeed < -0.01) {
            double shooterFeedforward = m_topShooterFeedforward.calculate(m_topSpeed);
            double shooterPIDOutput = m_topShooterPIDController.calculate(getTopShooterRPS(), m_topSpeed);
            double shooterOutput = shooterFeedforward + shooterPIDOutput;

            m_topShooter.setVoltage(shooterOutput);
        } else {
            m_topShooter.setVoltage(0.0);
        }

        SmartDashboard.putString("shooter RPS", "" + (int) getBottomShooterRPS() + ", " + (int) getTopShooterRPS());
    }

    public void set(double topSpeed, double bottomSpeed) {
        m_topShooter.set(topSpeed);
        m_bottomShooter.set(bottomSpeed);
    }
}
