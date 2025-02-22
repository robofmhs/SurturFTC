package RijoyTeleOp;

import com.sfdev.assembly.state.*;
import Subsystems.Claw;

public class ClawFSM {
    public ClawFSM(){

    }

    public enum ClawState {
        OPEN_SPIN,
        OPEN_IDLE,
        OPEN_REVERSE,
        CLOSE
    }

    public static StateMachine getClawMachine(Claw intake) {
        return new StateMachineBuilder()
                .state(ClawState.OPEN_SPIN)
                .onEnter( () -> {
                    intake.setPosition(20);//set to acc position
                    intake.setPower(20,20);//set to acc power
                    //i put back the EV code hope u dont mind
                } )

                .state(ClawState.OPEN_IDLE)
                .onEnter( () -> {
                    intake.setPosition(20);//set to acc position
                    intake.setPower(0,0);//set to acc power
                    //i put back the EV code hope u dont mind
                } )

                .state(ClawState.OPEN_REVERSE)
                .onEnter( () -> {
                    intake.setPosition(20);//set to acc position
                    intake.setPower(-20,-20);//set to acc power
                    //i put back the EV code hope u dont mind
                } )

                .state(ClawState.CLOSE)
                .onEnter( () -> {
                    intake.setPosition(20);//set to acc position
                    intake.setPower(0,0);//set to acc power
                } )

                .build();
    }
}
