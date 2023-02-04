package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TestIntake", group="Test")
public class TestClass extends OpMode {

    private DcMotor mScissor;
    private DcMotor m180;
    private DcMotor mGlisLeft;
    private DcMotor mGlisRight;
    private Servo servoClaw;

    private boolean clawState = false;



    @Override
    public void init() {
        mScissor = hardwareMap.get(DcMotor.class, "mScissor");
        m180 = hardwareMap.get(DcMotor.class, "m180");
        mGlisLeft = hardwareMap.get(DcMotor.class, "mGlisLeft");
        mGlisRight = hardwareMap.get(DcMotor.class, "mGlisRight");
        servoClaw = hardwareMap.get(Servo.class, "servoClaw");

        mScissor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mScissor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mGlisLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mGlisRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        m180.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        m180.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mGlisLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mGlisRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        mGlisRight.setDirection(DcMotorSimple.Direction.REVERSE);



    }

    @Override
    public void loop() {
        float mScissorInput = gamepad1.left_stick_x;
        float m180Input = gamepad1.right_stick_x;
        float upGlis = gamepad1.left_trigger;
        float downGlis = gamepad1.right_trigger;
        boolean claw = gamepad1.left_bumper;

        if(claw)
        {
            clawState = !clawState;
            if(clawState)
            {
                servoClaw.setPosition(1f);
            }
            else
            {
                servoClaw.setPosition(0f);
            }
        }

        mScissor.setPower(mScissorInput * 0.5f);

        if(m180Input != 0)
            m180.setPower(m180Input * 0.4f);
        else
            m180.setPower(0.01);

        if(upGlis == 0 && downGlis == 0)
        {
            mGlisLeft.setPower(0);
            mGlisRight.setPower(0);

        }
        else if(upGlis != 0)
        {
            float power = Math.max(0.2f, Math.min(1, upGlis * 0.5f));
            mGlisLeft.setPower(power);
            mGlisRight.setPower(power);

        }
        else
        {
            float power = -Math.max(0.2f, Math.min(1, downGlis * 0.5f));

            mGlisLeft.setPower(power);
            mGlisRight.setPower(power);
        }

        telemetry.addData("Claw State", clawState);

    }
}
