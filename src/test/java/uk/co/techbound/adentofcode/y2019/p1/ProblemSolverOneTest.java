package uk.co.techbound.adentofcode.y2019.p1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ProblemSolverOneTest {

    ProblemSolverOne problemSolverOne = new ProblemSolverOne();

    @ParameterizedTest
    @MethodSource
    void calculateFuelOfModule(long input, long expected) {
        Assertions.assertEquals(expected, problemSolverOne.calculateFuelOfModule(input));
    }

    private static Stream<Arguments> calculateFuelOfModule() {
        return Stream.of(
            Arguments.of(12, 2),
            Arguments.of(14, 2),
            Arguments.of(1969, 654),
            Arguments.of(100756, 33583),
            Arguments.of(0, 0),
            Arguments.of(1, 0),
            Arguments.of(4, 0),
            Arguments.of(6, 0),
            Arguments.of(7, 0)
        );
    }

    @ParameterizedTest
    @MethodSource
    void calculateFuelOfFuel(long input, long expected) {
        Assertions.assertEquals(expected, problemSolverOne.calculateFuelOfFuel(input));
    }

    private static Stream<Arguments> calculateFuelOfFuel() {
        return Stream.of(
            Arguments.of(2, 0),
            Arguments.of(654, 312),
            Arguments.of(33583, 16763)
        );
    }

    @ParameterizedTest
    @MethodSource
    void calculateFuelOfModuleAndFuel(long input, long expected) {
        Assertions.assertEquals(expected, problemSolverOne.calculateFuelOfModuleAndFuel(input));
    }

    private static Stream<Arguments> calculateFuelOfModuleAndFuel() {
        return Stream.of(
            Arguments.of(14, 2),
            Arguments.of(1969, 966),
            Arguments.of(100756, 50346)
        );
    }
}