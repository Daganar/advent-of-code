package uk.co.techbound.adentofcode.y2020.p2;

import lombok.Value;
import one.util.streamex.IntStreamEx;

@Value
public class MinMaxCharacterRule {
    int minOccurences;
    int maxOccurences;
    char aChar;

    public boolean meetsCriteria(String input) {
        long count = IntStreamEx.of(input.toCharArray()).filter(a -> a == aChar).count();
        return count >= minOccurences && count <= maxOccurences;
    }
}
