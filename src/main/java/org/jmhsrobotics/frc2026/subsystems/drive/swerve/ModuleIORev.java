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
    private final RelativeEncoder driveEcoder;
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
    }


}