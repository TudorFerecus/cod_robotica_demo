package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Hardware {

    /**
     * @param motor[0] - right front
     * @param motor[1] - left front
     * @param motor[2] - right back
     * @param motor[3] - left back
     */
    public boolean isAuto = false;
    public DcMotor[] motor = new DcMotor[4]; // motoarele rotilor

    public DcMotor mGlisLeft, mGlisRight, m180, mScissor;

    // servo uri
    public Servo servoClaw;

    // parametrii imu
    public BNO055IMU imu;
    public BNO055IMU.Parameters parameters;
    private HardwareMap hardwareMap = null;


    public ElapsedTime runTime = new ElapsedTime();

    public Hardware(HardwareMap hdMap, boolean auto) {
        this.isAuto = auto;
        initializare(hdMap);
    }

    private void initializare(HardwareMap hdMap) {
        this.hardwareMap = hdMap;

        initMotors();
        initServos();
        initImu();
    }

    private void initImu() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }

    private DcMotor setDefaultStateMotor(DcMotor motor, String nume, DcMotorSimple.Direction direction) {
        motor = hardwareMap.get(DcMotor.class, nume);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(direction);
        return motor;
    }

    public double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

    private void initMotors() {

        motor[0] = setDefaultStateMotor(motor[0], "mFrontRight", DcMotorSimple.Direction.REVERSE);
        motor[1] = setDefaultStateMotor(motor[1], "mFrontLeft", DcMotorSimple.Direction.REVERSE);
        motor[2] = setDefaultStateMotor(motor[2], "mBackRight", DcMotorSimple.Direction.FORWARD);
        motor[3] = setDefaultStateMotor(motor[3], "mBackLeft", DcMotorSimple.Direction.REVERSE);


        //motor brat
        mGlisLeft = setDefaultStateMotor(mGlisRight, "mGlisLeft", DcMotorSimple.Direction.REVERSE);
        mGlisRight = setDefaultStateMotor(mGlisLeft, "mGlisRight", DcMotorSimple.Direction.REVERSE);
        m180 = setDefaultStateMotor(m180, "m180", DcMotorSimple.Direction.REVERSE);
        mScissor = setDefaultStateMotor(mScissor, "mScissor", DcMotorSimple.Direction.FORWARD);

        m180.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mGlisLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mGlisRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mScissor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        if(isAuto)
        {
            mGlisLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mGlisRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            mGlisRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mGlisLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        else
        {

            mGlisRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mGlisLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m180.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mScissor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }


    }

    private void initServos() {
        servoClaw = hardwareMap.get(Servo.class, "servoClaw");
    }
}