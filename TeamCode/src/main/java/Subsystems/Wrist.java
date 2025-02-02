package Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Config
public class Wrist implements Subsystem{
    private Servo wrist;
    public double lowerBound = 0, upperBound = 1;
    public static double wristPosition = 0;

    public Wrist(HardwareMap hardwareMap) {
        wrist = hardwareMap.get(Servo.class, "wrist");
    }

    @Override
    public void update() {
        wrist.setPosition(wristPosition);
        wristPosition= Range.clip(wrist.getPosition(), lowerBound, upperBound);
    }
    public void setPosition(double position) {
        wristPosition = Range.clip(position, lowerBound, upperBound);
    }
    public double getPosition() {
        return wristPosition;
    }
    public void incrementPosition(double increment) {
        setPosition(getPosition() + increment);
    }


}
