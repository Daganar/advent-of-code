package uk.co.techbound.adentofcode.y2019.p2;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class OpcodeFunctions {

    private final EnumMap<Opcode, BiConsumer<int[], Integer>> opcodeFunctions;

    private OpcodeFunctions() {
        this.opcodeFunctions = new EnumMap<>(Map.of(
            Opcode.ADD, this::add,
            Opcode.MULTIPLY, this::multiply
        ));
    }

    public BiConsumer<int[], Integer> getFunction(int[] array, int i) {
        Opcode opcode = Opcode.getOpcode(array[i]);
        return opcodeFunctions.get(opcode);
    }

    public void add(int[] array, int i) {
        int first = getParameterValue(array, i, 1);
        int second = getParameterValue(array, i, 2);
        int result = first + second;
        setParameterValue(array, i, 3, result);
    }

    public void multiply(int[] array, int i) {
        int first = getParameterValue(array, i, 1);
        int second = getParameterValue(array, i, 2);
        int result = first * second;
        setParameterValue(array, i, 3, result);
    }

    public int getParameterValue(int[] array, int i, int offset) {
        int index = getIndex(array, i, offset);
        return array[index];
    }

    public void setParameterValue(int[] array, int i, int offset, int value) {
        int index = getIndex(array, i, offset);
        array[index] = value;
    }

    public int getIndex(int[] array, int i, int offset) {
        return array[i + offset];
    }
}
