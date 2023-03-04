package frc.robot;
//Reverse test 
import frc.robot.autonomous.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.commands.driveCommands.DriveDistanceCommand;
import frc.robot.utils.MiscMath;

import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;



import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;




import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.Button;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Robot extends TimedRobot {
    public  double kMaxJoySpeed = 3; // meters per sec
    public  double kMaxJoyTurn = 5; // radians per sec
    public static final double kMaxHoodSpeed = 0.8; // ratio
    public static final double kMaxWinchSpeed = 1.0;
    public static final double kMaxTurretSpeed = 0.6;
    public static final double kMaxClimbingSpeed = .8;

    // private static final Compressor Pressy = new Compressor(0, PneumaticsModuleType.REVPH);

    public static double activateKicker = 0;



    private final Arm m_arm = Arm.getInstance();

    private final Drive m_drive = Drive.getInstance();
    private final Vision m_vision = Vision.getInstance();
    private final Intake m_intake = Intake.getInstance();
    private final pincher m_Pincher = pincher.getInstance();
    private final conveyor m_Conveyor = conveyor.getInstance();
    private final Dumper m_Dumper = Dumper.getInstance();
    private final Spinner m_Spinner = Spinner.getInstance();
    // shooter and kicker exist also, but are not needed in this file. They are still created though because other commands call the getInstance() method

    private final XboxController m_controller = new XboxController(1);
    public final Joystick m_stick = new Joystick(0);

    private final Compressor pressy = new Compressor(PneumaticsModuleType.REVPH);
    private CommandBase m_auto;
    private final SendableChooser<CommandBase> m_autoChooser = new SendableChooser<CommandBase>();

    public static boolean kUseColorSensor = true;
    public static boolean kUseColorSensorIntake = true;

    public Robot() {
        m_autoChooser.setDefaultOption("No_Auto", new NoAutonomous());
        m_autoChooser.addOption("NOCHARGECUBE", new NOCGCUBE());
    }

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {

        SmartDashboard.putData("Auto_Choice", m_autoChooser);


     pressy.enableDigital();

        m_arm.setDefaultCommand(
            new RunCommand(() -> m_arm.setArm(m_controller.getLeftY()/-4), m_arm));
           
            m_Spinner.setDefaultCommand(
                new RunCommand(() -> m_Spinner.setSpinner(m_controller.getRawAxis(4)), m_Spinner));
            
        // Joystick
  

        m_drive.setDefaultCommand(new RunCommand(() -> m_drive.drive(kMaxJoySpeed *
                MiscMath.deadband(m_stick.getY()),
                kMaxJoyTurn * MiscMath.deadband(m_stick.getX())), m_drive));

        // Joystick buttons
       
        new JoystickButton(m_controller, XboxController.Button.kRightBumper.value)
        .whileTrue(new InstantCommand(m_Dumper::engage, m_Dumper))
        .whileFalse(new InstantCommand(m_Dumper::disEngage,m_Dumper));

        new JoystickButton(m_stick, 10).onTrue(new InstantCommand(m_intake::lower, m_intake));
        new JoystickButton(m_stick, 11).onTrue(new InstantCommand(m_intake::raise, m_intake));


        new JoystickButton(m_controller, XboxController.Button.kBack.value)
        .onTrue(new InstantCommand(m_Pincher::toBeOrNotToBe, m_Pincher)); // toggle climber pnumatics

        new JoystickButton(m_controller, XboxController.Button.kStart.value)
        .onTrue(new InstantCommand(m_arm::MastMovment, m_arm));
 

        
        // Joystick Trigger

        new JoystickButton(m_stick, 1).whileTrue(new IntakeCommand(.8))
                .whileFalse(new Intake2Command(.8));

                new JoystickButton(m_stick, 1).whileTrue(new ConveyorCommand(-.8))
                .whileFalse(new ConveyorCommand(0));
          
        // Other Joystick Buttons


        // new JoystickButton(m_stick, 8).whenHeld(new RunCommand(m_vision::killYourEnimiesViaLEDS))
        //         .whenReleased(new RunCommand(() -> m_vision.setLEDS(true)));

        // new JoystickButton(m_stick, 7).whenHeld(new IntakeCommand(.8))
        //         .whenReleased(new Intake2Command(.8));

        // Controller joysticks


       
        // Controller Triggers/Bumpers


        new Trigger(() -> m_controller.getYButton()).onTrue(new IntakeCommand(-.3));

      
        // new JoystickButton(m_controller, XboxController.Button.kStart.value)
        //         .whenHeld(new AutoClimbCommand(false, false)); // climbing auto

        // these two commands are weird, "activateKicker" is public, kicker subsystem will activate kicker based off of that var
        // this way manual control always overrides other things that are happening with the kicker
        // new JoystickButton(m_controller, XboxController.Button.kA.value)
        //         .onTrue(new InstantCommand(() -> activateKicker = 1))
        //         .onFalse(new InstantCommand(() -> activateKicker = 0));

        new JoystickButton(m_controller, XboxController.Button.kB.value)
                .whileTrue(new ConveyorCommand(-.8))
                .whileFalse(new ConveyorCommand(0));
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();

          }
    

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable chooser
     * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
     * remove all of the chooser code and uncomment the getString line to get the
     * auto name from the text box below the Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the SendableChooser
     * make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        m_vision.setLEDS(true);
        m_drive.zeroHeading();

        m_auto = m_autoChooser.getSelected();

        if (m_auto != null) {
            m_auto.schedule();
        }

    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {}

    /**
     * This function is called when entering operator control.
     */
    @Override
    public void teleopInit() {
        if (m_auto != null) {
            m_auto.cancel();
            m_drive.resetEncoders();

        }
        
        m_drive.zeroHeading();
        m_vision.setLEDS(true);
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        if(m_stick.getRawButton(8)){
            kMaxJoySpeed = 0.5;
            kMaxJoyTurn = 0.5;
        }if(m_stick.getRawButton(9)){
            kMaxJoySpeed = 3.0;
            kMaxJoyTurn = 5.0;
        }
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void disabledInit() {
        m_vision.setLEDS(false);
    
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void simulationPeriodic() {}

}
