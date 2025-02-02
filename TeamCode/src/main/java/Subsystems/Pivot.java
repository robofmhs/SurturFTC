package Subsystems;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.util.InterpLUT;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

@Config
public class Pivot implements Subsystem {
    private DcMotor pivot;
//    private Slides slides;
    public static double kP = 0.001, kI = 0, kD = 0;
    public static double kF = 0.0005;
    private InterpLUT kfLUT;
    public static int lowerBound = 0, upperBound = 2600;
    private PIDController controller;
    public static int pivotTarget = 0;
    private int pivotCurrent = 0;
    private final double ticks_in_degree = 5281.1/360;
    public static boolean isPID=true;
    private double control;

    @Override
    public void update() {
    if(isPID){
        pivot.setPower(getPower(getPID()));}
    else{
        pivot.setPower(getPower(control));
    }
    pivotCurrent = pivot.getCurrentPosition();
    }

    public void setControl(double x){
        control=x;
    }

    public void setPID(boolean isPID) {
        this.isPID = isPID;
    }

    public Pivot(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(DcMotor.class, "pivot");
//        slides = new Slides(hardwareMap);
        controller = new PIDController(kP, kI, kD);
        kfLUT = new InterpLUT();
        kfLUT.add(0, 0.0005);
        kfLUT.add(180, 1);
//        kfLUT.add(180, 1);
//        kfLUT.add(180, 1);
//        kfLUT.add(180, 1);
//        kfLUT.add(180, 1);
        kfLUT.createLUT();
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setDirection(DcMotor.Direction.REVERSE);

    }

    public double getPID() {
        pivotCurrent = pivot.getCurrentPosition();
        pivotTarget = Range.clip(pivotTarget, lowerBound, upperBound);

        controller.setPID(kP,kI,kD);

        return controller.calculate(pivotCurrent, pivotTarget);
    }

    public double getFF() {
//        kF = kfLUT.get(slides.getCurrentPosition());
        double angle = (pivotCurrent / ticks_in_degree);
        return kF * Math.cos(Math.toRadians(angle));
    }

    public void resetEncoder() {
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public double getPower(double drive) {
        return drive + getFF();
    }

    public int getCurrentPosition() {
        return pivot.getCurrentPosition();
    }

    public int getTargetPosition() {
        return pivotTarget;
    }
    public void setTargetPosition(int position) {
        pivotTarget = Range.clip(position, lowerBound, upperBound);
    }




}
