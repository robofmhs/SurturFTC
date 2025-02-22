package Auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import Subsystems.Robot;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous
public class Auto4_0New extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private Robot robot;


    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */
    private double intialx=7.5,intialy=59;
    /** Start Pose of our robot */
    private final Pose startPose = new Pose(intialx, intialy, Math.toRadians(0));

    /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
    private final Pose scorePreLoadPose = new Pose(30+intialx, 10.78+intialy, Math.toRadians(0));

    private final Pose pickupControlPose = new Pose(20, 51, Math.toRadians(0));

    private final Pose middlePose = new Pose(30+intialx, -25+intialy, Math.toRadians(6));

    /** Lowest (First) Sample from the Spike Mark */
    private final Pose push1Pose = new Pose(48+intialx, -36.12+intialy, Math.toRadians(0));
    private final Pose push1ControlPose = new Pose(71, 37, Math.toRadians(0));

    /** Middle (Second) Sample from the Spike Mark */
    private final Pose push2Pose = new Pose(48+intialx, -46.01+intialy, Math.toRadians(0));
    private final Pose push2ControlPose = new Pose(71, 33, Math.toRadians(0));

    /** Highest (Third) Sample from the Spike Mark */
    private final Pose push3Pose = new Pose(48+intialx, -50.2854+intialy, Math.toRadians(0));
    private final Pose push3ControlPose = new Pose(65, 25, Math.toRadians(0));

    private final Pose dropOffPose = new Pose(12.966+intialx, -38.36+intialy, Math.toRadians(0));

    private final Pose dropOff2Pose = new Pose(10+intialx, -50.2854+intialy, Math.toRadians(0));

    private final Pose intakePose = new Pose(2+intialx, -33.874+intialy, Math.toRadians(0));

    private final Pose intake2Pose = new Pose(3+intialx, -50.2854+intialy, Math.toRadians(0));

    private final Pose intakeControlPose = new Pose(21, 53, Math.toRadians(0));

    private final Pose intakePausePose = new Pose(10+intialx, -33.874+intialy, Math.toRadians(0));

    private final Pose scoreControlPose = new Pose(11, 70, Math.toRadians(0));

    private final Pose scoreControl2Pose = new Pose(17, 58, Math.toRadians(0));

    private final Pose scorePose = new Pose(30.422+intialx, 7.83+intialy, Math.toRadians(0));


    /* These are our Paths and PathChains that we will define in buildPaths() */
    private PathChain scorePreload, grabPickup1, dragPickup1, grabPickup2, dragPickup2, grabPickup3, dragPickup3, intakeScooch2 ,intakeScooch, intakePickup,intakePickup2, scorePickup1, scorePickup2,park;

    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */

        /* Here is an example for Constant Interpolation
        scorePreload.setConstantInterpolation(startPose.getHeading()); */



        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePreLoadPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePreLoadPose.getHeading())
                .addParametricCallback(.8,() -> follower.setMaxPower(.7))
                .build();


        /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        grabPickup1 = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(scorePreLoadPose), /* Control Point */ new Point(pickupControlPose), new Point(middlePose)))
//                .setLinearHeadingInterpolation(scorePreLoadPose.getHeading(), middlePose.getHeading())
//                .addPath(new BezierCurve(new Point(middlePose), /* Control Point */ new Point(push1ControlPose), new Point(push1Pose)))
//                .setLinearHeadingInterpolation(middlePose.getHeading(), push1Pose.getHeading())
//                .addParametricCallback(.8,() -> follower.setMaxPower(.3))
//                .build();

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePreLoadPose), /* Control Point */ new Point(pickupControlPose), new Point(middlePose)))
                .setLinearHeadingInterpolation(scorePreLoadPose.getHeading(), middlePose.getHeading())
                .addPath(new BezierCurve(new Point(middlePose), /* Control Point */ new Point(push1ControlPose), new Point(push1Pose)))
                .setLinearHeadingInterpolation(middlePose.getHeading(), push1Pose.getHeading())
                .addParametricCallback(.0,() -> follower.setMaxPower(1))
                .addParametricCallback(.8,() -> follower.setMaxPower(.3))

                .addPath(new BezierLine(new Point(push1Pose), new Point(dropOffPose)))
                .setLinearHeadingInterpolation(push1Pose.getHeading(), dropOffPose.getHeading())
                .addParametricCallback(.0,() -> follower.setMaxPower(1))
                .addParametricCallback(.8,() -> follower.setMaxPower(.3))

                .addPath(new BezierCurve(new Point(dropOffPose), /* Control Point */ new Point(push2ControlPose) , new Point(push2Pose)))
                .setLinearHeadingInterpolation(dropOffPose.getHeading(), push2Pose.getHeading())
                .addParametricCallback(.0,() -> follower.setMaxPower(1))
                .addParametricCallback(.8,() -> follower.setMaxPower(.35))

                .addPath(new BezierLine(new Point(push2Pose), new Point(dropOffPose)))
                .setLinearHeadingInterpolation(push2Pose.getHeading(), dropOffPose.getHeading())
                .addParametricCallback(.0,() -> follower.setMaxPower(1))
                .addParametricCallback(.6,() -> follower.setMaxPower(.2))
                .build();

        dragPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(push1Pose), new Point(dropOffPose)))
                .setLinearHeadingInterpolation(push1Pose.getHeading(), dropOffPose.getHeading())
                .addParametricCallback(.8,() -> follower.setMaxPower(.3))
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(dropOffPose), /* Control Point */ new Point(push2ControlPose) , new Point(push2Pose)))
                .setLinearHeadingInterpolation(dropOffPose.getHeading(), push2Pose.getHeading())
                .addParametricCallback(.8,() -> follower.setMaxPower(.35))
                .build();

        dragPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(push2Pose), new Point(dropOffPose)))
                .setLinearHeadingInterpolation(push2Pose.getHeading(), dropOffPose.getHeading())
                .addParametricCallback(.6,() -> follower.setMaxPower(.2))
                .build();

//        grabPickup3 = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(dropOffPose), /* Control Point */ new Point(push3ControlPose) ,new Point(push3Pose)))
//                .setLinearHeadingInterpolation(dropOffPose.getHeading(), push3Pose.getHeading())
//                .build();
//
//        dragPickup3 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(push3Pose), new Point(dropOff2Pose)))
//                .setLinearHeadingInterpolation(push3Pose.getHeading(), dropOff2Pose.getHeading())
//                .build();


//        intakeScooch2 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(dropOff2Pose), new Point(intake2Pose)))
//                .setLinearHeadingInterpolation(dropOff2Pose.getHeading(), intake2Pose.getHeading())
//                .build();
        intakePickup = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(dropOffPose), new Point(intakePausePose)))
                .setLinearHeadingInterpolation(dropOffPose.getHeading(), intakePausePose.getHeading())
                .build();

        intakeScooch = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intakePausePose), new Point(intakePose)))
                .setLinearHeadingInterpolation(intakePausePose.getHeading(), intakePose.getHeading())
                .build();
// This code doesn't work? It gets 5 big booms. BOOM BOOM BOOM BOOM BOOM
        /* This is our scorePickup PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(intakePose), /* Control Point */ new Point(scoreControlPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(intakePose.getHeading(), scorePose.getHeading())
                .build();


        intakePickup2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(intakeControlPose), new Point(intakePausePose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intakePausePose.getHeading())
                .addParametricCallback(.7,()->follower.setMaxPower(.3))
                .build();

    }
    private double pow = .8;
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                robot.pivot.setTargetPosition(1114);
                robot.slides.setTargetPosition(190);
                robot.arm.setPosition(.4279);
                robot.wrist.setPosition(.69);
                robot.update();

                follower.followPath(scorePreload,true);
                setPathState(100);
                break;
            case 100:
                if (pathTimer.getElapsedTimeSeconds()>1){
                    setPathState(1);
                }
                break;
            case 1:

                /* You could check for
                - Follower State: "if(!follower.isBusy() {}"
                - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
                - Robot Position: "if(follower.getPose().getX() > 36) {}"
                */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Preload */
                    robot.pivot.setTargetPosition(1876);
                    robot.slides.setTargetPosition(0);
                    robot.arm.setPosition(.24527);
                    robot.wrist.setPosition(.045);
                    robot.claw.setPosition(0.6);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(grabPickup1,true);

                    setPathState(7);
                }
                break;
//            case 2:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(dragPickup1,1,true);
//                    setPathState(3);
//                }
//                break;
//            case 3:
//                if(!follower.isBusy()) {
//                    follower.followPath(grabPickup2,1,true);
//                    setPathState(4);
//                }
//                break;
//            case 4:
//                if(!follower.isBusy()) {
//                    follower.followPath(dragPickup2,1,true);
//                    setPathState(7);
//                }
//                break;
            case 7:
                if(!follower.isBusy()) {
                    follower.followPath(intakePickup,.6,true);
                    setPathState(8);
                }
                break;
            case 8:
                if(!follower.isBusy()){
                    follower.followPath(intakeScooch, .4,true);
                    setPathState(900);
                }
                break;
            case 900:
                if (!follower.isBusy()&&pathTimer.getElapsedTimeSeconds()>1){
                    robot.claw.setPosition(.4);
                    setPathState(9);
                }
                break;
            case 9:
                if(pathTimer.getElapsedTimeSeconds()>.5) {
                    robot.pivot.setTargetPosition(1114);
                    robot.slides.setTargetPosition(170);
                    robot.arm.setPosition(.4279);
                    robot.wrist.setPosition(.675);
                    robot.update();
                    follower.followPath(scorePickup1, 1,true); // score pickup 1
                    setPathState(10);
                }
                break;
            case 10:
                if(!follower.isBusy()) {
                    robot.pivot.setTargetPosition(1876);
                    robot.slides.setTargetPosition(0);
                    robot.arm.setPosition(.245);
                    robot.wrist.setPosition(.045);
//                    robot.claw.setPosition(0.6);
                    follower.followPath(intakePickup2,1, true); // intake pickup 2
                    setPathState(11);
                }
                break;
            case 11:
                if(!follower.isBusy()) {
                    robot.claw.setPosition(0.6);
                    follower.followPath(intakeScooch,.4, true); // intake pickup 2
                    setPathState(110);
                }
                break;
            case 110:
                if (!follower.isBusy()&&pathTimer.getElapsedTimeSeconds()>1){
                    robot.claw.setPosition(.4);
                    setPathState(12);
                }
                break;
            case 12:
                if(pathTimer.getElapsedTimeSeconds()>.5) {
                    robot.pivot.setTargetPosition(1114);
                    robot.slides.setTargetPosition(170);
                    robot.arm.setPosition(.4279);
                    robot.wrist.setPosition(.675);
                    robot.update();
                    follower.followPath(scorePickup1, 1,true); // score pickup 2
                    setPathState(13);
                }
                break;
            case 13:
                if(!follower.isBusy()) {
                    robot.pivot.setTargetPosition(1876);
                    robot.slides.setTargetPosition(0);
                    robot.arm.setPosition(.24527);
                    robot.wrist.setPosition(.045);
//                    robot.claw.setPosition(0.6);
                    follower.followPath(intakePickup2,1,true); // intake pickup 3
                    setPathState(14);
                }
                break;
            case 14:
                if(!follower.isBusy()) {
                    robot.claw.setPosition(0.6);
                    follower.followPath(intakeScooch,true); // score pickup 3
                    setPathState(140);
                }
                break;
            case 140:
                if (!follower.isBusy()&&pathTimer.getElapsedTimeSeconds()>1){
                    robot.claw.setPosition(.4);
                    setPathState(15);
                }
                break;
            case 15:
                if(pathTimer.getElapsedTimeSeconds()>.5) {
                    robot.pivot.setTargetPosition(1114);
                    robot.slides.setTargetPosition(170);
                    robot.arm.setPosition(.4279);
                    robot.wrist.setPosition(.675);
                    robot.update();
                    follower.followPath(scorePickup1, 1,true); // score pickup 2
                    setPathState(16);
                }
                break;
            case 16:
                if(!follower.isBusy()) {
                    robot.pivot.setTargetPosition(1876);
                    robot.slides.setTargetPosition(0);
                    robot.arm.setPosition(.24527);
                    robot.wrist.setPosition(.0);
                    follower.followPath(intakePickup2,true); // intake pickup
                    setPathState(17);
                }
                break;
            case 17:
                if(!follower.isBusy()) {
                    robot.claw.setPosition(.4);
                    follower.followPath(intakeScooch,true); // score pickup 4
                    setPathState(170);
                }
                break;
            case 170:
                if (!follower.isBusy()&&pathTimer.getElapsedTimeSeconds()>1){
                    robot.claw.setPosition(.4);
                    setPathState(18);
                }
                break;
            case 18:
                if(pathTimer.getElapsedTimeSeconds()>.5) {
                    robot.pivot.setTargetPosition(1114);
                    robot.slides.setTargetPosition(205);
                    robot.arm.setPosition(.4279);
                    robot.wrist.setPosition(.69);
                    robot.update();
                    follower.followPath(scorePickup1, 1,true); // score pickup 2
                    setPathState(19);
                }
                break;
            case 19:
                if(!follower.isBusy()) {
                    follower.followPath(park, true);
                    setPathState(-1);
                }
                break;



        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot
        follower.update();
        robot.update();

        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("slide: ",robot.slides.getCurrentPosition());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        robot = new Robot(hardwareMap);

        robot.pivot.setPID(true);
        robot.slides.setPID(true);
        robot.update();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {
        robot.pivot.setPID(true);
        robot.slides.setPID(true);
        robot.pivot.setTargetPosition(950);
        robot.slides.setTargetPosition(30);
        robot.arm.setPosition(.9);
        robot.wrist.setPosition(100);
        robot.claw.setPosition(.3);
        robot.update();
    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }




}
