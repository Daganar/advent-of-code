package uk.co.techbound.adentofcode.y2019.p2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import one.util.streamex.StreamEx;

import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum Opcode {

    ADD(1, 3),
    MULTIPLY(2, 3),
    ;

    private final int opcode;
    private final int numberOfParameters;

    private static Map<Integer, Opcode> OPCODES = StreamEx.of(Opcode.values()).toMap(Opcode::getOpcode, Function.identity());

    public static Opcode getOpcode(int opcode) {
        if(!OPCODES.containsKey(opcode)) {
            throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
        return OPCODES.get(opcode);
    }

}
