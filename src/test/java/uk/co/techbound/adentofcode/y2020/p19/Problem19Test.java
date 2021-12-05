package uk.co.techbound.adentofcode.y2020.p19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem19Test {

    Map<Integer, Problem19.Rule> ruleMap = makeRuleMap(
    List.of(List.of(1, 1, 2), List.of(1, 1, 3)),
        List.of(List.of(2, 3), List.of(3, 2)),
        "a",
        "b",
        List.of(List.of(0, 2, 0)),
        List.of(List.of(2), List.of(3)),
        List.of(List.of(2, 3, 2))
    );

    private Problem19 problem19 = new Problem19();

    @Test
    public void countTest() {
        Assertions.assertEquals(5, problem19.countLength(ruleMap, ruleMap.get(0)));
        Assertions.assertEquals(11, problem19.countLength(ruleMap, ruleMap.get(4)));
    }

//    @Test
//    public void matchesExact() {
//        Assertions.assertTrue(problem19.matches(ruleMap, "a", ruleMap.get(2)));
//        Assertions.assertTrue(problem19.matches(ruleMap, "b", ruleMap.get(3)));
//    }
//
//    @Test
//    public void matchesEither() {
//        Assertions.assertTrue(problem19.matches(ruleMap, "a", ruleMap.get(5)));
//        Assertions.assertTrue(problem19.matches(ruleMap, "b", ruleMap.get(5)));
//    }
//
//    @Test
//    public void matchesAdjacent() {
//        Assertions.assertTrue(problem19.matches(ruleMap, "aba", ruleMap.get(6)));
//    }



    Map<Integer, Problem19.Rule> makeRuleMap(Object... input) {
        HashMap<Integer, Problem19.Rule> ruleMap = new HashMap<>();
        for(int i = 0; i < input.length; i++) {
            if(input[i] instanceof String) {
                ruleMap.put(i, new Problem19.Rule(i, (String) input[i], List.of()));
            } else {
                List<List<Integer>> indexes = (List<List<Integer>>) input[i];
                ruleMap.put(i, new Problem19.Rule(i, "", indexes));
            }
        }
        return ruleMap;
    }

}