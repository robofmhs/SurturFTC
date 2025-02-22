package Controls;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import Subsystems.Arm;
import Subsystems.Claw;
import Subsystems.Hang;
import Subsystems.Pivot;
import Subsystems.Robot;
import Subsystems.Slides;
import Subsystems.Wrist;

@Config
public class PivotControl {
    private Robot robot;
    private Gamepad g1;
    private Gamepad g2;
    boolean slow;
    double multiplier = .35;
    public static int incre=50;


    private Gamepad prevg1;
    private Gamepad prevg2;
    private boolean clawToggle;
    private boolean rollerToggle;
    private boolean exitBool1;
    private boolean exitBool2;

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




        double slidetrig = -(g2.left_trigger-g2.right_trigger);
        double slidetrig2 = g1.left_trigger-g1.right_trigger;

        if(g2.left_bumper){
            multiplier=.35;
        }
        else{
            multiplier=1;
        }
        if(g1.dpad_up&&!prevg1.dpad_up){
            robot.hang.setPID(true);
            robot.hang.setTargetPosition(5820);
        } else if (g1.dpad_down&&!prevg1.dpad_down) {
            robot.hang.setPID(true);
            robot.hang.setTargetPosition(-200);
        }
        if (g1.dpad_left){
            incre=5;
        }else {
            incre = 50;
        }

        robot.pivot.setPID(true);
        robot.pivot.setTargetPosition((int)(robot.pivot.getTargetPosition()+100*-g2.left_stick_y*multiplier));

        robot.hang.setPID(true);
        robot.hang.setTargetPosition((int)(robot.hang.getTargetPosition()+incre*slidetrig2*multiplier));


        robot.slides.setPID(true);
        robot.slides.setTargetPosition((int)(robot.slides.getTargetPosition()+50*slidetrig*multiplier));
//        robot.slides.setControl(slidetrig);
        robot.arm.incrementPosition(g2.right_stick_y*.015);





        robot.arm.incrementPosition(g2.right_stick_y*.00035);

        if(g2.dpad_up){
            robot.wrist.incrementPosition(-.015);
        }
        else if(g2.dpad_down){
            robot.wrist.incrementPosition(.015);
        }
        else{
            robot.wrist.incrementPosition(0);
        }




        if(g2.a&&!prevg2.a){
            clawToggle = !clawToggle;
        }
        if(clawToggle){
            robot.claw.setPosition(.4);//clos
        }
        else {
            robot.claw.setPosition(.55);//open
        }

        if(g2.dpad_left&&!prevg2.dpad_left){
            rollerToggle=!rollerToggle;
        }
        if(rollerToggle){
            robot.claw.setPower(0);
        }
        else {
            if(robot.claw.getPosition()==.6){
            robot.claw.setPower(1);}
            else{
                robot.claw.setPower(0);
            }
        }

        if(g2.x&&!prevg2.x){
            robot.slides.setPID(true);
            robot.pivot.setTargetPosition(1876);
            robot.slides.setTargetPosition(0);
            robot.arm.setPosition(.245);
            robot.wrist.setPosition(.045);
            robot.claw.setPosition(0.6);
            clawToggle=false;
            rollerToggle=false;
        }
        if (g2.right_bumper&&!prevg2.right_bumper){
            robot.slides.setPID(true);
            robot.pivot.setTargetPosition(100);
            robot.slides.setTargetPosition(-2);
            robot.arm.setPosition(.7141);
            robot.wrist.setPosition(.615);
        }


        if(exitBool2){
            if(robot.pivot.getCurrentPosition()>1100) {
                robot.slides.setTargetPosition(1100);
            }
            exitBool2=false;
        }
        if(g2.y&&!prevg2.y){
            robot.slides.setPID(true);
            robot.pivot.setTargetPosition(1610);
            exitBool2=true;
            robot.arm.setPosition(.5);
            robot.wrist.setPosition(.045);
        }

        if(exitBool1){
            if(robot.slides.getCurrentPosition()<600){
                robot.pivot.setTargetPosition(0);
                robot.slides.setTargetPosition(100);
            }
            exitBool1=false;
        }
        //intake sub
        if(g2.b&&!prevg2.b&&!g2.start){
            robot.slides.setPID(true);
            robot.slides.setTargetPosition(500);
            exitBool1=true;
            robot.arm.setPosition(.487);
            robot.wrist.setPosition(.465);
        }



        // deposit spec
        if (g2.back&&!prevg2.back){
            robot.arm.setPosition(.4279);
            robot.wrist.setPosition(.705);
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
