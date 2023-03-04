package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import edu.wpi.first.wpilibj.motorcontrol.Spark;


public class Spinner extends SubsystemBase{
   private static final Spinner m_instance = new Spinner();

    private final Spark m_Spinner = new Spark(5);
    private static boolean runningSpinner = false;

    public Spinner() {
       
    }
    
    public static Spinner getInstance() {
        return m_instance;
    }





    public void moveSpinner( double speed){
        speed = 1;


    }
    
    public void setSpinner(double speed) {
      
      
           m_Spinner.set(-speed);
         
    }


    
    } 
    





