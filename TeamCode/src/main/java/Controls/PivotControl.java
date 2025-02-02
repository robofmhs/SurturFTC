package Controls;

import com.qualcomm.robotcore.hardware.Gamepad;

import Subsystems.Arm;
import Subsystems.Claw;
import Subsystems.Hang;
import Subsystems.Pivot;
import Subsystems.Robot;
import Subsystems.Slides;
import Subsystems.Wrist;

public class PivotControl {
    private Robot robot;
    private Gamepad g1;
    private Gamepad g2;
    boolean slow;
    double multiplier = .35;

    private Gamepad prevg1;
    private Gamepad prevg2;
    private boolean clawToggle;
    private int rollerToggle;
    public PivotControl(Robot robot){
        this.robot = robot;

        g1 = new Gamepad();
        g2 = new Gamepad();

        prevg1 = new Gamepad();
        prevg2 = new Gamepad();
    }
    public void update(Gamepad g1a, Gamepad g2a) {
        prevg1.copy(g1);
        prevg2.copy(g2);
        g1.copy(g1a);
        g2.copy(g2a);




        double slidetrig = -g2.left_trigger-g2.right_trigger;

        if(g2.left_bumper){
            multiplier=.35;
        }
        else{
            multiplier=1;
        }

        robot.pivot.setPID(true);
        robot.pivot.setTargetPosition((int)(robot.pivot.getTargetPosition()+100*-g2.left_stick_y*multiplier));


        robot.slides.setPID(true);
        robot.slides.setTargetPosition((int)(robot.slides.getTargetPosition()+50*slidetrig*multiplier));

        robot.arm.incrementPosition(g2.right_stick_y);





        robot.arm.incrementPosition(g2.right_stick_y*.00035);

        if(g2.dpad_up){
            robot.wrist.incrementPosition(.0015);
        }
        else if(g2.dpad_down){
            robot.wrist.incrementPosition(-.0015);
        }
        else{
            robot.wrist.incrementPosition(0);
        }




        if(g2.a&&!prevg2.a){
            clawToggle = !clawToggle;
        }
        if(clawToggle){
            robot.claw.setPosition(1);
        }
        else {
            robot.claw.setPosition(0);
        }

        if(g2.x&&!prevg2.x){
            robot.pivot.setTargetPosition(2600);
            robot.slides.setTargetPosition(34);
            robot.arm.setPosition(.3);
            robot.wrist.setPosition(.6);
            robot.claw.setPosition(0);
        }

        if(g2.y&&!prevg2.y){
            robot.pivot.setTargetPosition(2600);
            robot.slides.setTargetPosition(10);
            robot.arm.setPosition(.5);
            robot.wrist.setPosition(.4);
        }

        if(g2.b&&!prevg2.b){
            robot.pivot.setTargetPosition(0);
            robot.slides.setTargetPosition(200);
            robot.arm.setPosition(.4);
            robot.wrist.setPosition(.6);
        }

//        if(g2.x&&!prevg2.x){
//            rollerToggle = (rollerToggle+1)%3;
//        }
//        if(rollerToggle==0){
//            robot.claw.setPower(0);
//        }
//        else if(rollerToggle==1){
//            robot.claw.setPower(1);
//        }
//        else {
//            robot.claw.setPower(-1);
//        }
        robot.update();



    }
}
