package Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import java.util.Base64;

@Config
public class Slides implements Subsystem {
    private DcMotor slide1;
    private DcMotor slide2;
    private DcMotorEx slideEncoder;
    private Pivot pivot;
    public static double kP = 0.027, kI = 0.0002, kD = 0.0005;
    public static double kF = 0.0002;

    public static int lowerBound = 0, upperBound = 1100;
    private PIDController controller;
    public static int slideTarget = 0;
    public int slideCurrent = 0;
    private final double ticks_in_degree = 5281.1/360;
    private double control;
    public static boolean isPID=true;

    public Slides(HardwareMap hardwareMap) {
        slide1 = hardwareMap.get(DcMotor.class, "slide1");
        slide2 = hardwareMap.get(DcMotor.class, "slide2");
//        slideEncoder = hardwareMap.get(DcMotorEx.class, "br");
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide1.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.REVERSE);
        pivot = new Pivot(hardwareMap);
        controller = new PIDController(kP, kI, kD);
    }

    @Override
    public void update() {
        if(isPID) {
            slide1.setPower(getPower(getPID()));
            slide2.setPower(getPower(getPID()));
        }
        else {
            slide1.setPower(getPower(control));
            slide2.setPower(getPower(control));

        }
        slideCurrent= slide1.getCurrentPosition();
        pivot.update();
    }

    public void setControl(double x){
        control=x;
    }
    public void setPID(boolean isPID) {
        this.isPID = isPID;
    }
    public double getPID(){
        slideCurrent= slide1.getCurrentPosition();
        slideTarget= Range.clip(slideTarget, lowerBound, upperBound);
        controller.setPID(kP,kI,kD);
        return controller.calculate(slideCurrent, slideTarget);
    }
    public double getFF(){
        double angle = (pivot.getCurrentPosition()/ticks_in_degree);
        return kF * Math.sin(Math.toRadians(angle));
    }
    public void resetEncoder() {
        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public double getPower(double drive) {
        return drive+getFF();
    }
    public int getCurrentPosition() {
        return slide1.getCurrentPosition();
    }
    public int getTargetPosition() {
        return slideTarget;
    }
    public void setTargetPosition(int position) {
        if(pivot.getCurrentPosition()>=1000) {
            slideTarget = Range.clip(position, lowerBound, upperBound);
        }
        else{
            slideTarget = Range.clip(position, lowerBound, 200);
        }
    }


}
