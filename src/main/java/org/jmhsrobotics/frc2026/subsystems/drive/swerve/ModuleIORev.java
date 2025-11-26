package org.jmhsrobotics.frc2026.subsystems.drive.swerve;
 
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.geometry.Rotation2d;
import java.util.Queue;
import java.util.function.DoubleSupplier;
import org.jmhsrobotics.frc2026.subsystems.drive.DriveConstants;
import org.jmhsrobotics.frc2026.subsystems.drive.DriveConstants.revConstants;
import org.jmhsrobotics.frc2026.subsystems.drive.SparkOdometryThread;
import org.jmhsrobotics.frc2026.util.SparkUtil;

public class ModuleIORev implements ModuleIO{
    private final Rotation2d zeroRotation;

    //Hardware objects
    private final SparkBase driveSpark;
    private final SparkBase turnSpark;
    private final RelativeEncoder driveEncoder;
    private final AbsoluteEncoder turnEncoder;

    // Closed loop controllers
    private final SparkClosedLoopController driveController;
    private final SparkClosedLoopController turnController;

    // Queue inputs from odometry thread
    private final Queue<Double> timestampQueue;
    private final Queue<Double> drivePositionQueue;
    private final Queue<Double> turnPositionQueue;

    // Connection debouncers
    private final Debouncer driveConnectedDebounce = new Debouncer(0.5);
    private final Debouncer turnConnectedDebounce = new Debouncer(0.5);

    
    public ModuleIORev(int module){
        zeroRotation =
        switch (module) {
          case 0 -> DriveConstants.frontLeftZeroRotation;
          case 1 -> DriveConstants.frontRightZeroRotation;
          case 2 -> DriveConstants.backLeftZeroRotation;
          case 3 -> DriveConstants.backRightZeroRotation;
          default -> new Rotation2d();
        };
    driveSpark =
        new SparkMax(
            switch (module) {
              case 0 -> revConstants.frontLeftDriveCanId;
              case 1 -> revConstants.frontRightDriveCanId;
              case 2 -> revConstants.backLeftDriveCanId;
              case 3 -> revConstants.backRightDriveCanId;
              default -> 0;
            },
            MotorType.kBrushless);
    turnSpark =
        new SparkMax(
            switch (module) {
              case 0 -> revConstants.frontLeftTurnCanId;
              case 1 -> revConstants.frontRightTurnCanId;
              case 2 -> revConstants.backLeftTurnCanId;
              case 3 -> revConstants.backRightTurnCanId;
              default -> 0;
            },
            MotorType.kBrushless);
    driveEncoder = driveSpark.getEncoder();
    turnEncoder = turnSpark.getAbsoluteEncoder();
    driveController = driveSpark.getClosedLoopController();
    turnController = turnSpark.getClosedLoopController();

    //Configure drive motor
    var driveConfig = new SparkFlexConfig();
    driveConfig
        .idleMode(IdleMode.kBrake)
        .smartCurrentLimit(revConstants.driveMotorCurrentLimit)
        .voltageCompensation(12.0);
    driveConfig
        .closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pidf(
            revConstants.driveKp, 0.0,
            revConstants.driveKd, 0.0);
    driveConfig
        .signals
        .primaryEncoderPositionAlwaysOn(true)
        .primaryEncoderPositionPeriodMs((int) (1000.0 / DriveConstants.odometryFrequency))
        .primaryEncoderVelocityAlwaysOn(true)
        .primaryEncoderVelocityPeriodMs(20)
        .busVoltagePeriodMs(20)
        .outputCurrentPeriodMs(20);
    SparkUtil.tryUntilOk(
        driveSpark,
        5,
        () ->
            driveSpark.configure(
              driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters));
    SparkUtil.tryUntil


    // Create odometry queues
    timestampQueue = SparkOdometryThread.getInstance().makeTimestampQueue();
    drivePositionQueue =
        SparkOdometryThread.getInstance().registerSignal(driveSpark, driveEncoder::getPosition);
    turnPositionQueue =
        SparkOdometryThread.getInstance().registerSignal(turnSpark, turnEncoder::getPosition);
  }
}