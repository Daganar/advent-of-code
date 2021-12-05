package uk.co.techbound.adentofcode.y2021.p2;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;

@Log4j2
@Component
public class Problem2 extends AbstractProblemSolver<List<String>, Long> {

    @Override
    protected Long partOne(List<String> lines) {
        MutableInt horizontal = new MutableInt(0);
        MutableInt depth = new MutableInt(0);
        StreamEx.of(lines)
            .map(line -> line.split(" "))
            .forEach(parts -> {
                switch (parts[0]) {
                    case "up":
                        depth.subtract(Integer.parseInt(parts[1]));
                        break;
                    case "down":
                        depth.add(Integer.parseInt(parts[1]));
                        break;
                    case "forward":
                        horizontal.add(Integer.parseInt(parts[1]));
                        break;
                    default:
                        break;
                }
            });
        return horizontal.longValue() * depth.longValue();
    }

    @Override
    protected Long partTwo(List<String> lines) {
        MutableInt horizontal = new MutableInt(0);
        MutableInt depth = new MutableInt(0);
        MutableInt aim = new MutableInt(0);
        StreamEx.of(lines)
            .map(line -> line.split(" "))
            .forEach(parts -> {
                switch (parts[0]) {
                    case "up":
                        aim.subtract(Integer.parseInt(parts[1]));
                        break;
                    case "down":
                        aim.add(Integer.parseInt(parts[1]));
                        break;
                    case "forward":
                        horizontal.add(Integer.parseInt(parts[1]));
                        depth.add(Integer.parseInt(parts[1]) * aim.longValue());
                        break;
                    default:
                        break;
                }
            });
        return horizontal.longValue() * depth.longValue();
    }

    @Override
    protected List<String> convertInput(StreamEx<String> lines) {
        return lines.toList();
    }
}
