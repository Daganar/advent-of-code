package uk.co.techbound.adentofcode.y2020.p12;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

@Log4j2
@Component
public class Problem12 extends AbstractProblemSolver<StreamEx<Pair<String, Integer>>, Integer> {

    @Override
    protected Integer partOne(StreamEx<Pair<String, Integer>> instructions) {
        MutableInt x = new MutableInt(0);
        MutableInt y = new MutableInt(0);
        MutableInt facingAngle = new MutableInt(90);
        instructions.forEach(pair -> {
            updatePositions(x, y, facingAngle, pair);
        });

        return Math.abs(x.getValue()) + Math.abs(y.getValue());
    }

    private void updatePositions(MutableInt x, MutableInt y, MutableInt facingAngle, Pair<String, Integer> pair) {
        switch(pair.getLeft()) {
            case "N":
                y.add(pair.getRight());
                break;
            case "E":
                x.add(pair.getRight());
                break;
            case "S":
                y.subtract(pair.getRight());
                break;
            case "W":
                x.subtract(pair.getRight());
                break;
            case "F":
                updatePositions(x, y, facingAngle, Pair.of(convertToDirection(facingAngle), pair.getRight()));
                break;
            case "L":
                facingAngle.subtract(pair.getRight());
                break;
            case "R":
                facingAngle.add(pair.getRight());
                break;

            default:
                throw new IllegalStateException("Unknown operation:" + pair);
        }
    }

    private String convertToDirection(MutableInt facingAngle) {
        Integer value = facingAngle.getValue();
        while (value < 0) {
            value+= 360;
        }
        switch (value / 90 % 4) {
            case 0: return "N";
            case 1: return "E";
            case 2: return "S";
            case 3: return "W";
            default:
                throw new IllegalStateException("Angle:" + value);
        }
    }


    @Override
    protected Integer partTwo(StreamEx<Pair<String, Integer>> instructions) {
        MutableInt shipX = new MutableInt(0);
        MutableInt shipY = new MutableInt(0);
        MutableInt waypointX = new MutableInt(10);
        MutableInt waypointY = new MutableInt(1);
        instructions.forEach(pair -> {
            updatePositions2(shipX, shipY, waypointX, waypointY, pair);
            log.info("{} -> Ship({}, {}), waypoint({},{})",pair, shipX, shipY, waypointX, waypointY);
        });

        return Math.abs(shipX.getValue()) + Math.abs(shipY.getValue());
    }

    private void updatePositions2(MutableInt shipX, MutableInt shipY, MutableInt waypointX, MutableInt waypointY, Pair<String, Integer> pair) {
        switch(pair.getLeft()) {
            case "N":
                waypointY.add(pair.getRight());
                break;
            case "E":
                waypointX.add(pair.getRight());
                break;
            case "S":
                waypointY.subtract(pair.getRight());
                break;
            case "W":
                waypointX.subtract(pair.getRight());
                break;
            case "F":
                shipX.add(waypointX.getValue() * pair.getRight());
                shipY.add(waypointY.getValue() * pair.getRight());
                break;
            case "L":
                rotate(360 - pair.getRight(), waypointX, waypointY);
                break;
            case "R":
                rotate(pair.getRight(), waypointX, waypointY);
                break;

            default:
                throw new IllegalStateException("Unknown operation:" + pair);
        }
    }

    private void rotate(Integer right, MutableInt waypointX, MutableInt waypointY) {
        for(int i = 0; i < right / 90; i++) {
            int x = waypointX.getValue();
            int y = waypointY.getValue();
            waypointX.setValue(y);
            waypointY.setValue(-1 * x);
        }
    }

    @Override
    protected StreamEx<Pair<String, Integer>> convertInput(StreamEx<String> lines) {
        return lines
            .map(line -> Pair.of(line.substring(0, 1), Integer.valueOf(line.substring(1))));
    }
}
