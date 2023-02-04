package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.WeakReferenceSet;

import org.firstinspires.ftc.teamcode.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.teamcode.Specifications;
import org.opencv.core.Mat;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Teleop", group="MainTeleop")
public class Teleop extends OpMode {

    private boolean clawState = false;

    private Hardware hardware;

    private ElapsedTime elapsedTime;
    private double lastTime;
    private boolean reset = false;

    @Override
    public void init() {

        hardware = new Hardware(hardwareMap, false);
        elapsedTime = new ElapsedTime();

    }

    @Override
    public void loop() {

        float forward = -gamepad1.left_stick_y; // forward movement
        float strafe = gamepad1.left_stick_x; // strafe movement
        float rotation = -gamepad1.right_stick_x; // rotate

        float mScissorInput = gamepad2.left_stick_x;
        float m180Input = gamepad2.right_stick_x;
        float upGlis = gamepad2.left_trigger;
        float downGlis = gamepad2.right_trigger;
        boolean claw = gamepad2.a;

        if(reset)
        {
            if(elapsedTime.seconds() > lastTime + 500)
            {
                reset = false;
                lastTime = elapsedTime.seconds();
            }
        }

        movement(forward, strafe, rotation);

        clawOperation(claw);

        scissorOperation(mScissorInput);

        m180operation(m180Input);

        glisOperation(downGlis, upGlis);


        //if(glis)
          //  runToPos(300);

        telemetry.addData("GlisLeftTickPos", hardware.mGlisLeft.getCurrentPosition());
        telemetry.addData("GlisRightTickPos", hardware.mGlisRight.getCurrentPosition());
        telemetry.addData("M180Pos", hardware.m180.getCurrentPosition());
        telemetry.addData("MScissorPos", hardware.mScissor.getCurrentPosition());telemetry.update();
        telemetry.addData("reset", reset);
        telemetry.update();

    }

    void clawOperation(boolean state) {
        if (state) {
            clawState = !clawState;
            if (clawState && !reset) {
                hardware.servoClaw.setPosition(0.5f);
            } else if(!reset){
                hardware.servoClaw.setPosition(0f);
            }
        }
    }

    void scissorOperation(float mScissorInput) {
        hardware.mScissor.setPower(mScissorInput * Specifications.scissor_speed_multiplier);
    }

    void m180operation(float m180Input)
    {
        if (m180Input != 0)
            hardware.m180.setPower(m180Input * Specifications.m180_speed_multiplier);
        else
            hardware.m180.setPower(0.0);
    }

    void glisOperation(float downGlis, float upGlis)
    {
        int rightMax = Math.abs(hardware.mGlisRight.getCurrentPosition()) < Specifications.upper_limit_glis_ticks? 1 : 0;
        int leftMax = Math.abs(hardware.mGlisRight.getCurrentPosition()) < Specifications.upper_limit_glis_ticks? 1 : 0;

        int rightMin = Math.abs(hardware.mGlisRight.getCurrentPosition()) > 0? 1 : 0;
        int leftMin = Math.abs(hardware.mGlisRight.getCurrentPosition()) > 0? 1 : 0;


        if (upGlis == 0 && downGlis == 0) {
            hardware.mGlisLeft.setPower(0);
            hardware.mGlisRight.setPower(0);

        } else if (upGlis != 0) {
            float power = Math.max(Specifications.glis_min_speed, Math.min(1, upGlis * Specifications.glis_speed_multiplier));
            hardware.mGlisLeft.setPower(power * leftMax);
            hardware.mGlisRight.setPower(-power * rightMax);

        }else {
            float power = -Math.max(Specifications.glis_min_speed, Math.min(1, downGlis * Specifications.glis_speed_multiplier));

            hardware.mGlisLeft.setPower(power * leftMin);
            hardware.mGlisRight.setPower(-power * rightMin);
        }
    }

    void glisOperationFree(boolean downGlis, boolean upGlis)
    {
        if (upGlis && downGlis) {
            hardware.mGlisLeft.setPower(0);
            hardware.mGlisRight.setPower(0);

        } else if (upGlis) {
            float power = Math.max(Specifications.glis_min_speed, Math.min(1, Specifications.glis_speed_multiplier));
            hardware.mGlisLeft.setPower(power);
            hardware.mGlisRight.setPower(-power);

        }else {
            float power = -Math.max(Specifications.glis_min_speed, Math.min(1, Specifications.glis_speed_multiplier));

            hardware.mGlisLeft.setPower(power);
            hardware.mGlisRight.setPower(-power);
        }
    }



    private void movement(float forward, float strafe, float rotation)
    {
        // power applied to the robot wheel by wheel
        double[] power = new double[4];
        rotation *= -1;
        power[0] = (-forward + strafe + rotation) * Specifications.moving_speed_teleop;   //+
        power[1] = (+forward + strafe + rotation) * Specifications.moving_speed_teleop;   //-
        power[2] = (-forward - strafe + rotation) * Specifications.moving_speed_teleop;   //-
        power[3] = (+forward - strafe + rotation) * Specifications.moving_speed_teleop;   //+

        // applying the power
        for(int i=0; i<4; i++)
        {
            hardware.motor[i].setPower(power[i]);
        }
    }

    void runToPos(int ticks)
    {
        hardware.mGlisLeft.setTargetPosition(ticks);
        hardware.mGlisRight.setTargetPosition(ticks);

        hardware.mGlisLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.mGlisRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        hardware.mGlisRight.setPower(0.5f);
        hardware.mGlisLeft.setPower(0.5f);


    }



}
