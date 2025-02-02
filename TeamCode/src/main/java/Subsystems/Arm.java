package Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

@Config
public class Arm implements Subsystem {
    private Servo arm1;
    private Servo arm2;
    public static double lowerBound = .04, upperBound = 1;
    public static double armPosition = 0;

    public Arm(HardwareMap hardwareMap) {
        arm1 = hardwareMap.get(Servo.class, "arm1");
        arm2 = hardwareMap.get(Servo.class, "arm2");


    }
    @Override
    public void update() {
        arm1.setPosition(armPosition);
        arm2.setPosition(armPosition);
        armPosition=Range.clip(arm1.getPosition(), lowerBound, upperBound);
    }
    public void setPosition(double position) {
        armPosition = Range.clip(position, lowerBound, upperBound);
    }
    public double getPosition() {
        return armPosition;
    }
    public void incrementPosition(double increment) {
        setPosition(getPosition() + increment);
    }


}
