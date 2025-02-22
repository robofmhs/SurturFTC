package RijoyTeleOp;

import com.sfdev.assembly.state.*;
import Subsystems.Slides;

public class SlideFSM {
    public enum SlideState {
        INIT,
        INTAKE,
        HPTAKE,
        PRECLIP,
        POSTCLIP,
        BASKET,
        DRIVERCONTROL
    }

    public static StateMachine getSlideMachine(Slides extend) {
        return new StateMachineBuilder()
                .state(SlideState.INIT)
                .onEnter( () -> {
                    extend.setTargetPosition(20);//set to acc position
                } )

                .state(SlideState.INTAKE)
                .onEnter( () -> {
                    extend.setTargetPosition(20);//set to acc position
                } )

                .state(SlideState.HPTAKE)
                .onEnter( () -> {
                    extend.setTargetPosition(20);//set to acc position
                } )

                .state(SlideState.PRECLIP)
                .onEnter( () -> {
                    extend.setTargetPosition(20);//set to acc position
                } )

                .state(SlideState.POSTCLIP)
                .onEnter( () -> {
                    extend.setTargetPosition(20);//set to acc position
                } )

                .state(SlideState.BASKET)
                .onEnter( () -> {
                    extend.setTargetPosition(20);//set to acc position
                } )

                .state(SlideState.DRIVERCONTROL)
                .onEnter( () -> {
                    //controls
                } )

                .build();
    }
}
