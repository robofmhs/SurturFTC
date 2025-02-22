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
@Config
public class RC_Base extends LinearOpMode {


    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);
    double multipier=.35;
    public static double wristPos=.5;
    public boolean roce = true;

    public static double armPos=.5;

    @Override
    public void runOpMode() throws InterruptedException {


        Robot robot = new Robot(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        PivotControl pivotControl = new PivotControl(robot);
        Subsystem[] subsystems = new Subsystem[]{robot};


        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
//        Servo wrist = hardwareMap.servo.get("wrist");

        robot.wrist.setPosition(wristPos);
        robot.arm.setPosition(armPos);
        robot.pivot.setPID(true);
        robot.slides.setPID(false);
        robot.hang.setPID(true);

        robot.pivot.setTargetPosition(robot.pivot.getCurrentPosition());

//        robot.slides.setTargetPosition(robot.slides.getCurrentPosition());

        robot.hang.setTargetPosition(robot.hang.getCurrentPosition());

    robot.update();
        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.



//        robot.wrist.setWristPos(robot.wrist.getWristPos());
//        robot.OuttakeGripper.setPosition(.55);
//        robot.pivotSlides.togglePID(false);
//        robot.hang.setArmTarget(robot.hang.getArmPos() );
        waitForStart();
        follower.startTeleopDrive();
        while(opModeIsActive()) {
            for(Subsystem system : subsystems) system.update();
            pivotControl.update(gamepad1, gamepad2);
            if(gamepad1.left_bumper){
                multipier=.35;
            }
            else{
                multipier=1;
            }



            double y = -gamepad1.left_stick_y*multipier; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x * 1.1*multipier; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x*multipier;

            follower.setTeleOpMovementVectors(y, x, rx, roce);
            follower.update();


            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]

            telemetry.addData("pivot: ",robot.pivot.getCurrentPosition());
            telemetry.addData("slides: ",robot.slides.getCurrentPosition());
            telemetry.addData("slidesTarget: ",robot.slides.getTargetPosition());
            telemetry.addData("hang: ",robot.hang.getCurrentPosition());
            telemetry.addData("hangTarget: ",robot.hang.getTargetPosition());
            telemetry.addData("wrist: ",robot.wrist.getPosition());
            telemetry.addData("arm: ",robot.arm.getPosition());
            telemetry.addData("game: ",gamepad1.left_stick_y);

            telemetry.update();



        }
    }
}
