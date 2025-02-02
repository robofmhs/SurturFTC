package Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Config
public class Claw implements Subsystem{
    private Servo claw;
//    private CRServo roller1;
//    private CRServo roller2;
    public double lowerBound = 0, upperBound = .35;
    public static double clawPosition = 0;
//    public static double roller1Power = 0;
//    public static double roller2Power= 0;

    public Claw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
//        roller1 = hardwareMap.get(CRServo.class, "roller1");
//        roller2 = hardwareMap.get(CRServo.class, "roller2");
    }
    @Override
    public void update() {
        claw.setPosition(clawPosition);
//        roller1.setPower(roller1Power);
//        roller2.setPower(roller2Power);
        clawPosition= Range.clip(claw.getPosition(), lowerBound, upperBound);
    }
    public void setPosition(double position) {
        clawPosition = Range.clip(position, lowerBound, upperBound);
    }
    public double getPosition() {
        return clawPosition;
    }
    public void incrementPosition(double increment) {
        setPosition(getPosition() + increment);
    }
//    public void setPower(double power) {
//        roller1Power = power;
//        roller2Power = power;
//    }
//    public void setPower(double power1, double power2) {
//        roller1Power = power1;
//        roller2Power = power2;
//    }






}
