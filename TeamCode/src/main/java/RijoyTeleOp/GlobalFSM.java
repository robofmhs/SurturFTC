package RijoyTeleOp;

import com.sfdev.assembly.state.*;
import Subsystems.Robot;

public class GlobalFSM {
    public static StateMachine claw;
    public static StateMachine pivot;
    public static StateMachine slide;
    public static Robot robot;

    public GlobalFSM(Robot robot){
        this.robot=robot;
        claw = ClawFSM.getClawMachine(robot.claw);
        pivot = PivotArmFSM.getPivotMachine(robot.pivot);
        slide = SlideFSM.getSlideMachine(robot.slides);
    }

    public enum GlobalState {
        INIT,
        INTAKE,
        HPTAKE,
        CLIP,
        BASKET,
        HANG
    }

    public static StateMachine getGlobalMachine() {
        return new StateMachineBuilder()
                .state(GlobalState.INIT)
                .onEnter( () -> {
                    claw.setState(ClawFSM.ClawState.CLOSE);
                    pivot.setState(PivotArmFSM.PivotState.INIT);
                    slide.setState(SlideFSM.SlideState.INIT);
                })

                .state(GlobalState.INTAKE)
                .onEnter( () -> {
                    claw.setState(ClawFSM.ClawState.OPEN_SPIN);
                    pivot.setState(PivotArmFSM.PivotState.INTAKE);
                    slide.setState(SlideFSM.SlideState.INTAKE);
                })

                .state(GlobalState.HPTAKE)
                .onEnter( () -> {
                    claw.setState(ClawFSM.ClawState.OPEN_SPIN);
                    pivot.setState(PivotArmFSM.PivotState.HPTAKE);
                    slide.setState(SlideFSM.SlideState.HPTAKE);
                })

                .state(GlobalState.CLIP)
                .onEnter( () -> {
                    claw.setState(ClawFSM.ClawState.CLOSE);
                    pivot.setState(PivotArmFSM.PivotState.CLIP);
                    slide.setState(SlideFSM.SlideState.PRECLIP);
                })
                .state(GlobalState.BASKET)
                .onEnter( () -> {
                    claw.setState(ClawFSM.ClawState.OPEN_IDLE);
                    pivot.setState(PivotArmFSM.PivotState.BASKET);
                    slide.setState(SlideFSM.SlideState.POSTCLIP);
                })

                .build();
    }
}
