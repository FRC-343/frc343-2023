package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.conveyor;

public class ConveyorCommand extends CommandBase {

    private double m_speed;
    private final conveyor m_conveyor;

    public ConveyorCommand(double speed) {
        m_conveyor = conveyor.getInstance();
        m_speed = speed;
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
        boolean value = false;
        // if (m_speed > 0) { // positive speed is going down
        //     if (m_conveyor.getBottomLimit()) {
        //         value = true;
        //     }
        // } else if (m_speed < 0) {
        //     if (m_conveyor.getTopLimit()) {
        //         value = true;
        //     }
       if (m_speed == 0) {
             value = true;
         }

        return value;
    }
}
