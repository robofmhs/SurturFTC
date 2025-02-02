package Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

@Config
public class Hang implements Subsystem{
    public static double kP = 0, kI = 0, kD = 0;
    public static double kF=0;
    public static int lowerBound = 0, upperBound = 1000;
    private PIDController controller;
    public static int hangTarget = 0;
    private int hangCurrent = 0;
    private final double ticks_in_degree = 5281.1/360;


    private DcMotor hang;
    private boolean isPID;
    private double hangControl;

    public Hang(HardwareMap hardwareMap) {
        this.hang = hardwareMap.get(DcMotor.class, "hang");
        controller = new PIDController(kP, kI, kD);
        hang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hang.setDirection(DcMotor.Direction.REVERSE);

    }

    public void setPID(boolean isPID) {
        this.isPID = isPID;
    }
    public void setControl(double x){
        hangControl=x;
    }

    public double getPID(){
        hangCurrent=hang.getCurrentPosition();
        hangTarget=Range.clip(hangTarget, lowerBound, upperBound);
        controller.setPID(kP,kI,kD);

        return controller.calculate(hangCurrent, hangTarget);
    }

    public double getFF(){
        double angle = (hangCurrent/ticks_in_degree);
        return kF * Math.cos(Math.toRadians(angle));
    }


    public void resetEncoder() {
        hang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public double getPower(double drive) {
        return drive+getFF();
    }
    public int getCurrentPosition() {
        return hang.getCurrentPosition();
    }
    public int getTargetPosition() {
        return hangTarget;
    }
    public void setTargetPosition(int position) {
        hangTarget= Range.clip(position, lowerBound, upperBound);
    }


    @Override
    public void update() {
        if(isPID) {
            hang.setPower(getPower(getPID()));
        }
        else {
            hang.setPower(getPower(hangControl));}
        hangCurrent=hang.getCurrentPosition();
    }
}
