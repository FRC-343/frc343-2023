package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.conveyor;

public class ConveyorTime extends CommandBase {

    private double m_speed;
    private final conveyor m_conveyor;
    private Timer t;
    private double m_time;
    
    public ConveyorTime(double speed, double time) {
        m_conveyor = conveyor.getInstance();
        m_speed = speed;
        t = new Timer();
        m_time = time;
    }
    

    @Override
    public void initialize() {
        t.reset();
        t.start();
    }


    @Override
    public void execute() {
        m_conveyor.setConveyor(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_conveyor.setConveyor(0.0);
    }

    @Override
    public boolean isFinished() {
        if (t.get() >= m_time) {
            return true;
        } else {
            return false;
        }
    }
}
