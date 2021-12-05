package uk.co.techbound.adentofcode.y2021.p3;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;

@Log4j2
@Component
public class Problem3 extends AbstractProblemSolver<List<String>, Long> {

    @Override
    protected Long partOne(List<String> lines) {
        String gammeRate = "";
        String epsilonRate = "";
        int length = lines.get(0).length();
        for(int i = 0; i < length; i++) {
            List<String> mostCommonBitInPosition = mostCommonBitInPosition(lines, i);
            if(mostCommonBitInPosition.size() > 1) {
                log.info("equal");
            }
            gammeRate += mostCommonBitInPosition.get(0);
            epsilonRate += mostCommonBitInPosition.contains("0") ? "1" : "0";
        }
        int gamma = Integer.parseInt(gammeRate, 2);
        int epsilon = Integer.parseInt(epsilonRate, 2);
        log.info("gammaRate {}, gamma {}", gammeRate, gamma);
        log.info("epsilonRate {}, epsilon {}", epsilonRate, epsilon);
        return Long.valueOf(gamma * epsilon);
    }



    private List<String> mostCommonBitInPosition(List<String> lines, int position) {
        return mostCommonBitInPosition(lines, position, List.of("1", "0"));
    }

    private List<String> mostCommonBitInPosition(List<String> lines, int position, List<String> valueIfEqual) {
        int oneCount = 0;
        int zeroCount = 0;
        for(String line: lines) {
            if(line.charAt(position) == '0') {
                zeroCount++;
            } else {
                oneCount++;
            }
        }
        if(oneCount == zeroCount) {
            return valueIfEqual;

        } else if(oneCount > zeroCount) {
            return List.of("1");
        } else {
            return List.of("0");
        }
    }

    private List<String> leastCommonBitInPosition(List<String> lines, int position, List<String> valueIfEqual) {
        List<String> mostCommon = mostCommonBitInPosition(lines, position, List.of("1", "0"));
        if(mostCommon.size() == 2) {
            return valueIfEqual;
        }
        return mostCommon.contains("1") ? List.of("0") : List.of("1");
    }

    @Override
    protected Long partTwo(List<String> lines) {
        List<String> lines2 = lines;
        int length = lines.get(0).length();
        String oxygen = null;
        for(int i = 0; i < length; i++) {
            if(lines.size() == 1) {
                break;
            }
            List<String> mostCommonBitInPosition = mostCommonBitInPosition(lines, i, List.of("1"));
            final int iFinal = i;
            lines = StreamEx.of(lines).filter(line -> mostCommonBitInPosition.contains(String.valueOf(line.charAt(iFinal)))).toList();
        }
        log.info("oxygen {}", lines);
        oxygen = lines.get(0);
        lines = lines2;
        String co2 = null;
        for(int i = 0; i < length; i++) {
            if(lines.size() == 1) {
                break;
            }
            List<String> leastCommonBitInPosition = leastCommonBitInPosition(lines, i, List.of("0"));
            final int iFinal = i;
            lines = StreamEx.of(lines).filter(line -> leastCommonBitInPosition.contains(String.valueOf(line.charAt(iFinal)))).toList();
        }
        log.info("co2 {}", lines);
        co2 = lines.get(0);

        return Long.valueOf(Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2));
    }

    @Override
    protected List<String> convertInput(StreamEx<String> lines) {
        return lines.toList();
    }
}
