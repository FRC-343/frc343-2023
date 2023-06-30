package frc.robot;
//Reverse test
import frc.robot.autonomous.*;
import frc.robot.autonomous.Auto_Bal_Things.AUTOBALCHA;
import frc.robot.autonomous.Auto_Bal_Things.AUTOBALMOBILE;
import frc.robot.autonomous.Auto_Bal_Things.LEFTBALTWOCUBE;
import frc.robot.autonomous.Auto_Bal_Things.REDAUTABAL_HEART;
import frc.robot.autonomous.Auto_Bal_Things.RIGHTBALTWOCUBE;
import frc.robot.autonomous.Auto_Bal_Things.TWOCUBETESTRIGHT;
import frc.robot.autonomous.Auto_Bal_Things.TWOCUBETESTLEFT;
import frc.robot.autonomous.Auto_Bal_Things.TWOCUBETESTRIGHT;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.commands.driveCommands.DriveDistanceCommand;
import frc.robot.commands.driveCommands.autoBal;
import frc.robot.utils.MiscMath;

import frc.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;



import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;




import edu.wpi.first.wpilibj2.command.button.Trigger;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Robot extends TimedRobot {
    public  double kMaxJoySpeed = 4; // meters per sec
    public  double kMaxJoyTurn = 6; // radians per sec
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
    private final grayson m_Grayson = grayson.getInstance();
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
            // Rename to what drive team wants
        m_autoChooser.setDefaultOption("No_Auto", new NoAutonomous());
        m_autoChooser.addOption("TWO CUBE", new TWOCUBE());
        // m_autoChooser.addOption("RED_CUBECHARGETURN", new CUBECGTURNRED());
        // m_autoChooser.addOption("BLUE_CUBECHARGETURN", new CUBECGTURNBLUE());
        m_autoChooser.addOption("CUBEDUMP SIT", new CUBEDUMP());
        m_autoChooser.addOption("CUBE DUMP MOBILE", new DRIVEOUT());
        m_autoChooser.addOption("AUTO BAL CHARGE NOT MOBILE", new AUTOBALCHA());
        m_autoChooser.addOption("RED THREE CUBE", new RED_THREECUBE());
        m_autoChooser.addOption("MOBILE AUTO BAL", new AUTOBALMOBILE());
        m_autoChooser.addOption("RED NON-MOBILE AUTO BAL", new REDAUTABAL_HEART());
        m_autoChooser.addOption("LEFT TWO CUBE BAL", new RIGHTBALTWOCUBE());
        m_autoChooser.addOption("RIGHT TWO CUBE BAL", new LEFTBALTWOCUBE());
        m_autoChooser.addOption("BLUE THREE CUBE", new BLUE_THREECUBE());
        m_autoChooser.addOption("TURN TEST", new TURNTEST());   // remove before comp
        m_autoChooser.addOption("ARM MOVMENT TEST", new ARMTEST());
        m_autoChooser.addOption("Center Test", new CENTERTEST());
        m_autoChooser.addOption("TIME CONVEYOR TEST", new TIMEDCONVEYORTEST());
        m_autoChooser.addOption("RIGHT TWO CUBE BAL TEST", new TWOCUBETESTRIGHT());
        m_autoChooser.addOption("LEFT TWO CUBE BAL TEST", new TWOCUBETESTLEFT());
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
            new RunCommand(() -> m_arm.setArm(  -m_controller.getLeftY()/1.5), m_arm));

            m_Spinner.setDefaultCommand(
                new RunCommand(() -> m_Spinner.setSpinner(m_controller.getRawAxis(5)*2.5), m_Spinner));


        // Joystick


        m_drive.setDefaultCommand(new RunCommand(() -> m_drive.drive(kMaxJoySpeed *
                MiscMath.deadband(m_stick.getY()/-.5),
                kMaxJoyTurn * MiscMath.deadband(m_stick.getX()/-.5)), m_drive));

        // Joystick buttons
          new JoystickButton(m_stick, 3).whileTrue(new autoBal());

        new JoystickButton(m_controller, XboxController.Button.kRightBumper.value)
        .whileTrue(new InstantCommand(m_Dumper::engage, m_Dumper))
        .whileFalse(new InstantCommand(m_Dumper::disEngage,m_Dumper));

        new JoystickButton(m_controller, XboxController.Button.kX.value)
            .onTrue(new InstantCommand(m_Grayson::disEngage, m_Grayson))
            .onFalse(new InstantCommand(m_Grayson::engage, m_Grayson));

        new JoystickButton(m_stick, 10).onTrue(new InstantCommand(m_intake::lower, m_intake));
        new JoystickButton(m_stick, 11).onTrue(new InstantCommand(m_intake::raise, m_intake));


        new JoystickButton(m_controller, XboxController.Button.kBack.value)
        .onTrue(new InstantCommand(m_Pincher::toBeOrNotToBe, m_Pincher)); // toggle climber pnumatics

        new JoystickButton(m_controller, XboxController.Button.kStart.value)
        .onTrue(new InstantCommand(m_arm::MastMovment, m_arm));



        // Joystick Trigger

        new JoystickButton(m_stick, 1).whileTrue(new IntakeCommand(-.8))
                .whileFalse(new Intake2Command(-.8));

                new JoystickButton(m_stick, 1).whileTrue(new ConveyorCommand(-.8))
                .whileFalse(new ConveyorCommand(0));

        // Other Joystick Buttons


        // new JoystickButton(m_stick, 8).whenHeld(new RunCommand(m_vision::killYourEnimiesViaLEDS))
        //         .whenReleased(new RunCommand(() -> m_vision.setLEDS(true)));

        // new JoystickButton(m_stick, 7).whenHeld(new IntakeCommand(.8))
        //         .whenReleased(new Intake2Command(.8));

        // Controller joysticks



        // Controller Triggers/Bumpers


        new Trigger(() -> m_controller.getYButton()).onTrue(new IntakeCommand(-.1));


        // new JoystickButton(m_controller, XboxController.Button.kStart.value)
        //         .whenHeld(new AutoClimbCommand(false, false)); // climbing auto

        // these two commands are weird, "activateKicker" is public, kicker subsystem will activate kicker based off of that var
        // this way manual control always overrides other things that are happening with the kicker
        // new JoystickButton(m_controller, XboxController.Button.kA.value)
        //         .onTrue(new InstantCommand(() -> activateKicker = 1))
        //         .onFalse(new InstantCommand(() -> activateKicker = 0));

        new JoystickButton(m_controller, XboxController.Button.kB.value)
                .whileTrue(new ConveyorCommand(-.6))
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
    public void disabledPeriodic() {
    }
    @Override
    public void simulationPeriodic() {}

}
