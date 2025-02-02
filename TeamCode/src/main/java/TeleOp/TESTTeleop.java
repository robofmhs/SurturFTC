package TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import Subsystems.Arm;
import Subsystems.Claw;
import Subsystems.Hang;
import Subsystems.Pivot;
import Subsystems.Slides;
import Subsystems.Wrist;

@Config
@TeleOp
public class TESTTeleop extends LinearOpMode {
    private Arm arm;
    private Pivot pivot;
    private Slides slides;
    private Hang hang;
    private DcMotor fl, fr, bl, br;
    private Wrist wrist;
    private Claw gripper;
    double multipier=.35;
    private Gamepad g1;
    private Gamepad g2;
    private Gamepad prevg1;
    private Gamepad prevg2;

    //    private Servo arm10, arm20,wrist,gripper;
    public static double armposition = 0;
    public static double wristposition = 0;
    public static double gripperposition = 0;
//    private DcMotor pivot;
//    private Pivot pivot;

    @Override
    public void runOpMode() {
//        arm10 = hardwareMap.get(Servo.class, "arm1");
//        arm20 = hardwareMap.get(Servo.class, "arm2");
//        wrist = hardwareMap.get(Servo.class,"wrist");
//        gripper = hardwareMap.get(Servo.class,"claw");


        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);
        gripper = new Claw(hardwareMap);
        pivot = new Pivot(hardwareMap);
        slides = new Slides(hardwareMap);
        hang = new Hang(hardwareMap);
        hang.setPID(true);
        slides.setPID(true);
        pivot.setPID(true);

        fl = hardwareMap.get(DcMotor.class,"fl");
        fr = hardwareMap.get(DcMotor.class,"fr");
        bl = hardwareMap.get(DcMotor.class,"bl");
        br = hardwareMap.get(DcMotor.class,"br");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm.setPosition(.4);
        wrist.setPosition(.6);
        gripper.setPosition(0.35);
        arm.update();
        wrist.update();
        gripper.update();
        pivot.setTargetPosition(pivot.getCurrentPosition());
        slides.setTargetPosition(slides.getCurrentPosition());
        pivot.update();
        slides.update();
        g1 = new Gamepad();
        g2 = new Gamepad();

        prevg1 = new Gamepad();
        prevg2 = new Gamepad();


//        arm = new Arm(hardwareMap);
//            arm=new Arm(hardwareMap);
//            pivot = new Pivot(hardwareMap);
//            pivot.setPID(false);

        waitForStart();

        while(opModeIsActive()) {
            prevg1 = g1;
            prevg2 = g2;
            g1 = gamepad1;
            g2 = gamepad2;

            if (g2.x){
                pivot.setTargetPosition(1891);
                slides.setTargetPosition(7);
                arm.setPosition(.91);
                wrist.setPosition(.3499999);
                pivot.update();
                slides.update();
                arm.update();
                wrist.update();
            }





            pivot.setPID(true);
            pivot.setTargetPosition((int)(pivot.getTargetPosition()+100*gamepad2.left_stick_y));
            pivot.update();


            slides.setPID(true);
            slides.setTargetPosition((int)(slides.getTargetPosition()+25*(gamepad2.right_trigger-gamepad2.left_trigger)));
            slides.update();

            if (g1.left_bumper){
                multipier = .35;
            }
            else{;
                multipier = .8;
            }
            double y = -gamepad1.left_stick_y*multipier; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1*multipier; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x*multipier;

            if (g2.dpad_up) {
                wrist.setPosition(wrist.getPosition()+0.015);
            } else if (gamepad2.dpad_down) {
                wrist.setPosition(wrist.getPosition()-0.015);
            }

            arm.setPosition(arm.getPosition()+.01*gamepad2.right_stick_y);



            double flPower = y + x + rx;
            double frPower = y - x - rx;
            double blPower = y - x + rx;
            double brPower = y + x - rx;

            fl.setPower(flPower);
            fr.setPower(frPower);
            bl.setPower(blPower);
            br.setPower(brPower);

            if(g2.y){
                gripper.setPosition(0.35);
            } else if (g2.a) {
                gripper.setPosition(0);
            }


            arm.update();
            wrist.update();
            gripper.update();




//            armposition+=gamepad1.left_stick_y*.0012;
//            arm10.setPosition(armposition);
//            arm20.setPosition(armposition);
//            wrist.setPosition(wristposition);
//            gripper.setPosition(gripperposition);


//            pivot.setPID(false);
//            pivot.setControl(gamepad1.left_stick_y);
//            pivot.update();
//            arm.incrementPosition(gamepad1.right_stick_y);
//            arm.update();

//            telemetry.addData("arm: ", arm10.getPosition());
//            telemetry.addData("wrist: ",wrist.getPosition());
//            telemetry.addData("gripper: ",gripper.getPosition());
            telemetry.addData("pivot: ",pivot.getCurrentPosition());
            telemetry.addData("slides: ",slides.getCurrentPosition());
            telemetry.addData("slidesTarget: ",slides.getTargetPosition());
            telemetry.addData("arm: ",slides.getTargetPosition());
            telemetry.addData("wrist: ",slides.getTargetPosition());

            telemetry.addData("hang: ",hang.getCurrentPosition());
            telemetry.addData("game: ",g1.left_stick_y);
//            telemetry.addData("hell: ",armposition);
//            telemetry.addData("hell2: ",wristposition);
//            telemetry.addData("hell3: ",gripperposition);
            telemetry.update();

        }
    }
}
