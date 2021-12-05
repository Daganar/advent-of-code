package uk.co.techbound.adentofcode.y2020.p19;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;
import java.util.function.Function;

@Log4j2
@Component
public class Problem19 extends AbstractProblemSolver<Pair<Map<Integer, Problem19.Rule>, List<String>>, Long> {

    Set<Integer> seen = new HashSet<>();

    @Override
    protected Long partOne(Pair<Map<Integer, Rule>, List<String>> input) {
        Map<Integer, Rule> ruleMap = input.getLeft();
        Rule ruleZero = ruleMap.get(0);

        return input.getRight().stream().filter(line -> matches(ruleMap, line, ruleZero) == line.length())
            .count();
    }

    @Override
    protected Long partTwo(Pair<Map<Integer, Rule>, List<String>> input) {
        Map<Integer, Rule> ruleMap = input.getLeft();
        Map<Integer, Set<String>> possibleStrings = possibleStrings(ruleMap);
        Set<String> fourtyTwo = possibleStrings.get(42);
        Set<String> thirtyOne = possibleStrings.get(31);

        return input.getRight().stream()
            .filter(line -> checkRuleZero(line, fourtyTwo, thirtyOne))
            .peek(log::info)
            .count();
    }

    private boolean checkRuleZero(String line, Set<String> fourtyTwo, Set<String> thirtyOne) {
        int prefixCount = 0;
        while(true) {
            boolean foundPrefixAgain = false;
            for(String prefix : fourtyTwo) {
                if(line.startsWith(prefix)) {
                    line = line.substring(prefix.length());
                    prefixCount++;
                    foundPrefixAgain = true;
                }
            }
            if(!foundPrefixAgain) {
                break;
            }
        }
        int suffixCount = 0;
        while(true) {
            boolean foundSuffixAgain = false;
            for(String suffix : thirtyOne) {
                if(line.startsWith(suffix)) {
                    line = line.substring(suffix.length());
                    suffixCount ++;
                    foundSuffixAgain = true;
                }
            }
            if(!foundSuffixAgain) {
                break;
            }
        }
        return suffixCount < prefixCount && suffixCount > 0 && prefixCount >= 2 && line.isEmpty();
    }

    public String match(Map<Integer, Rule> ruleMap, int currentRuleIndex, String currentString) {
        Rule currentRule = ruleMap.get(currentRuleIndex);
        if(!currentRule.getSingleCharacter().isEmpty()) {
            if(currentString.startsWith(currentRule.getSingleCharacter())) {
                return currentRule.getSingleCharacter();
            }
        }
        List<List<Integer>> subRuleIndexes = currentRule.getSubRuleIndexes();
        for (List<Integer> subRuleIndex : subRuleIndexes) {
            String currentMatch = "";
            for (Integer ruleIndex : subRuleIndex) {
                String nextPart = StringUtils.substringAfter(currentString, currentMatch);
                if(nextPart.isEmpty()) {
                    currentMatch = "";
                    break;
                }
                String match = match(ruleMap, ruleIndex, nextPart);
                if (match.isEmpty()) {
                    currentMatch = "";
                    break;
                } else {
                    currentMatch += match;
                }
            }
            if(!currentMatch.isEmpty()) {
                return currentMatch;
            }
        }
        return "";
    }

    public Map<Integer, Set<String>> possibleStrings(Map<Integer, Rule> ruleMap) {
        Map<Integer, Rule> nonRecursiveRules = EntryStream.of(ruleMap)
            .removeKeys(key -> key == 8)
            .removeKeys(key -> key == 11)
            .removeKeys(key -> key == 0)
            .toMap();
        Map<Integer, Set<String>> possibleStrings = EntryStream.of(nonRecursiveRules)
            .removeValues(rule -> rule.getSingleCharacter().isEmpty())
            .mapValues(Rule::getSingleCharacter)
            .mapValues(Set::of)
            .toMap();
        nonRecursiveRules.keySet().removeAll(possibleStrings.keySet());
        while(!nonRecursiveRules.isEmpty()) {
            Map<Integer, Set<String>> nextPossibleStrings = EntryStream.of(nonRecursiveRules)
                .filterValues(rule -> StreamEx.of(rule.getSubRuleIndexes()).flatMap(List::stream).allMatch(possibleStrings::containsKey))
                .mapValues((rule) -> StreamEx.of(rule.getSubRuleIndexes())
                    .flatCollection(subRuleIndexes -> possibleStringForSubIndexes(subRuleIndexes, possibleStrings))
                    .toSet()).toMap();
            possibleStrings.putAll(nextPossibleStrings);
            nonRecursiveRules.keySet().removeAll(nextPossibleStrings.keySet());
        }

        return possibleStrings;
    }

    public Set<String> possibleStringForSubIndexes(List<Integer> subIndexes, Map<Integer, Set<String>> possibleStrings) {
        Set<String> possibleStringsForSubIndexes = new HashSet<>();
        for(Integer subIndex : subIndexes) {
            Set<String> stringsForCurrentSubIndex = possibleStrings.get(subIndex);
            if(possibleStringsForSubIndexes.isEmpty()) {
                possibleStringsForSubIndexes.addAll(stringsForCurrentSubIndex);
            } else {
                Set<String> newPossibleStringsForSubIndexes = new HashSet<>();
                for(String prefix : possibleStringsForSubIndexes) {
                    for(String suffix : stringsForCurrentSubIndex) {
                        newPossibleStringsForSubIndexes.add(prefix + suffix);
                    }
                }
                possibleStringsForSubIndexes = newPossibleStringsForSubIndexes;
            }
        }
        return possibleStringsForSubIndexes;
    }

    @Override
    protected Pair<Map<Integer, Rule>, List<String>> convertInput(StreamEx<String> lines) {
        List<List<String>> inputGroups = lines.groupRuns((l, r) -> !r.trim().isEmpty())
            .toList();
        Map<Integer, Rule> rules = StreamEx.of(inputGroups.get(0))
            .map(line -> {
                int index = Integer.parseInt(StringUtils.substringBefore(line, ":"));
                String rulesString = StringUtils.substringAfter(line, ": ");
                String singleCharacter;
                List<List<Integer>> indexes;
                if (rulesString.contains("\"")) {
                    char c = rulesString.charAt(1);
                    singleCharacter = String.valueOf(c);
                    indexes = List.of();
                } else {
                    indexes = StreamEx.of(rulesString.split("\\|"))
                        .map(subRule -> StreamEx.of(subRule.split(" ")).remove(String::isEmpty).map(Integer::valueOf).toList())
                        .toList();
                    singleCharacter = "";
                }
                return new Rule(index, singleCharacter, indexes);
            }).toMap(Rule::getIndex, Function.identity());

        List<String> values = inputGroups.get(1);
        values.removeIf(String::isBlank);
        return Pair.of(rules, values);
    }


    public int matches(Map<Integer, Rule> ruleMap, String value, Rule rule) {
        if(value.isEmpty()) {
            return -1;
        }
        if(isSingleCharacterRule(rule)) {
            return value.equals(rule.getSingleCharacter()) ? 1 : -1;
        }
        return anyMatch(ruleMap, value, rule);
    }

    public int anyMatch(Map<Integer, Rule> ruleMap, String value, Rule rule) {
        for(List<Integer> subRule : rule.getSubRuleIndexes()) {
            int subSize;
            if((subSize = allMatch(ruleMap, value, subRule, rule)) != -1) {
                return subSize;
            }
        }
        return -1;
    }

    public int allMatch(Map<Integer, Rule> ruleMap, String value, List<Integer> indexes, Rule containingRule) {
        int start = 0;
        for(Integer index : indexes) {
            Rule rule = ruleMap.get(index);
            int length = countLength(ruleMap, rule);
            if(index == 11 || index == 8) {
                int baseLength = length;
                length *= 10;
                for(length *= 10; length > 0; length -= baseLength) {
                    if(length > value.length()) {
                        // skip
                    } else if (matches(ruleMap, StringUtils.substring(value, start, start + length), rule) == length) {
                        break;
                    }
                }
                if (matches(ruleMap, StringUtils.substring(value, start, start + length), rule) != length) {
                    return -1;
                }
            } else if (matches(ruleMap, StringUtils.substring(value, start, start + length), rule) != length) {
                return -1;
            }
            start += length;
        }
        return start == value.length() ? start : -1;
    }

    public boolean isCorrectLength(Map<Integer, Rule> ruleMap, String value, Rule rule) {
        return countLength(ruleMap, rule) == value.length();
    }

    int countLength(Map<Integer, Rule> ruleMap, Rule currentRule) {
        if(isSingleCharacterRule(currentRule)) {
            return 1;
        }
        return StreamEx.of(currentRule.getSubRuleIndexes().iterator().next()).map(ruleMap::get).mapToInt(rule -> countLength(ruleMap, rule)).sum();
    }

    private boolean isSingleCharacterRule(Rule currentRule) {
        return !currentRule.getSingleCharacter().isEmpty();
    }


    @Value
    public static class Rule {
        int index;
        String singleCharacter;
        List<List<Integer>> subRuleIndexes;
    }

}
