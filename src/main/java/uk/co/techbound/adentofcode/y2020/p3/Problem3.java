package uk.co.techbound.adentofcode.y2020.p3;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;

@Log4j2
@Component
public class Problem3 extends AbstractProblemSolver<List<String>> {

    private int countTreesOnSlope(List<String> map, int xIncrement, int yIncrement) {
        log.debug("Counting trees for slope: Right {}, Down {}", xIncrement, yIncrement);
        int initialWidth = map.iterator().next().length();
        int x = 0;
        int treeCount = 0;
        for(int y = 0; y < map.size(); y+= yIncrement) {
            String currentRow = map.get(y);
            char currentLocation = currentRow.charAt(x % initialWidth);
            if(currentLocation == '#') {
                treeCount++;
                log.trace("Tree found at {},{}", x, y);
            } else {
                log.trace("Open space found at {}, {}", x, y);
            }
            x += xIncrement;
        }
        log.debug("treeCount: {}", treeCount);
        return treeCount;
    }

    @Override
    protected Object partOne(List<String> input) {
        return countTreesOnSlope(input, 3, 1);
    }

    @Override
    protected Object partTwo(List<String> map) {
        long a = countTreesOnSlope(map, 1, 1);
        long b = countTreesOnSlope(map, 3, 1);
        long c = countTreesOnSlope(map, 5, 1);
        long d = countTreesOnSlope(map, 7, 1);
        long e = countTreesOnSlope(map, 1, 2);
        return a*b*c*d*e;
    }

    @Override
    protected List<String> convertInput(StreamEx<String> lines) {
        return lines.toList();
    }
}
