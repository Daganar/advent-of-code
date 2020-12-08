package uk.co.techbound.adentofcode.y2020.p7;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Component
public class Problem7 extends AbstractProblemSolver<Map<String, Problem7.BagNode>, Long> {

    @Override
    protected Long partOne(Map<String, Problem7.BagNode> input) {
        return StreamEx.of(input.values())
            .filter(colour -> containsColour("shiny gold", colour))
            .count();
    }

    public boolean containsColour(String toFind, BagNode startingBag) {
        Set<BagNode> currentBagContents = startingBag.getContents().keySet();
        for(BagNode bagNode : currentBagContents) {
            if(toFind.equals(bagNode.getColour())) {
                return true;
            } else if (containsColour(toFind, bagNode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Long partTwo(Map<String, Problem7.BagNode> input) {
        return countContents(input.get("shiny gold"));
    }

    public long countContents(BagNode current) {
        return EntryStream.of(current.getContents())
            .mapKeyValue((subBag, count) -> count + count * countContents(subBag))
            .mapToLong(Long::valueOf)
            .sum();

    }

    @Override
    protected Map<String, BagNode> convertInput(StreamEx<String> lines) {
        Pattern pattern = Pattern.compile("((\\d+) (.*)|(no other)) bags?");
        Map<String, Map<String, Long>> bagCounts = lines.map(line -> line.split(" bags contain "))
            .mapToEntry(split -> split[0], split -> split[1])
            .mapValues(Problem7::removeLastCharacter)
            .mapValues(rules -> rules.split(", "))
            .mapValues(rules -> StreamEx.of(rules)
                .map(rule -> {
                    Map<String, Long> ruleMap;
                    Matcher matcher = pattern.matcher(rule);
                    if (matcher.matches()) {
                        String colour = matcher.group(3);
                        if ("no other".equals(matcher.group(4))) {
                            ruleMap = Collections.emptyMap();
                        } else {
                            long count = Integer.parseInt(matcher.group(2));
                            ruleMap = Map.of(colour, count);
                        }
                        return ruleMap;
                    } else {
                        throw new RuntimeException("Unknown rule!: " + rule);
                    }
                })
                .flatMapToEntry(Function.identity())
                .toMap())
            .toMap();
        Map<String, BagNode> bagNodeMap = new HashMap<>();
        bagCounts.keySet().forEach(colour -> putBagContentsInMap(colour, bagNodeMap, bagCounts));
        return bagNodeMap;
    }

    private void putBagsInMap(Map<String, BagNode> bagNodeMap, Map<String, List<Map<String, Integer>>> bagCounts) {

    }

    private void putBagContentsInMap(String bagColour, Map<String, BagNode> bagNodeMap, Map<String, Map<String, Long>> bagCounts) {
        if(!bagNodeMap.containsKey(bagColour)) {
            Map<String, Long> currentBagContents = bagCounts.get(bagColour);
            if (!bagNodeMap.keySet().containsAll(currentBagContents.keySet())) {
                 currentBagContents.keySet().forEach(colour -> putBagContentsInMap(colour, bagNodeMap, bagCounts));
            }
            BagNode newBagNode = EntryStream.of(currentBagContents)
                .mapToKey((key, count) -> bagNodeMap.get(key))
                .toMapAndThen(map -> new BagNode(bagColour, map));
            bagNodeMap.put(newBagNode.getColour(), newBagNode);
        }
    }

    private static String removeLastCharacter(String input) {
        return input.substring(0, input.length() - 1);
    }

    @Value
    public static class BagNode {
        String colour;
        Map<BagNode, Long> contents;
    }
}
