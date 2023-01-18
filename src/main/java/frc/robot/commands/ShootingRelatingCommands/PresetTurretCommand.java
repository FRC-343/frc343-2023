package frc.robot.commands.ShootingRelatingCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Turret;

public class PresetTurretCommand extends CommandBase {

    private final Turret m_turret;

    private boolean m_startWithZeroing;

    private double kTarget;
    
    public PresetTurretCommand(double target, boolean startWithZeroing) {
        m_turret = Turret.getInstance();
        kTarget = target;
        m_startWithZeroing = startWithZeroing;
        addRequirements(m_turret);
    }

    public PresetTurretCommand(double target) {
        this(target, false);
    }

    @Override
    public void execute() {
        m_turret.aim(kTarget, m_startWithZeroing);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.stop();
    }

    @Override
    public boolean isFinished() {
        return m_turret.isAimed();
    }

}
