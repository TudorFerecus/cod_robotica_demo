package com.example.meepmeeptest;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTest {
    public static void main(String[] args)
    {
        MeepMeep mm = new MeepMeep(800)
                .setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setTheme(new ColorSchemeRedDark())
                .setBackgroundAlpha(1f);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(mm)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(30, 52.48291908330528, Math.toRadians(180),  Math.toRadians(180), 10)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-34, -63f, Math.toRadians(90)))
                                .forward(2)
                                .lineToLinearHeading(new Pose2d(-60, -61, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-60, -35, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-53, -33, Math.toRadians(60)))
                                .waitSeconds(1.5f)
                                .lineToLinearHeading(new Pose2d(-63, -33, Math.toRadians(60)))
                                .lineToLinearHeading(new Pose2d(-56.09, -12, Math.toRadians(180)))
                                .waitSeconds(1.5f)
                                .build()

                );

        mm.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}