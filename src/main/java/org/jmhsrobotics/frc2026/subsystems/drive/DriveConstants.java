package org.jmhsrobotics.frc2026.subsystems.drive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class DriveConstants{
    public static final double maxSpeedMetersPerSec = 5.0;
    
    public static final double odometryFrequency = 100.0; // Hz

    public static final Rotation2d frontLeftZeroRotation = new Rotation2d(0.0);
    public static final Rotation2d frontRightZeroRotation = new Rotation2d(0.0);
    public static final Rotation2d backLeftZeroRotation = new Rotation2d(0.0);
    public static final Rotation2d backRightZeroRotation = new Rotation2d(0.0);

    public static class revConstants{
        public static final DCMotor driveGearbox = DCMotor.getNEO(1);  // will need to change this for implementation with others motor types (2.0s, vortexes, etc.)
        public static final DCMotor turnGearbox = DCMotor.getNeo550(1);

        public static final double trackWidth = Units.inchesToMeters(22.5); // might need to change ts, dory's contsants rn
        public static final double wheelBase = Units.inchesToMeters(22.5); 
        public static final double driveBaseRadius = Math.hypot(trackWidth / 2.0, wheelBase / 2.0);
        public static final Translation2d[] moduleTranslations = 
            new Translation2d[] {
                new Translation2d(trackWidth / 2.0, wheelBase / 2.0),
                new Translation2d(trackWidth / 2.0, -wheelBase / 2.0),
                new Translation2d(-trackWidth / 2.0, wheelBase / 2.0),
                new Translation2d(-trackWidth / 2.0, -wheelBase / 2.0)
            };

        public static final double maxAngularSpeedRadPerSec =
            DriveConstants.maxSpeedMetersPerSec / DriveConstants.revConstants.driveBaseRadius;
        
        // Zeroed rotation values for each module, see setup instructions
        
        // Device CAN IDs
        public static final int pigeonCanId = 62; // gyro

        public static final int frontLeftDriveCanId = 10;
        public static final int backLeftDriveCanId = 12;
        public static final int frontRightDriveCanId = 11;
        public static final int backRightDriveCanId = 13;

        public static final int frontLeftTurnCanId = 20;
        public static final int backLeftTurnCanId = 22;
        public static final int frontRightTurnCanId = 21;
        public static final int backRightTurnCanId = 23;

        //Drive motor configuration
        public static final int driveMotorCurrentLimit = 50;
        public static final double wheelRadiusMeters = Units.inchesToMeters(1.5);
        public static final double driveMotorReduction =
            (45.0 * 22.0) / (14.0 * 15.0); // MAXSwerve with 14 pinion teeth and 22 spur teeth

        // Drive encoder configuration
        public static final double driveEncoderPositionFactor =
            2* Math.PI / driveMotorReduction; // Rotor Rotations -> Wheel Radians

        // Drive PID configuration
        public static final double driveKp = 0.2;
        public static final double driveKd = 0.0;
        public static final double driveKs = 0.0;
        public static final double driveKv = 0.1;
        public static final double driveSimP = 0.05;
        public static final double driveSimD = 0.0;
        public static final double driveSimKs = 0.0;
        public static final double driveSimKv = 0.0789;
    }
}