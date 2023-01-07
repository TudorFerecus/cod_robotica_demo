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
     * @param motor[0] - right back
     * @param motor[1] - left back
     * @param motor[2] - right front
     * @param motor[3] - left front
     */
    public boolean isAuto = false;
    public DcMotor[] motor = new DcMotor[4]; // motoarele rotilor

    public DcMotor mGlisLeft, mGlisRight, mShoulder;

    public DcMotor mRotate;

    // servo uri
    public CRServo servoHand, servoArm;

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

        // motoare roti
        for (int i = 0; i < motor.length; i++) {
            String nume = "m" + Integer.toString(i);
            motor[i] = setDefaultStateMotor(motor[i], nume, DcMotorSimple.Direction.FORWARD);
        }

        //motor brat
        mGlisLeft = setDefaultStateMotor(mGlisRight, "mGlisLeft", DcMotorSimple.Direction.FORWARD);
        mGlisRight = setDefaultStateMotor(mGlisLeft, "mGlisRight", DcMotorSimple.Direction.REVERSE);
        mShoulder = setDefaultStateMotor(mShoulder, "mShoulder", DcMotorSimple.Direction.REVERSE);

        mShoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mGlisLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mGlisRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mRotate = hardwareMap.get(DcMotor.class, "mRotate");
        mRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if(isAuto)
        {
            mGlisLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mGlisRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            mRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mGlisRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mGlisLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        else
        {

            mRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mGlisRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mGlisLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }


    }

    private void initServos() {
        servoHand = hardwareMap.get(CRServo.class, "servoHand");
        servoArm = hardwareMap.get(CRServo.class, "servoArm");
    }
}