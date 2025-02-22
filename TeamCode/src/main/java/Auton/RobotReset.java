package Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import Subsystems.Robot;

@Autonomous
public class RobotReset extends OpMode {
    Robot robot;
    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        robot.pivot.resetEncoder();
        robot.pivot.pivotCurrent =0;
        robot.pivot.pivotTarget =0;
        robot.hang.resetEncoder();
        robot.hang.hangCurrent =0;
        robot.hang.hangTarget =0;
        robot.slides.resetEncoder();
        robot.slides.slideCurrent =0;
        robot.slides.slideTarget =0;
        robot.arm.setPosition(0.4);
        robot.wrist.setPosition(0.6);
        robot.update();
    }
    @Override
    public void loop() {
        robot.arm.setPosition(0.4);
        robot.wrist.setPosition(0.6);

        robot.pivot.resetEncoder();
        robot.pivot.pivotCurrent =0;
        robot.pivot.pivotTarget =0;

        robot.hang.resetEncoder();
        robot.hang.hangCurrent =0;
        robot.hang.hangTarget =0;

        robot.slides.resetEncoder();
        robot.slides.slideCurrent =0;
        robot.slides.slideTarget =0;

        robot.update();
    }
    @Override
    public void stop() {}
}
