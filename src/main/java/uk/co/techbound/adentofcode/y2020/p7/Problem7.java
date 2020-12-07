package uk.co.techbound.adentofcode.y2020.p7;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Component
public class Problem7 extends AbstractProblemSolver<Map<String, List<Map<String, Integer>>>, Long> {

    @Override
    protected Long partOne(Map<String, List<Map<String, Integer>>> input) {
        Map<String, List<String>> bagRules = EntryStream.of(input)
            .mapValues(list -> StreamEx.of(list).flatCollection(Map::keySet).toList())
            .toMap();
        return StreamEx.of(bagRules.keySet())
            .filter(colour -> containsColour("shiny gold", colour, bagRules))
            .count();
    }

    public boolean containsColour(String toFind, String startingColour, Map<String, List<String>> bagRules) {
        List<String> currentBagContents = bagRules.get(startingColour);
        for(String subBagColour : currentBagContents) {
            if(toFind.equals(subBagColour)) {
                return true;
            } else if (containsColour(toFind, subBagColour, bagRules)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Long partTwo(Map<String, List<Map<String, Integer>>> input) {
        return countContents(input, "shiny gold");
    }

    public long countContents(Map<String, List<Map<String, Integer>>> bagRules, String colour) {
        return StreamEx.of(bagRules.get(colour))
            .mapToLong(contents ->
                EntryStream.of(contents)
                    .mapKeyValue((subColour, count) -> count + countContents(bagRules, subColour) * count)
                    .mapToLong(Long::valueOf)
                    .sum()
            )
            .sum();
    }

    @Override
    protected Map<String, List<Map<String, Integer>>> convertInput(StreamEx<String> lines) {
        Pattern pattern = Pattern.compile("((\\d+) (.*)|(no other)) bags?");
        return lines.map(line -> line.split(" bags contain "))
            .mapToEntry(split -> split[0], split -> split[1])
            .mapValues(Problem7::removeLastCharacter)
            .mapValues(rules -> rules.split(", "))
            .mapValues(rules -> StreamEx.of(rules)
                .map(rule -> {
                    Map<String, Integer> ruleMap;
                    Matcher matcher = pattern.matcher(rule);
                    if (matcher.matches()) {
                        String colour = matcher.group(3);
                        if ("no other".equals(matcher.group(4))) {
                            ruleMap = Collections.emptyMap();
                        } else {
                            int count = Integer.parseInt(matcher.group(2));
                            ruleMap = Map.of(colour, count);
                        }
                        return ruleMap;
                    } else {
                        throw new RuntimeException("Unknown rule!: " + rule);
                    }
                })
                .remove(Map::isEmpty)
                .toList())
            .toMap();
    }

    private static String removeLastCharacter(String input) {
        return input.substring(0, input.length() - 1);
    }
}
