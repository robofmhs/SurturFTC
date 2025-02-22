package TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
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

    private DcMotor fl, fr, bl, br;
    private Servo arm10, arm20,wrist1,gripper;
    private CRServo roller1,roller2;
    public static double armposition = .5;
    public static double wristposition = .7;
    public static double gripperposition = .5;
    public static double rollerPower = 0;
    public static String hi = "2";
//    private DcMotor pivot;
//    private Pivot pivot;

    @Override
    public void runOpMode() {
        arm10 = hardwareMap.get(Servo.class, "arm1");
        arm20 = hardwareMap.get(Servo.class, "arm2");
        wrist1 = hardwareMap.get(Servo.class,"wrist");
        gripper = hardwareMap.get(Servo.class,"claw");

        roller1=hardwareMap.get(CRServo.class,"roller1");
        roller2=hardwareMap.get(CRServo.class,"roller"+hi);




        fl = hardwareMap.get(DcMotor.class,"fl");
        fr = hardwareMap.get(DcMotor.class,"fr");
        bl = hardwareMap.get(DcMotor.class,"bl");
        br = hardwareMap.get(DcMotor.class,"br");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        roller2.setDirection(CRServo.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());



//        arm = new Arm(hardwareMap);
//            arm=new Arm(hardwareMap);
//            pivot = new Pivot(hardwareMap);
//            pivot.setPID(false);

        waitForStart();

        while(opModeIsActive()) {
            arm10.setPosition(armposition);
            arm20.setPosition(armposition);
            wrist1.setPosition(wristposition);
            roller2.setPower(rollerPower);
            roller1.setPower(rollerPower);
            gripper.setPosition(gripperposition);


            telemetry.addData("arm1: ",arm10.getPosition());
            telemetry.addData("arm2: ",arm20.getPosition());
            telemetry.addData("armpos: ",armposition);
            telemetry.addLine("");
            telemetry.addData("wristData: ",wrist1.getPosition());
            telemetry.addData("wristpos: ",wristposition);

            telemetry.update();

        }
    }
}
