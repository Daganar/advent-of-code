package uk.co.techbound.adentofcode.y2020.p2;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Component
public class Problem2 extends AbstractProblemSolver<StreamEx<List<String>>, Long> {

    private final ClassPathResource classPathResource = new ClassPathResource("2a.txt");
    private final Pattern pattern = Pattern.compile("(\\d+)-(\\d+) (.): (.*)");

    private List<String> extractMatches(String input) {
        Matcher matcher = pattern.matcher(input);
        List<String> matches = new ArrayList<>();
        if(matcher.matches()) {
            for(int i = 0; i < matcher.groupCount(); i++) {
                matches.add(matcher.group(i + 1));
            }
        }
        return matches;
    }

    @Override
    protected Long partOne(StreamEx<List<String>> input) {
        return input
            .mapToEntry(list -> new MinMaxCharacterRule(Integer.parseInt(list.get(0)) - 1, Integer.parseInt(list.get(1)) - 1, list.get(2).charAt(0)), list -> list.get(3))
            .filterKeyValue(MinMaxCharacterRule::meetsCriteria)
            .values()
            .count();
    }

    @Override
    protected Long partTwo(StreamEx<List<String>> input) {
        return input
                .mapToEntry(list -> new CharacterExistsInPositionRule(Integer.parseInt(list.get(0)) - 1, Integer.parseInt(list.get(1)) - 1, list.get(2).charAt(0)), list -> list.get(3))
                .filterKeyValue(CharacterExistsInPositionRule::meetsCriteria)
                .values()
                .count();
    }

    @Override
    protected StreamEx<List<String>> convertInput(StreamEx<String> lines) {
        return lines.map(this::extractMatches);
    }
}
