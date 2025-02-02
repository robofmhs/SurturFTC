package TeleOp;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import Controls.PivotControl;
import Subsystems.Pivot;
import Subsystems.Robot;
import Subsystems.Subsystem;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@TeleOp
public class RC_Base extends LinearOpMode {

//    private DcMotorEx leftFront;
//    private DcMotorEx leftRear;
//    private DcMotorEx rightFront;
//    private DcMotorEx rightRear;
    double multipier=.35;

    @Override
    public void runOpMode() throws InterruptedException {


        Robot robot = new Robot(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        PivotControl pivotControl = new PivotControl(robot);
        Subsystem[] subsystems = new Subsystem[]{robot};

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("fl");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("bl");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("fr");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("br");

        Servo wrist = hardwareMap.servo.get("wrist");

        robot.pivot.setPID(true);
        robot.pivot.setTargetPosition(2000);


        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


//        robot.wrist.setWristPos(robot.wrist.getWristPos());
//        robot.OuttakeGripper.setPosition(.55);
//        robot.pivotSlides.togglePID(false);
//        robot.hang.setArmTarget(robot.hang.getArmPos() );
        waitForStart();

        while(opModeIsActive()) {
            for(Subsystem system : subsystems) system.update();
            pivotControl.update(gamepad1, gamepad2);


            if (gamepad2.dpad_up) {
                wrist.setPosition(wrist.getPosition()+0.005);
            } else if (gamepad2.dpad_down) {
                wrist.setPosition(wrist.getPosition()-0.005);

            }
            if (gamepad1.left_bumper){
                multipier = .35;
            }
            else{;
                multipier = .8;
            }
            double y = gamepad1.left_stick_y*multipier; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x * 1.1*multipier; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x*multipier;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
            telemetry.addData("pivot: ",robot.pivot.getCurrentPosition());
            telemetry.addData("slides: ",robot.slides.getCurrentPosition());
            telemetry.addData("slidesTarget: ",robot.slides.getTargetPosition());
            telemetry.addData("hang: ",robot.hang.getCurrentPosition());
            telemetry.addData("game: ",gamepad1.left_stick_y);

            telemetry.update();



        }
    }
}
