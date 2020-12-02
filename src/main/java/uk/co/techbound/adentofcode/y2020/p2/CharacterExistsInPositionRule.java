package uk.co.techbound.adentofcode.y2020.p2;

import lombok.Value;

@Value
public class CharacterExistsInPositionRule {
    int firstIndex;
    int secondIndex;
    char aChar;

    public boolean meetsCriteria(String input) {
        return input.charAt(firstIndex) == aChar ^ input.charAt(secondIndex) == aChar;
    }
}
