package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Robot implements Subsystem{
    private TouchSensor touchSensor;
    public Slides slides;
    public Pivot pivot;
    public Claw claw;
    public Arm arm;
    public Wrist wrist;
    public Hang hang;
    public boolean PrevTouchState;
    public boolean CurrentTouchState;
    public Robot(HardwareMap hardwareMap) {
        slides = new Slides(hardwareMap);
        pivot = new Pivot(hardwareMap);
        claw = new Claw(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);
        hang = new Hang(hardwareMap);
        touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");
    }
    @Override
    public void update() {
        PrevTouchState = CurrentTouchState;
        CurrentTouchState = touchSensor.isPressed();

        if(CurrentTouchState && !PrevTouchState){
            pivot.resetEncoder();
        }


        slides.update();
        pivot.update();
        claw.update();
        arm.update();
        wrist.update();
        hang.update();
    }

}
