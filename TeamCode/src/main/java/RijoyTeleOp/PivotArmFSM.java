package RijoyTeleOp;

import com.sfdev.assembly.state.*;
import Subsystems.Pivot;

public class PivotArmFSM {
    public enum PivotState {
        INIT,
        INTAKE,
        HPTAKE,
        CLIP,
        BASKET,
        DRIVERCONTROL
    }

    public static StateMachine getPivotMachine(Pivot arm) {
        return new StateMachineBuilder()
                .state(PivotState.INIT)
                .onEnter( () -> {
                    arm.setTargetPosition(20);//set to acc position
                } )

                .state(PivotState.INTAKE)
                .onEnter( () -> {
                    arm.setTargetPosition(20);//set to acc position
                } )

                .state(PivotState.HPTAKE)
                .onEnter( () -> {
                    arm.setTargetPosition(20);//set to acc position
                } )

                .state(PivotState.CLIP)
                .onEnter( () -> {
                    arm.setTargetPosition(20);//set to acc position
                } )

                .state(PivotState.BASKET)
                .onEnter( () -> {
                    arm.setTargetPosition(20);//set to acc position
                } )

                .state(PivotState.DRIVERCONTROL)
                .onEnter( () -> {
                    arm.setTargetPosition(20);//set to acc position
                } )

                .build();
    }
}
